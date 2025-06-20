package dev.functionalnotpretty.githubpoc.projectevents;

import com.azure.spring.data.cosmos.repository.CosmosRepository;

import java.util.List;

public interface ProjectEventsRepository extends CosmosRepository<ProjectEvent, String> {
    List<ProjectEvent>  findAllByProjectId(String projectId);

    boolean existsByProjectIdAndId(String projectId, String id);
    void deleteByProjectIdAndId(String projectId, String id);
    void deleteByProjectId(String projectId);
}
