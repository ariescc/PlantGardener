package com.evan.wj.dao;

import com.evan.wj.models.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentDAO extends JpaRepository<Recruitment, Integer> {
    Recruitment findById(int id);
    void deleteById(int id);
}
