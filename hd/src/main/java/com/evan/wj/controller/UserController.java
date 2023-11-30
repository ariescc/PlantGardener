package com.evan.wj.controller;

import com.evan.wj.models.User;
import com.evan.wj.models.UserAddress;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.UserAddressService;
import com.evan.wj.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.util.List;

@Slf4j
@RestController
public class UserController {
    @Autowired
    UserAddressService userAddressService;
    
    @Autowired
    UserService userService;
    
    @GetMapping("/api/getAddressList")
    public Result getCurrentUserAddressList() {
        Subject subject = SecurityUtils.getSubject();
        User user = userService.findByUsername(subject.getPrincipal().toString());
        List<UserAddress> addressList = userAddressService.findUserAddressListByCurrentUser(user.getId());
        return ResultFactory.buildSuccessResult(addressList);
    }
    
    @PostMapping("/api/addNewAddress")
    public Result addNewUserAddress(@RequestBody UserAddress userAddress) {
        userAddressService.addNewAddress(userAddress);
        return ResultFactory.buildSuccessResult("添加成功！");
    }
    
    @ApiOperation(value = "修改用户头像")
    @PostMapping("/api/updateAvatar")
    public Result updateAvatar(@RequestParam(value = "file") MultipartFile avatar,
                               HttpServletRequest request) throws FileNotFoundException {
        log.info(avatar.getName());
        return ResultFactory.buildSuccessResult(userService.updateAvator(avatar, request));
    }
    
    @ApiOperation(value = "修改个人信息")
    @PostMapping("/api/updateProfile")
    public Result updateProfile(@RequestBody User user) {
        userService.alterUserInfo(user);
        return ResultFactory.buildSuccessResult("修改成功！");
    }
}
