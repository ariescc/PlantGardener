package com.evan.wj.controller;

import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.*;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.AdminService;
import com.evan.wj.service.DrugCompanyService;
import com.evan.wj.service.DrugService;
import com.evan.wj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class AdminController {
    @Autowired
    UserService userService;
    
    @Autowired
    AdminService adminService;
    
    @Autowired
    DrugCompanyService drugCompanyService;
    
    @Autowired
    DrugService drugService;

    /**
     * 用户管理功能
     * @param page 当前页码
     * @param limit 单页限制条目数
     * @param sort 排序方式
     * @return 用户列表
     */
    @GetMapping("/api/userlist")
    public Result getUserList(@RequestParam("page") int page,
                              @RequestParam("limit") int limit,
                              @RequestParam("sort") String sort) {
        log.info(String.valueOf(page));
        log.info(String.valueOf(limit));
        log.info(sort);
        PaginationDTO<User> result = userService.queryByPage(page, limit);
//        List<User> users = userService.list(query);
        return ResultFactory.buildSuccessResult(result);
    }

    /**
     * 药企管理功能
     * @param page 当前页码
     * @param limit 单页限制条目数
     * @param sort 排序方式
     * @return 药企列表
     */
    @GetMapping("/api/companylist")
    public Result getDrugCompanyList(@RequestParam("page") int page,
                                     @RequestParam("limit") int limit,
                                     @RequestParam("sort") String sort) {
        log.info(String.valueOf(page));
        PaginationDTO<DrugCompany> result = drugCompanyService.queryByPage(page, limit);
        return ResultFactory.buildSuccessResult(result);
    }
    
    @GetMapping(value = "/api/merchantlist")
    public Result getMerchantList() {
        List<Merchant> merchants = adminService.listAllMerchants();
        return ResultFactory.buildSuccessResult(merchants);
    }

    /**
     * 修改用户信息
     * @param user 用户
     * @return 修改结果
     */
    @PostMapping("/api/alterUserInfo")
    public Result AlterUserInfo(@RequestBody User user) {
        userService.alterUserInfo(user);
        return ResultFactory.buildSuccessResult("修改成功!");
    }
    
    @GetMapping("/api/drugpricelist")
    public Result drugPriceList(@RequestParam("page") int page,
                                @RequestParam("limit") int limit,
                                @RequestParam("sort") String sort) {
        PaginationDTO<DrugPrice> result = drugService.queryDrugPriceByPage(page, limit);
        return ResultFactory.buildSuccessResult(result);
    }
    
//    @GetMapping("/api/drugprice/fuzzysearch")
//    public Result getDrugOptionsByFuzzySearch(@RequestParam("keyword") String keyword) {
//        List<Drug> filterDrugList = drugService.fuzzySearchByKeyword(keyword);
//        return ResultFactory.buildSuccessResult(filterDrugList);
//    }
}
