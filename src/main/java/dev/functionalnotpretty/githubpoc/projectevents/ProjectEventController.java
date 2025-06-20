package dev.functionalnotpretty.githubpoc.projectevents;

import dev.functionalnotpretty.githubpoc.exceptions.BadRequestException;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/projects/{projectId}/events")
public class ProjectEventController {
    private final static Logger log = LoggerFactory.getLogger(ProjectEventController.class);
    private final ProjectEventService projectService;

    public ProjectEventController(ProjectEventService projectService) {
        this.projectService = projectService;
    }
    // begin the project event methods

    @GetMapping
    public List<ProjectEventDto> getProjectEvents(@PathVariable String projectId) {
        log.info("getProjectMessages in projects api {}", projectId);
        return this.projectService.getAllProjectEvents(projectId);
     }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectEventDto createProjectEvent(@PathVariable String projectId, @Valid @RequestBody ProjectEventDto projectEvent) {
        log.info("createProjectEvent({}) in projects controller", projectId);
        if (!StringUtils.equalsIgnoreCase(projectId, projectEvent.projectId())) {
            log.info("createProjectEvent({}) in projects controller project id does not match requested object", projectId);
            throw new BadRequestException("Project id mismatch");
        }

        return this.projectService.createProjectEvent(projectEvent);
    }

    @PutMapping("{eventId}")
    public ProjectEventDto updateProjectEvent(@PathVariable String projectId, @PathVariable String eventId, @Valid @RequestBody ProjectEventDto projectEventDto) {
        log.info("updateProjectEvent in projects controller");
        if (!StringUtils.equalsIgnoreCase(eventId, projectEventDto.id())||
                !StringUtils.equalsIgnoreCase(projectId, projectEventDto.projectId())
        ) {
            log.info("updateProjectEvent in projects controller project/event id does not match input object");
            throw new BadRequestException("id mismatch in request object");
        }
        return this.projectService.updateProjectEvent(projectEventDto);
    }

//    @PutMapping
//    public List<ProjectEventDto> bulkUpdateProjectMessages(@Valid @PathVariable String projectId, @RequestBody List<ProjectEventDto> events) {
//        log.info("add {} ProjectMessages in projects api for {}", events.size(), projectId);
//        var result = this.projectService.updateProjectEvents(projectId, events.stream()
//                .map(ProjectEventMapper.INSTANCE::projectEventDtoToProjectEvent)
//                .toList()
//        );
//        return result.stream()
//                .map(ProjectEventMapper.INSTANCE::projectEventToProjectEventDto)
//                .toList();
//    }

    @DeleteMapping("{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectMessages(@PathVariable String projectId, @PathVariable String eventId) {
        log.info("deleteProjectMessages in projects api for {}", eventId);
        this.projectService.deleteProjectEvent(projectId, eventId);
    }
}
