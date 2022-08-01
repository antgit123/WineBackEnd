package com.wineapp.WineBackend.repository;

import org.springframework.data.repository.CrudRepository;

import com.wineapp.WineBackend.models.Wine;

public interface WineRepository extends CrudRepository<Wine, String> {

}