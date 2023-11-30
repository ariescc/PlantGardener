package com.evan.wj.service;

import com.evan.wj.dao.DrugDAO;
import com.evan.wj.dao.PurchaseOrderDAO;
import com.evan.wj.dto.PaginationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StockManageService {
    @Autowired
    DrugDAO drugDAO;
    
    @Autowired
    PurchaseOrderDAO purchaseOrderDAO;
    
    
}
