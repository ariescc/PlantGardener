package com.evan.wj.dao;

import com.evan.wj.models.Drug;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DrugDAO extends JpaRepository<Drug, Integer> {
    Drug findByName(String name);
    Drug findById(int id);
    @Query("SELECT d FROM Drug d WHERE d.name LIKE %?1%")
    List<Drug> findByNameLike(String name);
}
