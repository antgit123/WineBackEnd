package com.wineapp.WineBackend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.wineapp.WineBackend.models.Wine;
import com.wineapp.WineBackend.repository.WineRepository;

@Service
public class WineService {

    private final WineRepository wineRepository;

    public WineService(WineRepository wineRepository) {
        this.wineRepository = wineRepository;
    }
    
    public List<Wine> list() {
    	List<Wine> wineList = new ArrayList<Wine>();
        for(Wine wine:wineRepository.findAll()) {
        	wineList.add(wine);
        };
        return wineList;
    }
    
    public Optional<Wine> findById(String lotCode) {
    	return wineRepository.findById(lotCode);
    }

    public Wine save(Wine wine) {
        return wineRepository.save(wine);
    }

    public void save(List<Wine> wines) {
    	wineRepository.saveAll(wines);
    }
}
