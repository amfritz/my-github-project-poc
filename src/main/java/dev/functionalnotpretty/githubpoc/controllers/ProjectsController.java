package dev.functionalnotpretty.githubpoc.controllers;


import dev.functionalnotpretty.githubpoc.models.ProjectDto;
import dev.functionalnotpretty.githubpoc.models.ProjectEventDto;
import dev.functionalnotpretty.githubpoc.repositories.ProjectEventsRepository;
import dev.functionalnotpretty.githubpoc.repositories.ProjectRepository;
import dev.functionalnotpretty.githubpoc.services.ProjectService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("api/projects")
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

        var result = this.projectRepository.findAllByUserId("amfritz")
                .stream()
                .map(ProjectDto::toDto)
                .toList();
        log.info("read {} projects", result.size());
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto createProject(@Valid @RequestBody ProjectDto project, @RequestParam(value = "add-commits", required = false) boolean addCommits) {
        // todo create a service to do business logic
        log.info("createProject in projects controller");
        return ProjectDto.toDto(this.projectService.createProject(project, addCommits));
    }

    // todo -- put and delete projects

    @GetMapping("{projectId}")
    public ProjectDto getProjectById(@PathVariable("projectId") String projectId) {
        log.info("getProject({}) in projects controller", projectId);
        return ProjectDto.toDto(this.projectService.getProject(projectId));
    }

    @GetMapping("{projectId}/events")
    public List<ProjectEventDto> getProjectEvents(@PathVariable String projectId) {
        log.info("getProjectMessages in projects api {}", projectId);
        return this.eventsRepository.findAllByProjectId(projectId)
                .stream()
                .map(ProjectEventDto::toDto)
                .toList();
    }

    // todo -- put and delete events

    @PostMapping("{projectId}/events")
    public List<ProjectEventDto> addProjectMessages(@Valid @PathVariable String projectId, @RequestBody List<ProjectEventDto> events) {
        log.info("add {} ProjectMessages in projects api for {}", events.size(), projectId);
        var result = this.eventsRepository.saveAll(events.stream()
                .map(ProjectEventDto::mapDto)
                .toList()
            );
        return StreamSupport.stream(result.spliterator(), false)
                .map(ProjectEventDto::toDto)
                .toList();
    }
}
