package com.event.ledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.event.ledger.controller",
		"com.event.ledger.service",
		"com.event.ledger.repository",
		"com.event.ledger.model",
		"com.event.ledger.dto",
		"com.event.ledger.converter"
})
public class EventLedgerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventLedgerApiApplication.class, args);
	}

}
