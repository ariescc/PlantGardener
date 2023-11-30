package com.evan.wj.service;

import com.evan.wj.dao.DrugCompanyDAO;
import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.DrugCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DrugCompanyService {
    @Autowired
    DrugCompanyDAO drugCompanyDAO;
    
    public PaginationDTO<DrugCompany> queryByPage(int page, int limit) {
        List<DrugCompany> companies = drugCompanyDAO.findAll();
        List<DrugCompany> curPageUserList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < Math.min(limit * page, companies.size()); i++) {
            curPageUserList.add(companies.get(i));
        }
        return new PaginationDTO<>(companies.size(), curPageUserList);
    }
}
