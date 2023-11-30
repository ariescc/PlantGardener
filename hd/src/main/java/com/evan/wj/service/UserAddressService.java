package com.evan.wj.service;

import com.evan.wj.dao.UserAddressDAO;
import com.evan.wj.models.User;
import com.evan.wj.models.UserAddress;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class UserAddressService {
    @Autowired
    UserAddressDAO userAddressDAO;
    
    @Autowired
    UserService userService;
    
    public List<UserAddress> findUserAddressListByCurrentUser(int uid) {
        return userAddressDAO.findByUid(uid);
    }

    /**
     * 给用户添加一个新的地址
     * @param userAddress 新地址
     */
    public void addNewAddress(UserAddress userAddress) {
        String address = userAddress.getAddress();
        String postcode = userAddress.getPostcode();
        String houseNumber = userAddress.getHouseNumber();
        String contactPerson = userAddress.getContactPerson();
        String tag = userAddress.getTag();
        
        address = HtmlUtils.htmlEscape(address);
        userAddress.setAddress(address);
        postcode = HtmlUtils.htmlEscape(postcode);
        userAddress.setPostcode(postcode);
        houseNumber = HtmlUtils.htmlEscape(houseNumber);
        userAddress.setHouseNumber(houseNumber);
        contactPerson = HtmlUtils.htmlEscape(contactPerson);
        userAddress.setContactPerson(contactPerson);
        tag = HtmlUtils.htmlEscape(tag);
        userAddress.setTag(tag);
        
        // 获取当前用户 id
        Subject subject = SecurityUtils.getSubject();
        User user = userService.findByUsername(subject.getPrincipal().toString());
        userAddress.setUid(user.getId());
        
        userAddressDAO.save(userAddress);
    }
    
    public void deleteUserAddress(UserAddress userAddress) {
        
    }
}
