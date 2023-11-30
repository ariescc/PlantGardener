package com.evan.wj.dao;

import com.evan.wj.models.SaleOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleOrderDAO extends JpaRepository<SaleOrder, Integer> {
    /**
     * 通过销售人员id查找名下负责的所有订单
     */
    List<SaleOrder> findBySaleStuffId(int saleStuffId);
}
