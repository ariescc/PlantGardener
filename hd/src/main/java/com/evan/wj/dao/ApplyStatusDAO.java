package com.evan.wj.dao;

import com.evan.wj.models.ApplyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyStatusDAO extends JpaRepository<ApplyStatus, Integer> {
    ApplyStatus findById(int id);
    void deleteById(int id);
}
