package com.evan.wj.service;

import com.evan.wj.dao.*;
import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrderService {
    @Autowired
    PurchaseOrderDAO purchaseOrderDAO;
    
    @Autowired
    DrugDAO drugDAO;
    
    @Autowired
    UserDAO userDAO;
    
    @Autowired
    DrugInventoryDAO drugInventoryDAO;
    
    @Autowired
    DrugPriceDAO drugPriceDAO;

    /**
     * 分页查询采购订单记录
     */
    public PaginationDTO<PurchaseOrder> queryPurchaseOrderByPage(int page, int limit) {
        List<PurchaseOrder> purchaseOrderList = purchaseOrderDAO.findAll();
        List<PurchaseOrder> curPagePurchaseOrderlList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < Math.min(limit * page, purchaseOrderList.size()); i++) {
            curPagePurchaseOrderlList.add(purchaseOrderList.get(i));
        }
        return new PaginationDTO<>(purchaseOrderList.size(), curPagePurchaseOrderlList);
    }

    /**
     * 新建采购订单
     */
    public int addPurchaseOrder(PurchaseOrder purchaseOrder) {
        // 新建记录存入采购订单表中
        Subject subject = SecurityUtils.getSubject();
        User userInDB = userDAO.findByUsername(subject.getPrincipal().toString());
        purchaseOrder.setPersonInCharge(userInDB.getName());
        
        // 判断采购数量是否大于最大库存
        List<DrugInventory> drugInventories = drugInventoryDAO.findAllByDrugId(purchaseOrder.getDrugId());
        int curQuantity = 0;
        for (DrugInventory inventory: drugInventories) {
            curQuantity += inventory.getInventoryQuantity();
        }
        
        DrugPrice drugPrice = drugPriceDAO.findByDrugId(purchaseOrder.getDrugId());
        if (purchaseOrder.getQuantity() > drugPrice.getMaxInventoryQuantity() - curQuantity) {
            return 0;
        }

        // 生成订单，数据入库
        purchaseOrderDAO.save(purchaseOrder);
        
        // 相应更新药品库存信息
        DrugInventory inventoryItem = new DrugInventory();
        inventoryItem.setDrugName(purchaseOrder.getDrugName());
        inventoryItem.setDrugId(purchaseOrder.getDrugId());
        inventoryItem.setInventoryQuantity(purchaseOrder.getQuantity());
        inventoryItem.setManufacturer(purchaseOrder.getManufacturer());
        inventoryItem.setShelfLife(purchaseOrder.getShelfLife());
        inventoryItem.setProductionDate(purchaseOrder.getProductionDate());
        inventoryItem.setMaxInventoryQuantity(drugPrice.getMaxInventoryQuantity());
        inventoryItem.setUpdateTime(LocalDateTime.now());
        inventoryItem.setApprovalStatus(0);  // 默认 0，创建订单时处于未审批状态，不可销售
        drugInventoryDAO.save(inventoryItem);
        return 1;
    }

    /**
     * 通过id删除采购订单
     */
    public void deletePurchaseOrderPriceById(int id) {
        purchaseOrderDAO.deleteById(id);
    }

    /**
     * 通过id获取采购订单信息
     */
    public PurchaseOrder getPurchaseOrderInfo(int id) {
        return purchaseOrderDAO.findById(id);
    }
}
