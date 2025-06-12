package dev.functionalnotpretty.githubpoc.services;

import dev.functionalnotpretty.githubpoc.entities.ProjectEntity;
import dev.functionalnotpretty.githubpoc.entities.ProjectEvent;
import dev.functionalnotpretty.githubpoc.entities.ProjectRepo;
import dev.functionalnotpretty.githubpoc.entities.ProjectStatus;
import dev.functionalnotpretty.githubpoc.exceptions.BadRequestException;
import dev.functionalnotpretty.githubpoc.exceptions.GitRequestException;
import dev.functionalnotpretty.githubpoc.exceptions.ResourceNotFoundException;
import dev.functionalnotpretty.githubpoc.repositories.ProjectEventsRepository;
import dev.functionalnotpretty.githubpoc.repositories.ProjectRepository;
import dev.functionalnotpretty.githubpoc.restservice.GithubRestClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProjectService {

    private final static Logger log = LoggerFactory.getLogger(ProjectService.class);
    private final ProjectRepository projectRepository;
    private final ProjectEventsRepository eventsRepository;
    private final GithubRestClient githubRestClient;

    ProjectService(ProjectRepository projectRepository, ProjectEventsRepository eventsRepository, GithubRestClient githubRestClient) {
        this.projectRepository = projectRepository;
        this.eventsRepository = eventsRepository;
        this.githubRestClient = githubRestClient;
    }

    public List<ProjectEntity> getAllProjectsByUserId(String userId) {
        return projectRepository.findAllByUserId(userId);
    }

    public ProjectEntity getProject(String projectId) {
        return this.projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
//        return this.projectRepository.findByIdAndProjectId(projectId, projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    }

    public ProjectEntity createProject(ProjectEntity projectEntity, boolean addCommits) {
        if (this.projectRepository.existsByRepo_Name(projectEntity.getRepo().name())) {
            log.info("Project with this repository already exists");
            throw new BadRequestException("Project with this repository already exists");
        }

        if (projectEntity.getStatus() != ProjectStatus.ACTIVE ) {
            log.info("Invalid project status");
            throw new BadRequestException("Project status must be active for create");
        }

        var created = Instant.now().toString();

        // generate our own id and use the same id for the project id as the partition key
        var uuid = UUID.randomUUID().toString();
        projectEntity.setId(uuid);
        projectEntity.setProjectId(uuid);
        projectEntity.setCreatedAt(created);
        projectEntity.setUpdatedAt(created);


        // if not populating project with the commit message, write the object and return
        if (!addCommits) {
            var result = projectRepository.save(projectEntity);
            log.info("added item {}", result);
            return result;
        }

        if (Objects.equals(projectEntity.getRepo().name(), "")) {
            log.info("bad request, repository name is empty");
            throw new BadRequestException("No repository specified to get commits from.");
        }

        // create the webhook subscription now.
        var resp = this.githubRestClient.createGitHubRepositoryWebHook(projectEntity.getUserId(), projectEntity.getRepo().name());
        log.info("created webhook response {} ", resp);
        //projectEntity.getRepo().
        ProjectRepo newRepo = new ProjectRepo(projectEntity.getRepo().id(), projectEntity.getRepo().name(), projectEntity.getRepo().url(),
                projectEntity.getRepo().isPrivate(), projectEntity.getRepo().createdAt(), Long.toString(resp.id()));
        projectEntity.setRepo(newRepo);

        var items = createCommits(projectEntity.getProjectId(), projectEntity.getUserId(), projectEntity.getRepo().name());
        var result = projectRepository.save(projectEntity);
        // cosmos doesn't have cross container transactions, so if a write fails here not much to do about it now
        this.eventsRepository.saveAll(items);

        log.info("wrote new project item and {} event messages", items.size());

        return result;
    }

    private List<ProjectEvent> createCommits(String projectId, String userId, String repoName) {
        var commits = githubRestClient.getUserRepoCommits(userId, repoName);
        if (commits == null) {
            // at this point the project entity is written
            log.info( "read commits are null from github");
            throw new GitRequestException("Could not read commits from github");
        }

        log.info( "read {} commit messages from github", commits.size());
        var events = new ArrayList<ProjectEvent>();
        for (var commit : commits) {
            try {
                var created = Instant.now().toString();
                var event = new ProjectEvent(userId, projectId,
                        commit.getCommit().get("message").asText(),
                        commit.getCommit().get("author").get("date").asText(),
                        repoName, "", true, created, created);
                events.add(event);
            }
            catch (Exception e) {
                log.error("processing commit error occurred: {}, exception: {}", commit, e.getMessage());
                throw new GitRequestException("an error occurred while reading commit messages from github");
            }
        }
        return events;
    }

    public ProjectEntity updateProject(ProjectEntity projectEntity) {
        var project = this.projectRepository.findById(projectEntity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (project.getStatus() == ProjectStatus.ARCHIVED) {
            log.error("Attempt to update an archived project {}", project);
            throw new BadRequestException("Archived project cannot be updated");
        }

        if (projectEntity.getStatus() == ProjectStatus.ARCHIVED &&
            project.getStatus() == ProjectStatus.ACTIVE) {
            if (!StringUtils.isEmpty(project.getRepo().hookId())) {
                // important to not trust the client and use entity value store in db.
                log.info("archiving project, deleting webhook {}", project.getRepo().hookId());
                try {
                    this.githubRestClient.deleteGitHubWebhook(project.getUserId(), project.getRepo().name(), project.getRepo().hookId());
                }
                catch (RuntimeException ex)
                {
                    log.info("failed to delete webhook {}", project.getRepo().hookId());
                    throw new GitRequestException("failed to delete webhook");
                }
                ProjectRepo newRepo = new ProjectRepo(project.getRepo().id(),project.getRepo().name(), project.getRepo().url(),
                        project.getRepo().isPrivate(),project.getRepo().createdAt(), null);
                projectEntity.setRepo(newRepo);
            } else {
                log.info("archiving project, but hook id is empty. ignoring");
            }
        }

        // todo -- other validations
        // todo -- adding a repo to a blank project down the line
        // todo -- maybe partial entity updates from FE?
        // todo -- trusting the client too much?
        projectEntity.setUpdatedAt(Instant.now().toString());
        return projectRepository.save(projectEntity);
    }

    public void deleteProject(String projectId) {
        var project = this.projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        // cascading deletes
        this.eventsRepository.deleteByProjectId(projectId);
        // delete webhook
        // project.getRepo().name();
        if (project.getStatus() == ProjectStatus.ACTIVE)
        {
            try {
                this.githubRestClient.deleteGitHubWebhook(project.getUserId(), project.getRepo().name(), project.getRepo().hookId() );
            }
            catch (RuntimeException ex)
            {
                log.info("delete webhook ({}) for project {} failed", project.getRepo().hookId() ,projectId);
                throw new GitRequestException("failed to delete webhook in ");
            }
        }
        this.projectRepository.deleteById(projectId);
    }

    public List<ProjectEvent> getAllProjectEvents(String projectId) {
        return this.eventsRepository.findAllByProjectId(projectId);
    }

    public ProjectEvent updateProjectEvent(ProjectEvent projectEvent) {
        // todo -- read the parent project and determine if it is archived
        if (!projectRepository.existsByProjectId(projectEvent.getProjectId())) {
            log.info( "project event {} is not found", projectEvent.getId());
            throw new ResourceNotFoundException("Project event not found");
        }
        projectEvent.setUpdatedDt(Instant.now().toString());
        return this.eventsRepository.save(projectEvent);
    }

    public ProjectEvent createProjectEvent(ProjectEvent projectEvent) {
        // todo -- check parent status, if archived don't allow create
        // ensure the db will generate an id
        projectEvent.setId(null);
        var created = Instant.now().toString();
        projectEvent.setCreatedDt(created);
        projectEvent.setUpdatedDt(created);
        return this.eventsRepository.save(projectEvent);
    }

    public List<ProjectEvent> updateProjectEvents(String projectId, List<ProjectEvent> events) {
        // validate that all objects are for the paths
        for(ProjectEvent event : events) {
            if (!StringUtils.equalsIgnoreCase(event.getProjectId(), projectId)) {
                log.info("bad request, project id {} does not match project id {}", event.getProjectId(), projectId);
                throw new BadRequestException("Invalid project id");
            }

            var created = Instant.now().toString();
            event.setUpdatedDt(created);
        }
        return (List<ProjectEvent>) this.eventsRepository.saveAll(events);
    }

    // event Ids, the object id, is unique per container so don't need the project is
    public void deleteProjectEvent(String projectId, String eventId) {
        if (!eventsRepository.existsByProjectIdAndId(projectId, eventId)) {
            log.info( "specified project event is not found");
            throw new ResourceNotFoundException("Project event not found");
        }

        this.eventsRepository.deleteByProjectIdAndId(projectId, eventId);
    }
}
