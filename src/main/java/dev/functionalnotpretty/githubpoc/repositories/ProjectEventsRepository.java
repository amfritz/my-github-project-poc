package dev.functionalnotpretty.githubpoc.repositories;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import dev.functionalnotpretty.githubpoc.entities.ProjectEvents;

import java.util.List;

public interface ProjectEventsRepository extends CosmosRepository<ProjectEvents, String> {
    @Query(value = "SELECT * from c where c.userId = @userId and c.id = @projectId and c.type='events'")
    List<ProjectEvents> findByUserIdAndId(String userId, String projectId);

}
