package dev.functionalnotpretty.githubpoc.controllers;


import com.azure.core.annotation.QueryParam;
import dev.functionalnotpretty.githubpoc.entities.ProjectEvents;
import dev.functionalnotpretty.githubpoc.entities.ProjectEntity;
import dev.functionalnotpretty.githubpoc.models.ProjectDto;
import dev.functionalnotpretty.githubpoc.repositories.ProjectEventsRepository;
import dev.functionalnotpretty.githubpoc.repositories.ProjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/projects")
public class ProjectsController {
    private final static Logger log = LoggerFactory.getLogger(ProjectsController.class);
    private final ProjectRepository projectRepository;
    private final ProjectEventsRepository eventsRepository;

    public ProjectsController(ProjectRepository projectRepository,
                              ProjectEventsRepository timelineMessagesRepository) {
        this.projectRepository = projectRepository;
        this.eventsRepository = timelineMessagesRepository;
    }

    @GetMapping
    public List<ProjectDto> getWatchedRepos() {
        log.info("getWatchedRepos in projects api");

        var result = projectRepository.getProjectsByUserId("amfritz")
                .stream()
                .map(ProjectDto::mapToDto)
                .toList();
        log.info("read {} watched repos", result.size());
        return result;
    }

    @PostMapping
    public ProjectEntity createProject(@RequestBody ProjectDto watchedRepo, @RequestParam(value = "add-commits", required = false) boolean addCommits) {
        // todo create a service to do business logic
        var item = new ProjectEntity(watchedRepo);
        var created = Instant.now().toString();
        item.setCreatedAt(created);
        item.setUpdatedAt(created);
        log.info("addWatchedRepo in projects api {}", item);
        return projectRepository.save(item);
    }

    @GetMapping("{projectId}/events")
    public List<ProjectEvents> getProjectEvents(@PathVariable String projectId) {
        log.info("getProjectMessages in projects api {}", projectId);
        return this.eventsRepository.findByUserIdAndId("amfritz", projectId);
    }

    @PostMapping("{name}/events")
    public List<ProjectEvents> addProjectMessages(@PathVariable String name, @RequestBody List<ProjectEvents> timelineMessages) {
        log.info("add {} ProjectMessages in projects api for {}", timelineMessages.size(), name);
        // todo -- validate inputs
        var result = this.eventsRepository.saveAll(timelineMessages);
        return (List<ProjectEvents>) result;
    }
}
