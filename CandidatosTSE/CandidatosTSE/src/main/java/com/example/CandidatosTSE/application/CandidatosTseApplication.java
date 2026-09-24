package com.example.CandidatosTSE.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// A classe está no subpacote "application", então é preciso mandar o Spring
// procurar controller/service a partir do pacote raiz do projeto.
@SpringBootApplication(scanBasePackages = "com.example.CandidatosTSE")
public class CandidatosTseApplication {

	public static void main(String[] args) {
		SpringApplication.run(CandidatosTseApplication.class, args);
	}

}
