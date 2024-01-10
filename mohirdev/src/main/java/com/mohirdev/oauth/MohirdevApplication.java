package com.mohirdev.oauth;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class MohirdevApplication {

	public static void main(String[] args) {
		SpringApplication.run(MohirdevApplication.class, args);
	}
}

