package dev.functionalnotpretty.githubpoc.controllers;


import dev.functionalnotpretty.githubpoc.entities.TimelineMessages;
import dev.functionalnotpretty.githubpoc.entities.WatchedRepo;
import dev.functionalnotpretty.githubpoc.models.WatchedRepoDto;
import dev.functionalnotpretty.githubpoc.repositories.TimelineMessagesRepository;
import dev.functionalnotpretty.githubpoc.repositories.WatchedRepoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.sql.Time;
import java.util.List;

@RestController
@RequestMapping("api/v1/projects")
public class ProjectsController {
    private final static Logger log = LoggerFactory.getLogger(ProjectsController.class);
    private final WatchedRepoRepository cosmicWorksRepository;
    private final TimelineMessagesRepository timelineMessagesRepository;

    public ProjectsController(WatchedRepoRepository cosmicWorksRepository,
        TimelineMessagesRepository timelineMessagesRepository) {
        this.cosmicWorksRepository = cosmicWorksRepository;
        this.timelineMessagesRepository = timelineMessagesRepository;
    }

    @GetMapping
    public List<WatchedRepoDto> getWatchedRepos() {
        log.info("getWatchedRepos in projects api");

        var result = cosmicWorksRepository.getWatchedRepoByUserId("amfritz")
                .stream()
                .map(WatchedRepoDto::mapToDto)
                .toList();
        log.info("read {} watched repos", result.size());
        return result;
    }

    @PostMapping
    public WatchedRepo addWatchedRepo(@RequestBody WatchedRepoDto watchedRepo) {
        var item = new WatchedRepo(watchedRepo);
        log.info("addWatchedRepo in projects api {}", item);
        return cosmicWorksRepository.save(item);
    }

    @GetMapping("{name}/messages")
    public List<TimelineMessages> getProjectMessages(@PathVariable String name) {
        log.info("getProjectMessages in projects api {}", name);
        return this.timelineMessagesRepository.findByUserIdAndName("amfritz", name);
    }

    @PostMapping("{name}/messages")
    public List<TimelineMessages> addProjectMessages(@PathVariable String name, @RequestBody List<TimelineMessages> timelineMessages) {
        log.info("add {} ProjectMessages in projects api for {}", timelineMessages.size(), name);
        // todo -- validate inputs
        var result = this.timelineMessagesRepository.saveAll(timelineMessages);
        return (List<TimelineMessages>) result;
    }
}
