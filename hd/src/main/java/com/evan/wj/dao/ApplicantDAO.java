package com.evan.wj.dao;

import com.evan.wj.models.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicantDAO extends JpaRepository<Applicant, Integer> {
}
