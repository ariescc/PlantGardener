package com.evan.wj.service;

import com.evan.wj.dao.DrugPriceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DrugPriceService {
    @Autowired
    DrugPriceDAO drugPriceDAO;
//    public List<DrugPrice> fuzzySearchByKeyword(String keyword) {
//        List<DrugPrice> drugPriceList = drugPriceDAO.findByDrugnameLike(keyword);
//        return drugPriceList;
//    }
}
