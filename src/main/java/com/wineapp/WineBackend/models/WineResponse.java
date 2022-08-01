package com.wineapp.WineBackend.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WineResponse {

	private String breakDownType;
	private List<Breakdown> breakdown;
	
}
