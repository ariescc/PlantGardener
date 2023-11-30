package com.evan.wj.service;

import com.evan.wj.dao.MerchantDAO;
import com.evan.wj.models.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    MerchantDAO merchantDAO;
    
    public List<Merchant> listAllMerchants() {
        return merchantDAO.findAll();
    }
    
}
