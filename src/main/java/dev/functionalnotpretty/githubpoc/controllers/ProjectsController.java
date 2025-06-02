package dev.functionalnotpretty.githubpoc.controllers;


import dev.functionalnotpretty.githubpoc.entities.ProjectEvents;
import dev.functionalnotpretty.githubpoc.entities.ProjectEntity;
import dev.functionalnotpretty.githubpoc.models.ProjectDto;
import dev.functionalnotpretty.githubpoc.repositories.ProjectEventsRepository;
import dev.functionalnotpretty.githubpoc.repositories.ProjectRepository;
import dev.functionalnotpretty.githubpoc.services.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/projects")
public class ProjectsController {
    private final static Logger log = LoggerFactory.getLogger(ProjectsController.class);
    private final ProjectRepository projectRepository;
    private final ProjectEventsRepository eventsRepository;
    private final ProjectService projectService;

    public ProjectsController(ProjectRepository projectRepository,
                              ProjectEventsRepository timelineMessagesRepository,
                              ProjectService projectService) {
        this.projectRepository = projectRepository;
        this.eventsRepository = timelineMessagesRepository;
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectDto> getProjects() {
        log.info("getProjects in projects api");

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
        log.info("createProject in projects controller");
        return this.projectService.createProject(watchedRepo, addCommits);
    }

    @GetMapping("{projectId}")
    public ProjectEntity getProjectById(@PathVariable("projectId") String projectId) {
        log.info("getProject({}) in projects controller", projectId);
        return this.projectRepository.getProjectById(projectId);
    }

    @GetMapping("{projectId}/events")
    public List<ProjectEvents> getProjectEvents(@PathVariable String projectId) {
        log.info("getProjectMessages in projects api {}", projectId);
        return this.eventsRepository.findByUserIdAndId("amfritz", projectId);
    }

    @PostMapping("{projectId}/events")
    public List<ProjectEvents> addProjectMessages(@PathVariable String projectId, @RequestBody List<ProjectEvents> timelineMessages) {
        log.info("add {} ProjectMessages in projects api for {}", timelineMessages.size(), projectId);
        // todo -- validate inputs
        var result = this.eventsRepository.saveAll(timelineMessages);
        return (List<ProjectEvents>) result;
    }
}
