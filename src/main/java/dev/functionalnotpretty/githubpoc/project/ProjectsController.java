package dev.functionalnotpretty.githubpoc.project;

import dev.functionalnotpretty.githubpoc.exceptions.BadRequestException;
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
        var result = this.projectService.getAllProjectsByUserId("amfritz");
        log.info("getProjects read {} projects", result.size());
        return result;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectDto createProject(@Valid @RequestBody CreateProjectDto project) {
        log.info("createProject with events in projects controller");
        return this.projectService.createProjectWithEvents(project);
        }

    @GetMapping("{projectId}")
    public ProjectDto getProjectById(@PathVariable("projectId") String projectId) {
        log.info("getProject({}) in projects controller", projectId);
        return this.projectService.getProject(projectId);
    }

    @PutMapping("{projectId}")
    public ProjectDto updateProject(@PathVariable("projectId") String projectId, @Valid @RequestBody ProjectDto project) {
        log.info("updateProject({}) in projects controller", projectId);
        if (!StringUtils.equalsIgnoreCase(projectId, project.id())) {
            log.info("updateProject({}) in projects controller project id does not match object", projectId);
            throw new BadRequestException("Project id mismatch");
        }
        return this.projectService.updateProject(project);
    }

    @DeleteMapping("{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProjectById(@PathVariable("projectId") String projectId) {
        log.info("deleteProject({}) in projects controller", projectId);
        this.projectService.deleteProject(projectId);
    }


}
