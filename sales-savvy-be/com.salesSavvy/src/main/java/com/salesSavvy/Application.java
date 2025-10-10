package com.salesSavvy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		System.out.println("✅ DB_USERNAME: " + System.getenv("DB_USERNAME"));
        System.out.println("✅ Razorpay Key: " + System.getenv("RAZORPAY_KEY_ID"));
		SpringApplication.run(Application.class, args);
	}

}
