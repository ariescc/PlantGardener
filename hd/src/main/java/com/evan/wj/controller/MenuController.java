package com.evan.wj.controller;

import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuController {
    @Autowired
    MenuService menuService;

    /**
     * 获取当前登录用户的菜单
     * @return
     */
    @GetMapping("/api/menu")
    public Result menu() {
        return ResultFactory.buildSuccessResult(menuService.getMenusByCurrentUser());
    }
}
