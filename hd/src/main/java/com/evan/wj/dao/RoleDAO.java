package com.evan.wj.dao;

import com.evan.wj.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDAO extends JpaRepository<Role, Integer> {
    Role findById(int id);
    
    Role findByName(String name);
}
