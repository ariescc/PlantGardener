package com.evan.wj.dao;

import com.evan.wj.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuDAO extends JpaRepository<Menu, Integer> {
    Menu findById(int id);
    
    List<Menu> findAllByRoleId(int roleId);
    
    List<Menu> findAllByParentId(int parentId); 
}
