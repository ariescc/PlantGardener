package com.evan.wj.dao;

import com.evan.wj.models.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MerchantDAO extends JpaRepository<Merchant, Integer> {
    Merchant findMerchantById(int id);
}
