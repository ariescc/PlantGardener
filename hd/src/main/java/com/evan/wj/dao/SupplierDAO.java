package com.evan.wj.dao;

import com.evan.wj.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierDAO extends JpaRepository<Supplier, Integer> {
    Supplier findByName(String name);
    Supplier findById(int id);
}
