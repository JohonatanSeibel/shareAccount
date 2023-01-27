package br.com.shareaccount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableAutoConfiguration
@EnableWebMvc
@EnableFeignClients
public class ShareAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShareAccountApplication.class, args);
	}

}
