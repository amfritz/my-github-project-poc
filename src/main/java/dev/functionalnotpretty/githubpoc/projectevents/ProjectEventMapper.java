package dev.functionalnotpretty.githubpoc.projectevents;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProjectEventMapper {
    ProjectEventMapper INSTANCE = Mappers.getMapper(ProjectEventMapper.class);

    @Mapping(target = "isNewEvent", source = "newEvent")
    @Mapping(target = "branchName", source = "branch_name")
    ProjectEventDto projectEventToProjectEventDto(ProjectEvent projectEvent);

    @Mapping(target = "branch_name", source = "branchName")
    ProjectEvent projectEventDtoToProjectEvent(ProjectEventDto projectEventDto);
}
