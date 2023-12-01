package com.evan.wj.service;

import com.evan.wj.dao.ApplyStatusDAO;
import com.evan.wj.dao.UserDAO;
import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.ApplyStatus;
import com.evan.wj.models.Plant;
import com.evan.wj.models.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplyStatusService {
    @Autowired
    ApplyStatusDAO applyStatusDAO;
    
    @Autowired
    UserDAO userDAO;
    
    public ApplyStatus getApplyStatusInfo(int id) {
        return applyStatusDAO.findById(id);
    }

    public void addApplyStatus(int id) {
        Subject subject = SecurityUtils.getSubject();
        User currentUser = userDAO.findByUsername(subject.getPrincipal().toString());
        ApplyStatus applyStatus = new ApplyStatus();
        applyStatus.setUid(currentUser.getId());
        applyStatus.setRid(id);
        applyStatus.setStatus(2);  // 初始状态为 <申请中>
        applyStatusDAO.save(applyStatus);
    }

    public void delApplyStatusById(int id) {
        applyStatusDAO.deleteById(id);
    }

    public PaginationDTO<ApplyStatus> getAllApplyStatusByPage(int page, int limit) {
        List<ApplyStatus> applyStatuses = applyStatusDAO.findAll();
        List<ApplyStatus> curApplyStatusList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < Math.min(limit * page, applyStatuses.size()); i++) {
            curApplyStatusList.add(applyStatuses.get(i));
        }
        return new PaginationDTO<>(applyStatuses.size(), curApplyStatusList);
    }
}
