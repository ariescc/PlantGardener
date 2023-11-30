package com.evan.wj.dao;

import com.evan.wj.models.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAddressDAO extends JpaRepository<UserAddress, Integer> {
    List<UserAddress> findByUid(int uid);
}
