package dev.functionalnotpretty.githubpoc.services;

import dev.functionalnotpretty.githubpoc.controllers.GithubRepoController;
import dev.functionalnotpretty.githubpoc.controllers.ProjectsController;
import dev.functionalnotpretty.githubpoc.entities.ProjectEntity;
import dev.functionalnotpretty.githubpoc.entities.ProjectEvents;
import dev.functionalnotpretty.githubpoc.models.ProjectDto;
import dev.functionalnotpretty.githubpoc.repositories.ProjectEventsRepository;
import dev.functionalnotpretty.githubpoc.repositories.ProjectRepository;
import dev.functionalnotpretty.githubpoc.restservice.GithubRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Objects;

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

    public ProjectEntity createProject(ProjectDto projectEntity, boolean addCommits) {
        var item = new ProjectEntity(projectEntity);
        var created = Instant.now().toString();
        item.setCreatedAt(created);
        item.setUpdatedAt(created);

        // validate the object
        if (addCommits) {
            if (Objects.equals(item.getRepo().name(), "")) {
                // TODO -- error handling
                log.info("bad request, no repo passes");
            }
        }

        var result = projectRepository.save(item);
        log.info("added item {}", result);

        if (addCommits) {
            createCommits(result.getId(), item.getRepo().name());
        }

        return result;
    }

    private void createCommits(String projectId, String repoName) {
        var commits = githubRestClient.getUserRepoCommits("amfritz", repoName);
        if (commits == null) {
            // todo -- better error handling
            log.info( "read commits are null from github");
            return;
        }

        log.info( "read {} commit messages from github", commits.size());
        var events = new ArrayList<ProjectEvents>();
        for (var commit : commits) {
            // String userId, String projectId, String eventDescription, String eventDate, String repoName, String branch_name, boolean isNewEvent
            var event = new ProjectEvents("amfritz", projectId,
                    commit.getCommit().get("message").asText(),
                    commit.getCommit().get("author").get("date").asText(),
                    repoName, "", true);
            events.add(event);
        }
        eventsRepository.saveAll(events);
    }
}
