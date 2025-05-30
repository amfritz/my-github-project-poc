package dev.functionalnotpretty.githubpoc.repositories;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import dev.functionalnotpretty.githubpoc.entities.TimelineMessages;

import java.util.List;

public interface TimelineMessagesRepository extends CosmosRepository<TimelineMessages, String> {
    @Query(value = "SELECT * from c where c.userId = @userId and c.name = @name and c.type='messages'")
    List<TimelineMessages> findByUserIdAndName(String userId, String name);

}
