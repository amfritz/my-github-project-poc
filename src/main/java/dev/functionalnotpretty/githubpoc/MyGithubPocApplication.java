package dev.functionalnotpretty.githubpoc;

import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCosmosRepositories // Add this annotation
public class MyGithubPocApplication {
	//private final static Logger log = LoggerFactory.getLogger(MyGithubPocApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(MyGithubPocApplication.class, args);
	}
}
