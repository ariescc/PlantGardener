package com.evan.wj.dao;

import com.evan.wj.models.Approval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApprovalDAO extends JpaRepository<Approval, Integer> {
    Approval findByDrugId(int drugId);
    
    Approval findById(int id);
}
