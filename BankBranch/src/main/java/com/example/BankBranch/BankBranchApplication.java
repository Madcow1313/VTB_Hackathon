package com.example.BankBranch;

import com.example.BankBranch.repository.SalePointRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BankBranchApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankBranchApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(SalePointRepository salePointRepository) {
		return args -> {

		};
	}

}
