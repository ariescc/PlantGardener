package com.evan.wj.service;

import com.evan.wj.dao.UploadFileDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadFileService {
    @Autowired
    UploadFileDAO uploadFileDAO;

//    @Value("${uploadFile.path}")
//    private String path;
//    
//    @Value("${uploadFile.maxSize}")
//    private long maxSize;
}
