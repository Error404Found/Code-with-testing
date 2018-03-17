package com.abhinendra;

import java.io.IOException;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import com.abhinendra.services.PersonService;
import com.abhinendra.services.SanctionService;
import com.abhinendra.services.TransactionService;

@SpringBootApplication
@EntityScan(basePackages = { "com.abhinendra.domain" })
@ComponentScan(basePackages = "com.abhinendra")
public class ServerApplication {

	@Autowired
	TransactionService transactionservice;
	
	@Autowired
	SanctionService sanctionService;
	
	@Autowired
	PersonService personService;

	@PostConstruct
    public void combine()
	{
		sanctionService.readSanctionList("sanctionList.txt");
		personService.createUser("CustomerList.txt");
		//transactionservice.readFile("sample1.txt");
	    try {
			transactionservice.Polling();
		} catch (IOException | InterruptedException e) {}
    }
	
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}
