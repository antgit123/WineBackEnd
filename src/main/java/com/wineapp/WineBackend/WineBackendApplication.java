package com.wineapp.WineBackend;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wineapp.WineBackend.models.Wine;
import com.wineapp.WineBackend.service.WineService;

@SpringBootApplication
public class WineBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(WineBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner initDb(WineService wineService) {
		return args -> {
			ObjectMapper mapper = new ObjectMapper();
			TypeReference<Wine> typeReference = new TypeReference<Wine>(){};
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
		    URL url = loader.getResource("data");
		    String path = url.getPath();
		    File[] files = new File(path).listFiles();
		    for(File file : files) {
				InputStream inputStream = TypeReference.class.getResourceAsStream("/data/" + file.getName());
				try {
					Wine wine = mapper.readValue(inputStream, typeReference);
					
					wineService.save(wine);
					System.out.println("Wine item" +file.getName() + "saved!");
				} catch (IOException e){
					System.out.println("Unable to save wine items: " + e.getMessage());
				}
		    }
		};
	}
}
