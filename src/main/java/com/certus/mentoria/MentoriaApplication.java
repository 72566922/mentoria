package com.certus.mentoria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class MentoriaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MentoriaApplication.class, args);
	}

	@PostConstruct
	public void debugEnv() {
		System.out.println("CLIENT_ID ==> " + System.getenv("GOOGLE_CLIENT_ID"));
	}

}
