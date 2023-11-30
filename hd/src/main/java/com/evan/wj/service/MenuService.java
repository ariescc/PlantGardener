package com.evan.wj.service;

import com.evan.wj.dao.MenuDAO;
import com.evan.wj.models.*;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    @Autowired
    MenuDAO menuDAO;

    @Autowired
    UserService userService;
    
    @Autowired
    RoleService roleService;
    
    /**
     * 通过父ID获取所有子菜单
     * @param parentId
     * @return
     */
    public List<Menu> getAllByParentId(int parentId) {
        return menuDAO.findAllByParentId(parentId);
    }
    
    public List<Menu> getMenusByCurrentUser() {
        // 获取当前用户
        String username = SecurityUtils.getSubject().getPrincipal().toString();
        User user = userService.findByUsername(username);
        
        // 获取当前用户的 Role id
        Role role = roleService.findByRoleName(user.getRolename());
        
        // 获取 role 所对应的 menu ids
        List<Menu> menus = menuDAO.findAllByRoleId(role.getId()).stream().distinct().collect(Collectors.toList());

        // 调整菜单结构
        AdjustMenuStructure(menus);
        return menus;
    }

    /**
     * 根据 parent id，构建层级菜单
     * @param menus 所有菜单项（父子菜单项并列）
     */
    private void AdjustMenuStructure(List<Menu> menus) {
        menus.forEach(menu -> {
            List<Menu> children = getAllByParentId(menu.getId());
            menu.setChildren(children);
        });
        
        menus.removeIf(menu -> menu.getParentId() != 0);
    }
}
