package com.wineapp.WineBackend.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
public class Wine {

	@Id
    @JsonProperty("lotCode")
    private String lotCode;
    @JsonProperty("volume")
    private double volume;
    @JsonProperty("description")
    private String description;
	@JsonProperty("tankCode")
    private String tankCode;
    @JsonProperty("productState")
    private String productState;
    @JsonProperty("ownerName")
    private String ownerName;
    @JsonProperty("components")
    @JsonManagedReference
    @OneToMany(targetEntity=GrapeComponent.class,
    		mappedBy="wine", 
    		cascade = CascadeType.ALL,
    		fetch = FetchType.LAZY)
    private List<GrapeComponent> components;

    public Wine() {}
}