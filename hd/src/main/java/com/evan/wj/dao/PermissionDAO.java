package com.evan.wj.dao;

import com.evan.wj.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionDAO extends JpaRepository<Permission, Integer> {
    Permission findById(int id);
}
