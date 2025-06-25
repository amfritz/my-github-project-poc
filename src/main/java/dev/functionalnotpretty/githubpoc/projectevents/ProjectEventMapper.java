package dev.functionalnotpretty.githubpoc.projectevents;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProjectEventMapper {
    ProjectEventMapper INSTANCE = Mappers.getMapper(ProjectEventMapper.class);

    @Mapping(target = "isNewEvent", source = "newEvent")
    ProjectEventDto projectEventToProjectEventDto(ProjectEvent projectEvent);

    @Mapping(target = "userId",  ignore = true)
    @Mapping(target = "newEvent", source = "isNewEvent")
    ProjectEvent projectEventDtoToProjectEvent(ProjectEventDto projectEventDto);

    List<ProjectEvent> projectDtoListToProjectEventList(List<ProjectEventDto> events);
}
