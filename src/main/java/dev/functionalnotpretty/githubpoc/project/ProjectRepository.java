package dev.functionalnotpretty.githubpoc.project;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProjectRepository extends CosmosRepository<ProjectEntity, String> {
    List<ProjectEntity> findAllByUserId(String userId);
    boolean existsByProjectId(String projectId);

    ProjectEntity findByRepo_Name(String repoName);

    boolean existsByRepo_Name(String repoName);
    ProjectEntity findByIdAndProjectId(String id, String projectId);

    void deleteByIdAndProjectId(String id, String projectId);
}
