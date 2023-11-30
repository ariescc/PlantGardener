package com.evan.wj.controller;

import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.DrugPriceService;
import com.evan.wj.service.DrugService;
import com.evan.wj.service.StockManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class StockManageController {
    @Autowired
    StockManageService stockManageService;
    
    @Autowired
    DrugService drugService;
    
    @Autowired
    DrugPriceService drugPriceService;
    
//    @PostMapping("/api/stockmanage/drugpurchase/add")
//    public Result addRecord(@RequestBody DrugPurchaseRecord record) {
//        stockManageService.addRecord(record);
//        return ResultFactory.buildSuccessResult("新增成功！");
//    }
//    
//    @GetMapping("/api/stockmanage/drugsearch")
//    public Result getDrugOptionsByfuzzySearch(@RequestParam("keyword") String keyword) {
//        log.info("keyword", keyword);
//        List<DrugPrice> filterDrugList = drugPriceService.fuzzySearchByKeyword(keyword);
//        return ResultFactory.buildSuccessResult(filterDrugList);
//    }
}
