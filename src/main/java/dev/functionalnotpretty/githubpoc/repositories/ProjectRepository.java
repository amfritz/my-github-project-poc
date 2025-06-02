package dev.functionalnotpretty.githubpoc.repositories;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import dev.functionalnotpretty.githubpoc.entities.ProjectEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CosmosRepository<ProjectEntity, String> {
    @Query(value = "SELECT * from c where c.userId = @userId and c.type='project'")
    List<ProjectEntity> getProjectsByUserId(String userId);

    @Query(value = "SELECT * from c where c.id = @id and c.type='project'")
    ProjectEntity getProjectById(String id);
}
