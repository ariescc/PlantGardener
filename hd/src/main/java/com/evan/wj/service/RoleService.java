package com.evan.wj.service;

import com.evan.wj.dao.RoleDAO;
import com.evan.wj.models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleDAO roleDAO;
    
    @Autowired
    UserService userService;
    
//    public List<Role> listWithPermissionsAndMenus() {
//        List<Role> roles = roleDAO.findAll();
//        List<Permission> permissions;
//        List<Menu> menus;
//        for (Role role : roles) {
//            
//        }
//    }
    
    public List<Role> findAll() {
        return roleDAO.findAll();
    }
    
    public void addOrUpdate(Role role) {
        roleDAO.save(role);
    }
    
    public Role findByRoleName(String rolename) {
        return roleDAO.findByName(rolename);
    }
    
    public Role updateRoleStatus(Role role) {
        Role roleInDB = roleDAO.findById(role.getId());
        roleInDB.setEnabled(role.isEnabled());
        return roleDAO.save(roleInDB);
    }
}
