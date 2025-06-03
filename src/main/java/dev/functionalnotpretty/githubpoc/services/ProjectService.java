package dev.functionalnotpretty.githubpoc.services;

import dev.functionalnotpretty.githubpoc.entities.ProjectEntity;
import dev.functionalnotpretty.githubpoc.entities.ProjectEvent;
import dev.functionalnotpretty.githubpoc.exceptions.BadRequestException;
import dev.functionalnotpretty.githubpoc.exceptions.GitRequestException;
import dev.functionalnotpretty.githubpoc.exceptions.ResourceNotFoundException;
import dev.functionalnotpretty.githubpoc.models.ProjectDto;
import dev.functionalnotpretty.githubpoc.repositories.ProjectEventsRepository;
import dev.functionalnotpretty.githubpoc.repositories.ProjectRepository;
import dev.functionalnotpretty.githubpoc.restservice.GithubRestClient;
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
    }

    public ProjectEntity createProject(ProjectDto projectEntity, boolean addCommits) {
        var item = new ProjectEntity(projectEntity);
        var created = Instant.now().toString();

        // generate our own id and use the same id for the project id as the partition key
        var uuid = UUID.randomUUID().toString();
        item.setId(uuid);
        item.setProjectId(uuid);
        item.setCreatedAt(created);
        item.setUpdatedAt(created);


        // if not populating project with the commit message, write the object and return
        if (!addCommits) {
            var result = projectRepository.save(item);
            log.info("added item {}", result);
            return result;
        }

        if (Objects.equals(item.getRepo().name(), "")) {
            log.info("bad request, repository name is empty");
            throw new BadRequestException("No repository specified to get commits from.");
        }

        var items = createCommits(item.getProjectId(), projectEntity.userId(), item.getRepo().name());
        var result = projectRepository.save(item);
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
}
