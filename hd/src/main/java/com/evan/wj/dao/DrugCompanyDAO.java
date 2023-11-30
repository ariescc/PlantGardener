package com.evan.wj.dao;

import com.evan.wj.models.DrugCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugCompanyDAO extends JpaRepository<DrugCompany, Integer> {
    DrugCompany findByName(String name);
}
