package dev.functionalnotpretty.githubpoc.project;

import dev.functionalnotpretty.githubpoc.projectevents.ProjectEventDto;
import dev.functionalnotpretty.githubpoc.projectevents.ProjectEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);
    @Mapping(target = "name", source = "projectName")
    ProjectDto projectToProjectDto(ProjectEntity projectEntity);

    @Mapping(target = "projectName", source = "name")
    ProjectEntity projectDtoToProjectEntity(ProjectDto projectDto);

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "projectName", source = "name")
    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    ProjectEntity createProjectDtoToProjectEntity(CreateProjectDto create);

    List<ProjectEvent> projectDtoListToProjectEntityList(List<ProjectEventDto> events);

}
