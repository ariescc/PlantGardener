package com.evan.wj.dao;

import com.evan.wj.models.DrugPrice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugPriceDAO extends JpaRepository<DrugPrice, Integer> {
    DrugPrice findByDrugId(int drugId);
//    @Query("SELECT dp FROM DrugPrice dp WHERE dp.drugname LIKE %?1%")
//    List<DrugPrice> findByDrugnameLike(String drugname);
}
