package com.evan.wj.dao;

import com.evan.wj.models.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadFileDAO extends JpaRepository<UploadFile, Integer> {
    UploadFile findByFileName(String fileName);
}
