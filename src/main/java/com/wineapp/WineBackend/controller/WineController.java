package com.wineapp.WineBackend.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wineapp.WineBackend.models.Wine;
import com.wineapp.WineBackend.models.WineResponse;
import com.wineapp.WineBackend.ErrorHandling.InternalServerErrorException;
import com.wineapp.WineBackend.ErrorHandling.ResourceNotFoundException;
import com.wineapp.WineBackend.models.Breakdown;
import com.wineapp.WineBackend.models.GrapeComponent;
import com.wineapp.WineBackend.service.WineService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/wines")
public class WineController {

	private final WineService wineService;

	public WineController(WineService wineService) {
		this.wineService = wineService;
	}

	@GetMapping("/list")
	public Iterable<Wine> list() {
		return wineService.list();
	}

	@GetMapping("/{lotCode}")
	public ResponseEntity<Wine> getWineById(@PathVariable(value = "lotCode") String lotCode)
			throws ResourceNotFoundException {
	
			Optional<Wine> wine = wineService.findById(lotCode);
			if (!wine.isPresent()) {
				throw new ResourceNotFoundException("Wine details does not exist");
			}
			return new ResponseEntity<Wine>(wine.get(), HttpStatus.OK);		
	}

	// Dynamic grouping method API
	@GetMapping(value = "/api/breakdown/{key}/{lotCode}", produces = "application/json")
	public ResponseEntity<WineResponse> getBreakdownByKey(@PathVariable(value = "key") String key,
			@PathVariable(value = "lotCode") String lotCode)
			throws InternalServerErrorException, ResourceNotFoundException {

		Optional<Wine> wine = wineService.findById(lotCode);
		if (!wine.isPresent()) {
			throw new ResourceNotFoundException("Wine details does not exist");
		}
		try {
			List<String> groupByKeys = Arrays.asList(key.split("-"));
			Map<String, Function<GrapeComponent, Object>> extractors = new HashMap<>();
			extractors.put("year", GrapeComponent::getYear);
			extractors.put("region", GrapeComponent::getRegion);
			extractors.put("variety", GrapeComponent::getVariety);
			Function<GrapeComponent, List<Object>> classifier = component -> groupByKeys.stream()
					.map(attr -> extractors.get(attr).apply(component)).collect(Collectors.toList());

			Map<List<Object>, Double> yearComposition = wine.get().getComponents().stream().collect(
					Collectors.groupingBy(classifier, Collectors.summingDouble(GrapeComponent::getPercentage)));

			Map<String, Double> yearCompositionMap = yearComposition.entrySet().stream()
					.collect(Collectors.toMap(e -> getKeyStringRepresentation(e.getKey()), Map.Entry::getValue));

			return new ResponseEntity<WineResponse>(getResponse(key, yearCompositionMap), HttpStatus.OK);

		} catch (Exception e) {
			throw new InternalServerErrorException("Internal Server error while handling the request");
		}
	}

	public WineResponse getResponse(String breakDownKey, Map<String, Double> yearComposition) {
		List<Breakdown> breakdownCompositionList = new ArrayList<>();
		for (Map.Entry<String, Double> entry : yearComposition.entrySet()) {
			Breakdown breakdown = new Breakdown(entry.getValue(), entry.getKey());
			breakdownCompositionList.add(breakdown);
		}
		breakdownCompositionList.sort(Comparator.comparing(Breakdown::getPercentage).reversed());
		return new WineResponse(breakDownKey, breakdownCompositionList);
	}

	public String getKeyStringRepresentation(List<Object> keys) {

		int index = 0;
		String key_str = "";
		for (Object key : keys) {
			key_str += key.toString();
			if (index != keys.size() - 1) {
				key_str += "-";
			}
			index++;
		}
		return key_str;
	}

}