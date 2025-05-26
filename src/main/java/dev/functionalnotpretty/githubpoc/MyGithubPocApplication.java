package dev.functionalnotpretty.githubpoc;

import dev.functionalnotpretty.githubpoc.restservice.GithubRestClient;
import dev.functionalnotpretty.githubpoc.restservice.model.GithubRepo;
import dev.functionalnotpretty.githubpoc.restservice.model.GithubRepoCommit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MyGithubPocApplication {
	private final static Logger log = LoggerFactory.getLogger(MyGithubPocApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MyGithubPocApplication.class, args);
	}

//	@Bean
//	CommandLineRunner commandLineRunner(GithubRestClient githubRestClient) {
//		return args -> {
//			var result = githubRestClient.getUserRepos();
//			if (result!=null) {
//				for(GithubRepo repo : result) {
//					log.info("got repo: name: {}, id: {}, is private: {}", repo.getName(), repo.getId(), repo.isPrivate());
//					if (!repo.isPrivate()) {
//						log.info("getting commit for repo: {}", repo.getName());
//						var commits = githubRestClient.getUserRepoCommits("amfritz", repo.getName());
//						log.info("got {} commits ", commits.size());
//						for(GithubRepoCommit commit : commits) {
//							log.info("got commit message {}", commit.getCommit().get("message"));
//						}
//					}
//				}
//
//			}
//		};
//	}
}
