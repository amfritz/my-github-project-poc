package dev.functionalnotpretty.githubpoc.projectevents;

import dev.functionalnotpretty.githubpoc.exceptions.BadRequestException;
import dev.functionalnotpretty.githubpoc.exceptions.ResourceNotFoundException;
import dev.functionalnotpretty.githubpoc.project.ProjectRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ProjectEventService {
    private final static Logger log = LoggerFactory.getLogger(ProjectEventService.class);
    private final ProjectRepository projectRepository;
    private final ProjectEventsRepository eventsRepository;


    public ProjectEventService(ProjectRepository projectRepository, ProjectEventsRepository eventsRepository) {
        this.projectRepository = projectRepository;
        this.eventsRepository = eventsRepository;
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
