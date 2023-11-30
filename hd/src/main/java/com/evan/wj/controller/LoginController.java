package com.evan.wj.controller;

import com.evan.wj.models.User;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/api/login")
    public Result login(@RequestBody User requestUser, HttpSession session) {
        // 对 html 标签进行转义，防止xss攻击
        String username = requestUser.getUsername();
        username = HtmlUtils.htmlEscape(username);
        log.info(username, requestUser.getPassword());

        /////////  生成密码和salt --- 调试信息
        String testPwd = "123";
        // 默认生成 16 位盐
        String salt = new SecureRandomNumberGenerator().nextBytes().toString();
        int times = 2;
        String encodedPassword = new SimpleHash("md5", testPwd, salt, times).toString();
        log.info(encodedPassword);
        log.info(salt);
        ////////////

        // 获取认证主体，Subject代表当前的用户
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken =
                new UsernamePasswordToken(username, requestUser.getPassword());
        usernamePasswordToken.setRememberMe(true);

        try {
            log.info("hello");
            subject.login(usernamePasswordToken);
            log.info("hi");
            User user = userService.findByUsername(username);
            log.info(subject.getPrincipals().toString(), requestUser.getPassword());
            log.info("user", user.isEnabled());
            if (!user.isEnabled()) {
                return ResultFactory.buildFailResult("该用户已被禁用！");
            }
            return ResultFactory.buildSuccessResult(username);
        } catch (IncorrectCredentialsException e) {
            return ResultFactory.buildFailResult("密码错误");
        } catch (UnknownAccountException e) {
            return ResultFactory.buildFailResult("账号不存在");
        }
    }

    /**
     * 用户注册
     * @param user 提交的用户信息
     * @return 注册结果
     */
    @PostMapping("/api/register")
    public Result register(@RequestBody User user) {
        log.info("register user: ", user.getUsername());
        int status = userService.register(user);
        switch (status) {
            case 0:
                return ResultFactory.buildFailResult("用户名和密码都不能为空");
            case 1:
                return ResultFactory.buildSuccessResult("注册成功");
            case 2:
                return ResultFactory.buildFailResult("用户已存在");
        }
        return ResultFactory.buildFailResult("未知错误");
    }
    
    @GetMapping("/api/logout")
    public Result logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultFactory.buildSuccessResult("成功退出");
    }
    
    @GetMapping("/api/getUserInfo")
    public Result getUserInfo() {
        Subject subject = SecurityUtils.getSubject();
        log.info("hi111", subject.getPrincipal().toString());
        User user = userService.findByUsername(subject.getPrincipal().toString());
        return ResultFactory.buildSuccessResult(user);
    }
    
    @PostMapping("/api/saveUserInfo")
    public Result saveUserInfo(@RequestBody User user) {
        userService.editUser(user);
        return ResultFactory.buildSuccessResult("修改成功");
    }
    
}
