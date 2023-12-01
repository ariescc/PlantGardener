package com.evan.wj.dao;

import com.evan.wj.models.Butlar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BularDAO extends JpaRepository<Butlar, Integer> {
    Butlar findById(int id);
    void deleteById(int id);
}
