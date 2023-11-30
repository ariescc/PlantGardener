package com.evan.wj.dao;

import com.evan.wj.models.DrugInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DrugInventoryDAO extends JpaRepository<DrugInventory, Integer> {
    DrugInventory findById(int id);
    @Query("SELECT I FROM DrugInventory I ORDER BY I.productionDate")
    List<DrugInventory> findAllByDrugId(int drugId);
}
