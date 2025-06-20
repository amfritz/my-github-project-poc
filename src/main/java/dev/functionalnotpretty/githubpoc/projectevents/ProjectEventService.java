package dev.functionalnotpretty.githubpoc.projectevents;

import dev.functionalnotpretty.githubpoc.exceptions.ResourceNotFoundException;
import dev.functionalnotpretty.githubpoc.project.ProjectRepository;
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

    public List<ProjectEventDto> getAllProjectEvents(String projectId) {
        return this.eventsRepository
                .findAllByProjectId(projectId)
                .stream()
                .map(ProjectEventMapper.INSTANCE::projectEventToProjectEventDto)
                .toList();
    }

    public ProjectEventDto updateProjectEvent(ProjectEventDto projectEventDto) {
        // todo -- read the parent project and determine if it is archived
        var projectEvent = ProjectEventMapper.INSTANCE.projectEventDtoToProjectEvent(projectEventDto);

        if (!projectRepository.existsByProjectId(projectEvent.getProjectId())) {
            log.info( "project event {} is not found", projectEvent.getId());
            throw new ResourceNotFoundException("Project event not found");
        }
        projectEvent.setUserId("amfritz");      // refactor this from user session when not poc
        projectEvent.setUpdatedDt(Instant.now().toString());
        return ProjectEventMapper.INSTANCE.projectEventToProjectEventDto(this.eventsRepository.save(projectEvent));
    }

    public ProjectEventDto createProjectEvent(ProjectEventDto projectEventDto) {
        // todo -- check parent status, if archived don't allow create
        // ensure the db will generate an id
        var projectEvent = ProjectEventMapper.INSTANCE.projectEventDtoToProjectEvent(projectEventDto);
        projectEvent.setId(null);
        projectEvent.setUserId("amfritz");      // refactor this from user session when not poc
        var created = Instant.now().toString();
        projectEvent.setCreatedDt(created);
        projectEvent.setUpdatedDt(created);
        return ProjectEventMapper.INSTANCE.projectEventToProjectEventDto(this.eventsRepository.save(projectEvent));
    }

    public void createProjectEventList(String projectId, List<ProjectEventDto> eventsDto) {
        // validate that all objects are for the paths
        var events = ProjectEventMapper.INSTANCE.projectDtoListToProjectEventList(eventsDto);
        for(ProjectEvent event : events) {
            event.setProjectId(projectId);
            event.setUserId("amfritz");
            var created = Instant.now().toString();
            event.setCreatedDt(created);
            event.setUpdatedDt(created);
        }

        this.eventsRepository.saveAll(events);
        //ProjectEventMapper.INSTANCE.projectEntityListToProjectEntityDtoList((List<ProjectEvent>) result);
    }

    // event Ids, the object id, is unique per container so don't need the project is
    public void deleteProjectEvent(String projectId, String eventId) {
        if (!eventsRepository.existsByProjectIdAndId(projectId, eventId)) {
            log.info( "specified project event is not found");
            throw new ResourceNotFoundException("Project event not found");
        }

        this.eventsRepository.deleteByProjectIdAndId(projectId, eventId);
    }

    public void deleteAllProjectEvents(String projectId) {
        this.eventsRepository.deleteByProjectId(projectId);
    }

}
