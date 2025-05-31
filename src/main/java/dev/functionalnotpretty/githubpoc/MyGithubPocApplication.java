package dev.functionalnotpretty.githubpoc;

import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCosmosRepositories // Add this annotation
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
//					log.info("got repo: projectName: {}, id: {}, is private: {}", repo.getProjectName(), repo.getId(), repo.repoIsPrivate());
//					if (!repo.repoIsPrivate()) {
//						log.info("getting commit for repo: {}", repo.getProjectName());
//						var commits = githubRestClient.getUserRepoCommits("amfritz", repo.getProjectName());
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
