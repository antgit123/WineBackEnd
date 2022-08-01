package com.wineapp.WineBackend;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.wineapp.WineBackend.controller.WineController;
import com.wineapp.WineBackend.models.GrapeComponent;
import com.wineapp.WineBackend.models.Wine;
import com.wineapp.WineBackend.repository.WineRepository;
import com.wineapp.WineBackend.service.WineService;

@WebMvcTest(WineController.class)
public class WebLayerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private WineService service;

	@MockBean
	private WineRepository wineRepository;
	private Wine wine;

	@BeforeEach
	public void setup() {
		wine = new Wine("1", 1000, "wine1", "TK1", "VIC", "Yarra Valley", null);
		GrapeComponent c1 = new GrapeComponent(1, wine, 5, 2011, "Pinot Noir", "Mornington");
		GrapeComponent c2 = new GrapeComponent(2, wine, 80, 2011, "Chardonnay", "Yarra Valley");
		GrapeComponent c3 = new GrapeComponent(3, wine, 5, 2010, "Pinot Noir", "Macedon");
		GrapeComponent c4 = new GrapeComponent(4, wine, 10, 2010, "Chardonnay", "Macedon");
		List<GrapeComponent> componentList = new ArrayList<GrapeComponent>();
		componentList.add(c1);
		componentList.add(c2);
		componentList.add(c3);
		componentList.add(c4);
		wine.setComponents(componentList);
		wineRepository.save(wine);
	}

	@Test
	public void getValidWineDataTest() throws Exception {
		when(service.findById("1")).thenReturn(Optional.of(wine));
		this.mockMvc.perform(get("/wines/{id}", "1")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("wine1")))
				.andExpect(jsonPath("$.lotCode").value(wine.getLotCode()));
	}

	@Test
	public void getInvalidWineDataTest() throws Exception {
		when(service.findById("2")).thenReturn(Optional.empty());
		this.mockMvc.perform(get("/wines/{id}", "2")).andDo(print()).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Wine details does not exist"));
	}

	@Test
	public void getWineList() throws Exception {
		List<Wine> wineList = new ArrayList<>();
		wineList.add(wine);
		when(service.list()).thenReturn(wineList);
		this.mockMvc.perform(get("/wines/list")).andDo(print()).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(wineList.size()));
	}

	@Test
	public void verifyWineCompositionDataWithValidKey() throws Exception {
		String key = "year";
		String lotCode = "1";
		when(service.findById("1")).thenReturn(Optional.of(wine));
		this.mockMvc.perform(get("/wines/api/breakdown/{key}/{lotCode}", key, lotCode)).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.breakDownType").value(key))
				.andExpect(jsonPath("$.breakdown.size()").value(2));
	}
	
	@Test
	public void verifyWineCompositionDataWithValidKeyCase2() throws Exception {
		String key = "year-variety";
		String lotCode = "1";
		when(service.findById("1")).thenReturn(Optional.of(wine));
		this.mockMvc.perform(get("/wines/api/breakdown/{key}/{lotCode}", key, lotCode)).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.breakDownType").value(key));
	}
	
	@Test
	public void verifyWineCompositionDataWithInvalidValidKey() throws Exception {
		String key = "year-variety2";
		String lotCode = "1";
		when(service.findById("1")).thenReturn(Optional.of(wine));
		this.mockMvc.perform(get("/wines/api/breakdown/{key}/{lotCode}", key, lotCode)).andDo(print())
				.andExpect(status().is5xxServerError())
				.andExpect(jsonPath("$.message").value("Internal Server error while handling the request"));
	}
	
	@Test
	public void verifyWineCompositionDataWithInvalidWineLotCode() throws Exception {
		String key = "year-variety";
		String lotCode = "10";
		when(service.findById("10")).thenReturn(Optional.empty());
		this.mockMvc.perform(get("/wines/api/breakdown/{key}/{lotCode}", key, lotCode)).andDo(print())
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("Wine details does not exist"));
	}
}