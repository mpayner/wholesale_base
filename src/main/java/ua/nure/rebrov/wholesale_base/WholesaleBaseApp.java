package ua.nure.rebrov.wholesale_base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import ua.nure.rebrov.wholesale_base.security.SessionEventListener;

@SpringBootApplication
public class WholesaleBaseApp {

	public static void main(String[] args) {
		SpringApplication.run(WholesaleBaseApp.class, args);

	}

}
