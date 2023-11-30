package com.evan.wj.service;

import com.evan.wj.dao.SupplierDAO;
import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SupplierService {
    @Autowired
    SupplierDAO supplierDAO;

    /**
     * 
     * @param page
     * @param limit
     * @return
     */
    public PaginationDTO<Supplier> queryByPage(int page, int limit) {
        List<Supplier> suppliers = supplierDAO.findAll();
        List<Supplier> curPageSupplierList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < Math.min(limit * page, suppliers.size()); i++) {
            curPageSupplierList.add(suppliers.get(i));
        }
        return new PaginationDTO<>(suppliers.size(), curPageSupplierList);
    }

    /**
     * 通过供货商id查找供货商
     * @param id
     */
    public void delSupplierById(int id) {
        supplierDAO.deleteById(id);
    }

    /**
     * 获取药品详细信息
     * @param id 药品id
     * @return 药品实例
     */
    public Supplier getSupplierInfo(int id) {
        return supplierDAO.findById(id);
    }

    /**
     * 新增一个供货商
     * @param supplier 供货商实体
     */
    public void addSupplier(Supplier supplier) {
        supplierDAO.save(supplier);
    }
}
