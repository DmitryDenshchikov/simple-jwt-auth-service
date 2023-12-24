package denshchikov.dmitry.simpleeventmanagementservice;

import denshchikov.dmitry.simpleeventmanagementservice.config.AppJwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppJwtProperties.class)
public class SimpleEventManagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleEventManagementServiceApplication.class, args);
	}

}
