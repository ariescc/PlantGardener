package com.evan.wj.service;

import com.evan.wj.dao.PlantDAO;
import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.Drug;
import com.evan.wj.models.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlantService {
    @Autowired
    PlantDAO plantDAO;
    
    public Plant getPlantInfo(int id) {
        return plantDAO.findById(id);
    }

    public void addNewPlant(Plant plant) {
        plantDAO.save(plant);
    }

    public void delPlantById(int id) {
        plantDAO.deleteById(id);
    }

    public PaginationDTO<Plant> getAllPlantsByPage(int page, int limit) {
        List<Plant> plants = plantDAO.findAll();
        List<Plant> curPlantList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < Math.min(limit * page, plants.size()); i++) {
            curPlantList.add(plants.get(i));
        }
        return new PaginationDTO<>(plants.size(), curPlantList);
    }
}
