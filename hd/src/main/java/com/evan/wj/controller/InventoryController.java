package com.evan.wj.controller;

import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.DrugInventory;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.DrugService;
import com.evan.wj.service.InventoryService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 库存管理
 */
@RestController
@Slf4j
public class InventoryController {
    @Autowired
    InventoryService inventoryService;
    
    @Autowired
    DrugService drugService;
    
    @ApiOperation(value = "分页查询药品库存信息")
    @GetMapping("/api/drugInventory/list")
    public Result getDrugInventoryList(@RequestParam("page") int page,
                                       @RequestParam("limit") int limit,
                                       @RequestParam("sort") String sort) {
        PaginationDTO<DrugInventory> drugInventoryList = inventoryService.queryInventoriesByPage(page, limit);
        return ResultFactory.buildSuccessResult(drugInventoryList);
    }
    
    @ApiOperation(value = "药品库存信息")
    @GetMapping("/api/drugInventoryInfo/{id}")
    public Result getDrugInventoryInfo(@PathVariable("id") int id) {
        DrugInventory drugInventory = inventoryService.getDrugInventoryInfo(id);
        return ResultFactory.buildSuccessResult(drugInventory);
    }
    
    @ApiOperation(value = "更新药品库存信息")
    @PostMapping("/api/updateInventory")
    public Result updateInventoryInfo(@RequestBody DrugInventory drugInventory) {
        inventoryService.updateInventoryInfo(drugInventory);
        return ResultFactory.buildSuccessResult("更新成功！");
    }
    
}
