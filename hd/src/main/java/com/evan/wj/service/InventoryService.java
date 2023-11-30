package com.evan.wj.service;

import com.evan.wj.dao.DrugInventoryDAO;
import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.DrugInventory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 库存管理服务
 */
@Service
@Slf4j
public class InventoryService {
    @Autowired
    DrugInventoryDAO drugInventoryDAO;

    /**
     * 分页查询
     */
    public PaginationDTO<DrugInventory> queryInventoriesByPage(int page, int limit) {
        List<DrugInventory> drugInventories = drugInventoryDAO.findAll();
        List<DrugInventory> curPageDrugInventorylList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < Math.min(limit * page, drugInventories.size()); i++) {
            curPageDrugInventorylList.add(drugInventories.get(i));
        }
        return new PaginationDTO<>(drugInventories.size(), curPageDrugInventorylList);
    }

    /**
     * 获取药品库存信息详情
     */
    public DrugInventory getDrugInventoryInfo(int id) {
        return drugInventoryDAO.findById(id);        
    }

    /**
     * 更新药品库存信息
     */
    public void updateInventoryInfo(DrugInventory drugInventory) {
        DrugInventory inventoryInDB = drugInventoryDAO.findById(drugInventory.getId());
        inventoryInDB.setMaxInventoryQuantity(drugInventory.getMaxInventoryQuantity());
        inventoryInDB.setInventoryQuantity(drugInventory.getInventoryQuantity());
        inventoryInDB.setUpdateTime(drugInventory.getUpdateTime());
        drugInventoryDAO.save(inventoryInDB);
    }
}
