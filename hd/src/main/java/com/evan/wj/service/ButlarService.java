package com.evan.wj.service;

import com.evan.wj.dao.BularDAO;
import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.Butlar;
import com.evan.wj.models.Plant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ButlarService {
    @Autowired
    BularDAO bularDAO;
    
    public Butlar getButlarInfo(int id) {
        return bularDAO.findById(id);
    }

    public void addNewButlar(Butlar butlar) {
        bularDAO.save(butlar);
    }

    public void delButlarById(int id) {
        bularDAO.deleteById(id);
    }

    public PaginationDTO<Butlar> getAllButlarsByPage(int page, int limit) {
        List<Butlar> butlars = bularDAO.findAll();
        List<Butlar> curButlarList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < Math.min(limit * page, butlars.size()); i++) {
            curButlarList.add(butlars.get(i));
        }
        return new PaginationDTO<>(butlars.size(), curButlarList);
    }
}
