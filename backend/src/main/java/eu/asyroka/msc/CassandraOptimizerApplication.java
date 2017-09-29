package eu.asyroka.msc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class CassandraOptimizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CassandraOptimizerApplication.class, args);
	}

}
