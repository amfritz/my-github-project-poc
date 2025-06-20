package dev.functionalnotpretty.githubpoc.controllers;

import dev.functionalnotpretty.githubpoc.exceptions.BadRequestException;
import dev.functionalnotpretty.githubpoc.models.*;
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
                .map(ProjectMapper.INSTANCE::projectToProjectDto)
                .toList();
        log.info("read {} projects", result.size());
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto createProject(@Valid @RequestBody CreateProjectDto project) {
        log.info("createProject with events in projects controller");
        return ProjectMapper.INSTANCE.projectToProjectDto(this.projectService.createProjectWithEvents(project));
        }

    @GetMapping("{projectId}")
    public ProjectDto getProjectById(@PathVariable("projectId") String projectId) {
        log.info("getProject({}) in projects controller", projectId);
        return ProjectMapper.INSTANCE.projectToProjectDto(this.projectService.getProject(projectId));
    }

    @PutMapping("{projectId}")
    public ProjectDto updateProject(@PathVariable("projectId") String projectId, @Valid @RequestBody ProjectDto project) {
        log.info("updateProject({}) in projects controller", projectId);
        if (!StringUtils.equalsIgnoreCase(projectId, project.id())) {
            log.info("updateProject({}) in projects controller project id does not match object", projectId);
            throw new BadRequestException("Project id mismatch");
        }
        return ProjectMapper.INSTANCE.projectToProjectDto(this.projectService.updateProject(ProjectMapper.INSTANCE.projectDtoToProjectEntity(project)));
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
                .map(ProjectEventMapper.INSTANCE::projectEventToProjectEventDto)
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

        return ProjectEventMapper.INSTANCE.projectEventToProjectEventDto(this.projectService.createProjectEvent(ProjectEventMapper.INSTANCE.projectEventDtoToProjectEvent(projectEvent)));
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
        return ProjectEventMapper.INSTANCE.projectEventToProjectEventDto(this.projectService.updateProjectEvent(ProjectEventMapper.INSTANCE.projectEventDtoToProjectEvent(projectEventDto)));
    }

    @PutMapping("{projectId}/events")
    public List<ProjectEventDto> bulkUpdateProjectMessages(@Valid @PathVariable String projectId, @RequestBody List<ProjectEventDto> events) {
        log.info("add {} ProjectMessages in projects api for {}", events.size(), projectId);
        var result = this.projectService.updateProjectEvents(projectId, events.stream()
                .map(ProjectEventMapper.INSTANCE::projectEventDtoToProjectEvent)
                .toList()
            );
        return result.stream()
                .map(ProjectEventMapper.INSTANCE::projectEventToProjectEventDto)
                .toList();
    }

    @DeleteMapping("{projectId}/events/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectMessages(@PathVariable String projectId, @PathVariable String eventId) {
        log.info("deleteProjectMessages in projects api for {}", eventId);
        this.projectService.deleteProjectEvent(projectId, eventId);
    }
}
