package com.wineapp.WineBackend;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.BDDMockito.given;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wineapp.WineBackend.models.GrapeComponent;
import com.wineapp.WineBackend.models.Wine;
import com.wineapp.WineBackend.repository.GrapeComponentRepository;
import com.wineapp.WineBackend.repository.WineRepository;
import com.wineapp.WineBackend.service.GrapeComponentService;
import com.wineapp.WineBackend.service.WineService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

    @Mock
    private WineRepository wineRepository;
    
    @Mock
    private GrapeComponentRepository componentRepository;

    @InjectMocks
    private WineService wineService;
    
    @InjectMocks
    private GrapeComponentService componentService;

    private Wine wine;
    private GrapeComponent c1;
    private GrapeComponent c2;
    private GrapeComponent c3;
    private GrapeComponent c4;

    @BeforeEach
    public void setup(){
    	wine = new Wine("1", 1000, "wine1", "TK1", "VIC", "Yarra Valley", null);
		c1 = new GrapeComponent(1, wine, 5, 2011, "Pinot Noir", "Mornington");
		c2 = new GrapeComponent(2, wine, 80, 2011, "Chardonnay", "Yarra Valley");
		c3 = new GrapeComponent(3, wine, 5, 2010, "Pinot Noir", "Macedon");
		c4 = new GrapeComponent(4, wine, 10, 2010, "Chardonnay", "Macedon");
		List<GrapeComponent> componentList = new ArrayList<GrapeComponent>();
		componentList.add(c1);
		componentList.add(c2);
		componentList.add(c3);
		componentList.add(c4);
		wine.setComponents(componentList);
    }
    
    @DisplayName("JUnit test for save Wine method")
    @Test
    public void verifySaveWine(){
        given(wineRepository.save(wine)).willReturn(wine);
        Wine savedWine = wineService.save(wine);
        assertThat(savedWine).isNotNull();
    }
    
    @DisplayName("JUnit test for get wine list method")
    @Test
    public void verifyGetWineList(){
    	List<Wine> testWineList = new ArrayList<Wine>();
    	testWineList.add(wine);
        given(wineRepository.findAll()).willReturn(testWineList);
        List<Wine> wineList = wineService.list();
        assertThat(wineList).isNotNull();
        assertThat(wineList.size()).isEqualTo(1);
    }
    
    
    @DisplayName("JUnit test for get wine method")
    @Test
    public void verifyGetWine(){
        given(wineRepository.findById("1")).willReturn(Optional.of(wine));
        Optional<Wine> wineRetrieved = wineService.findById("1");
        assertThat(wineRetrieved.get()).isNotNull();
        assertThat(wineRetrieved.get().getLotCode()).isEqualTo("1");
    }
    
    @DisplayName("JUnit test for save all wines method")
    @Test
    public void verifySaveAllWines(){
    	List<Wine> testWineList = new ArrayList<Wine>();
    	testWineList.add(wine);
        given(wineRepository.saveAll(testWineList)).willReturn(testWineList);
        wineService.save(testWineList);
    }
    
    @DisplayName("JUnit test for get all grape components method")
    @Test
    public void verifyGetAllGrapeComponents(){
    	List<GrapeComponent> componentList = new ArrayList<GrapeComponent>();
    	componentList.add(c1);
    	componentList.add(c2);
    	componentList.add(c3);
    	componentList.add(c4);
    	given(componentRepository.findAll()).willReturn(componentList);
    	List<GrapeComponent> retrievedComponentList = componentService.list();
    	assertThat(retrievedComponentList).isNotNull();
    	assertThat(retrievedComponentList.size()).isEqualTo(4);
    }
}