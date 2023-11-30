package com.evan.wj.dao;

import com.evan.wj.models.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderDAO extends JpaRepository<PurchaseOrder, Integer> {
    PurchaseOrder findById(int id);
}
