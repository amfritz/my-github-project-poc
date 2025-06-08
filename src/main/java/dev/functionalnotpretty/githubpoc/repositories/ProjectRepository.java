package dev.functionalnotpretty.githubpoc.repositories;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import dev.functionalnotpretty.githubpoc.entities.ProjectEntity;
import dev.functionalnotpretty.githubpoc.entities.ProjectEvent;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProjectRepository extends CosmosRepository<ProjectEntity, String> {
    List<ProjectEntity> findAllByUserId(String userId);
    boolean existsByProjectId(String projectId);

    ProjectEntity findByRepo_Name(String repoName);
}
