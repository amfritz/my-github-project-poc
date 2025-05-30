package dev.functionalnotpretty.githubpoc.repositories;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import dev.functionalnotpretty.githubpoc.entities.WatchedRepo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchedRepoRepository extends CosmosRepository<WatchedRepo, String> {
    @Query(value = "SELECT * from c where c.userId = @userId and c.type='watchedRepo'")
    List<WatchedRepo> getWatchedRepoByUserId(String userId);
}
