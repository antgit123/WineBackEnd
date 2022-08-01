package com.wineapp.WineBackend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.wineapp.WineBackend.models.GrapeComponent;
import com.wineapp.WineBackend.repository.GrapeComponentRepository;

@Service
public class GrapeComponentService {

    private final GrapeComponentRepository grapeRepository;

    public GrapeComponentService(GrapeComponentRepository grapeRepository) {
        this.grapeRepository = grapeRepository;
    }
    
    public List<GrapeComponent> list() {
    	List<GrapeComponent> componentList = new ArrayList<GrapeComponent>();
        for(GrapeComponent component:grapeRepository.findAll()) {
        	componentList.add(component);
        };
        return componentList;
    }
 
}