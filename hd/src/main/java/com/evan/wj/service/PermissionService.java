package com.evan.wj.service;

import com.evan.wj.dao.PermissionDAO;
import com.evan.wj.models.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService {
    @Autowired
    PermissionDAO permissionDAO;
    
    @Autowired
    RoleService roleService;
    
    public List<Permission> list() {
        return permissionDAO.findAll();
    }
    
    public boolean needFilter(String requestAPI) {
        List<Permission> permissions = permissionDAO.findAll();
        for (Permission p : permissions) {
            if (requestAPI.startsWith(p.getUrl())) {
                return true;
            }
        }
        return false;
    }
}
