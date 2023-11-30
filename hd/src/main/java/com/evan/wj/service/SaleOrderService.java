package com.evan.wj.service;

import com.evan.wj.dao.DrugInventoryDAO;
import com.evan.wj.dao.SaleOrderDAO;
import com.evan.wj.dao.UserDAO;
import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.DrugInventory;
import com.evan.wj.models.SaleOrder;
import com.evan.wj.models.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleOrderService {
    @Autowired
    SaleOrderDAO saleOrderDAO;
    
    @Autowired
    UserDAO userDAO;
    
    @Autowired
    DrugInventoryDAO drugInventoryDAO;
    
    @Autowired
    InventoryService inventoryService;

    /**
     * 分页查询 
     */
    public PaginationDTO<SaleOrder> queryAllSaleOrdersByPage(int page, int limit) {
        List<SaleOrder> saleOrders = saleOrderDAO.findAll();
        List<SaleOrder> curPageSaleOrderList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < Math.min(limit * page, saleOrders.size()); i++) {
            curPageSaleOrderList.add(saleOrders.get(i));
        }
        return new PaginationDTO<>(saleOrders.size(), curPageSaleOrderList);
    }

    /**
     * 查询销售名下所有订单
     */
    public PaginationDTO<SaleOrder> querySaleOrdersByStuff(int page, int limit) {
        Subject subject = SecurityUtils.getSubject();
        User user = userDAO.findByUsername(subject.getPrincipal().toString());
        List<SaleOrder> mySaleOrders = saleOrderDAO.findBySaleStuffId(user.getId());
        List<SaleOrder> curPageSaleOrderList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < Math.min(limit * page, mySaleOrders.size()); i++) {
            curPageSaleOrderList.add(mySaleOrders.get(i));
        }
        return new PaginationDTO<>(mySaleOrders.size(), curPageSaleOrderList);
    }

    /**
     * 新建销售订单
     * @param saleOrder
     * @return 0: 药品没有库存; 1: 药品库存余量不足; 2: 新建订单成功
     */
    public int addNewSaleOrder(SaleOrder saleOrder) {
        // 查询药品库存数量
        List<DrugInventory> drugInventories = drugInventoryDAO.findAllByDrugId(saleOrder.getDrugId());
        
        // 此类药品没有库存
        if (drugInventories.size() == 0) {
            return 0;
        }
        
        // 计算药品库存总量
        int totalInventories = 0;
        for (DrugInventory inventory: drugInventories) {
            totalInventories += inventory.getInventoryQuantity();
        }
        
        // 药品库存余量不足
        if (saleOrder.getQuantity() > totalInventories) {
            return 1;
        }
        
        // 从所有库存中更新信息
        int temp = saleOrder.getQuantity();
        for (DrugInventory inventory: drugInventories) {
            if (temp == 0) {
                break;
            }
            
            int inventoryQuantity = inventory.getInventoryQuantity();
            if (temp <= inventoryQuantity) {
                inventory.setInventoryQuantity(inventoryQuantity - temp);
            } else {
                inventory.setInventoryQuantity(0);
                temp -= inventoryQuantity;
            }
            inventory.setUpdateTime(LocalDateTime.now());
            inventoryService.updateInventoryInfo(inventory);  //  更新订单库存信息
        }
        
        Subject subject = SecurityUtils.getSubject();
        User currentUser = userDAO.findByUsername(subject.getPrincipal().toString());
        // 将当前销售人员信息写入订单中
        saleOrder.setSaleStuff(currentUser.getName());
        saleOrder.setSaleStuffId(currentUser.getId());
        saleOrderDAO.save(saleOrder);
        return 2;
    }
    
    public void deleteSaleOrderById(SaleOrder saleOrder) {
        saleOrderDAO.delete(saleOrder);
    }
}
