package dev.functionalnotpretty.githubpoc.controllers;

import dev.functionalnotpretty.githubpoc.entities.ProjectEvent;
import dev.functionalnotpretty.githubpoc.exceptions.BadRequestException;
import dev.functionalnotpretty.githubpoc.models.ProjectDto;
import dev.functionalnotpretty.githubpoc.models.ProjectEventDto;
import dev.functionalnotpretty.githubpoc.services.ProjectService;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/projects")
public class ProjectsController {
    private final static Logger log = LoggerFactory.getLogger(ProjectsController.class);
    private final ProjectService projectService;

    public ProjectsController(
                              ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<ProjectDto> getProjects() {
        log.info("getProjects in projects api");

        var result = this.projectService.getAllProjectsByUserId("amfritz")
                .stream()
                .map(ProjectDto::toDto)
                .toList();
        log.info("read {} projects", result.size());
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto createProject(@Valid @RequestBody ProjectDto project, @RequestParam(value = "add-commits", required = false) boolean addCommits) {
        log.info("createProject in projects controller");
        return ProjectDto.toDto(this.projectService.createProject(ProjectDto.toEntity(project), addCommits));
    }

    @GetMapping("{projectId}")
    public ProjectDto getProjectById(@PathVariable("projectId") String projectId) {
        log.info("getProject({}) in projects controller", projectId);
        return ProjectDto.toDto(this.projectService.getProject(projectId));
    }

    @PutMapping("{projectId}")
    public ProjectDto updateProject(@PathVariable("projectId") String projectId, @Valid @RequestBody ProjectDto project) {
        log.info("updateProject({}) in projects controller", projectId);
        if (!StringUtils.equalsIgnoreCase(projectId, project.id())) {
            log.info("updateProject({}) in projects controller project id does not match object", projectId);
            throw new BadRequestException("Project id mismatch");
        }
        return ProjectDto.toDto(this.projectService.updateProject(ProjectDto.toEntity(project)));
    }

    @DeleteMapping("{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectById(@PathVariable("projectId") String projectId) {
        log.info("deleteProject({}) in projects controller", projectId);
        this.projectService.deleteProject(projectId);
    }

    // begin the project event methods

    @GetMapping("{projectId}/events")
    public List<ProjectEventDto> getProjectEvents(@PathVariable String projectId) {
        log.info("getProjectMessages in projects api {}", projectId);
        return this.projectService.getAllProjectEvents(projectId)
                .stream()
                .map(ProjectEventDto::toDto)
                .toList();
    }

    @PostMapping("{projectId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectEventDto createProjectEvent(@PathVariable String projectId, @Valid @RequestBody ProjectEventDto projectEvent) {
        log.info("createProjectEvent({}) in projects controller", projectId);
        if (!StringUtils.equalsIgnoreCase(projectId, projectEvent.projectId())) {
            log.info("createProjectEvent({}) in projects controller project id does not match requested object", projectId);
            throw new BadRequestException("Project id mismatch");
        }

        return ProjectEventDto.toDto(this.projectService.createProjectEvent(ProjectEventDto.toEvent(projectEvent)));
    }

    @PutMapping("{projectId}/events/{eventId}")
    public ProjectEventDto updateProjectEvent(@PathVariable String projectId, @PathVariable String eventId, @Valid @RequestBody ProjectEventDto projectEventDto) {
        log.info("updateProjectEvent in projects controller");
        if (!StringUtils.equalsIgnoreCase(eventId, projectEventDto.id())||
                !StringUtils.equalsIgnoreCase(projectId, projectEventDto.projectId())
            ) {
            log.info("updateProjectEvent in projects controller project/event id does not match input object");
            throw new BadRequestException("id mismatch in request object");
        }
        return ProjectEventDto.toDto(this.projectService.updateProjectEvent(new ProjectEvent(projectEventDto)));
    }

    @PutMapping("{projectId}/events")
    public List<ProjectEventDto> bulkUpdateProjectMessages(@Valid @PathVariable String projectId, @RequestBody List<ProjectEventDto> events) {
        log.info("add {} ProjectMessages in projects api for {}", events.size(), projectId);
        var result = this.projectService.updateProjectEvents(projectId, events.stream()
                .map(ProjectEventDto::toEvent)
                .toList()
            );
        return result.stream()
                .map(ProjectEventDto::toDto)
                .toList();
    }

    @DeleteMapping("{projectId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectMessages(@PathVariable String projectId, @PathVariable String eventId) {
        log.info("deleteProjectMessages in projects api for {}", eventId);
        this.projectService.deleteProjectEvent(projectId, eventId);
    }
}
