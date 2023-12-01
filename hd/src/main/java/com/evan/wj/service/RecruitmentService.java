package com.evan.wj.service;

import com.evan.wj.dao.RecruitmentDAO;
import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.Plant;
import com.evan.wj.models.Recruitment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecruitmentService {
    @Autowired
    RecruitmentDAO recruitmentDAO;
    
    public Recruitment getRecruitmentInfo(int id) {
        return recruitmentDAO.findById(id);
    }

    public void addNewRecruitment(Recruitment recruitment) {
        recruitmentDAO.save(recruitment);
    }

    public void delRecruitmentById(int id) {
        recruitmentDAO.deleteById(id);
    }

    public PaginationDTO<Recruitment> getAllRecruitmentsByPage(int page, int limit) {
        List<Recruitment> recruitments = recruitmentDAO.findAll();
        List<Recruitment> curRecruitmentList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < Math.min(limit * page, recruitments.size()); i++) {
            curRecruitmentList.add(recruitments.get(i));
        }
        return new PaginationDTO<>(recruitments.size(), curRecruitmentList);
    }
}
