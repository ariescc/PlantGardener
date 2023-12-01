package com.evan.wj.dao;

import com.evan.wj.models.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantDAO extends JpaRepository<Plant, Integer> {
    Plant findById(int id);
    void deleteById(int id);
}
