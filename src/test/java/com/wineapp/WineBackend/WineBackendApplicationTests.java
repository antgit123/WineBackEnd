package com.wineapp.WineBackend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.wineapp.WineBackend.controller.WineController;

@SpringBootTest
class WineBackendApplicationTests {
	
	@Autowired
	private WineController wineController;

	@Test
	void contextLoads() {
		assertThat(wineController).isNotNull();
	}

}
