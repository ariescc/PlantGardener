package com.evan.wj.controller;

import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.PurchaseOrder;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.DrugService;
import com.evan.wj.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
public class OrderController {
    @Autowired
    DrugService drugService;
    
    @Autowired
    OrderService orderService;
    
    @ApiOperation(value = "获取采购订单列表")
    @GetMapping("/api/purchaseOrder/list")
    public Result getPurchaseOrderList(@RequestParam("page") int page,
                                       @RequestParam("limit") int limit,
                                       @RequestParam("sort") String sort) {
        PaginationDTO<PurchaseOrder> purchaseOrderList = orderService.queryPurchaseOrderByPage(page, limit);
        return ResultFactory.buildSuccessResult(purchaseOrderList);
    }
    
    @ApiOperation(value = "新增采购订单")
    @PostMapping("/api/purchaseOrder/add")
    public Result addPurchaseOrder(@RequestBody PurchaseOrder purchaseOrder) {
        int ret = orderService.addPurchaseOrder(purchaseOrder);
        if (ret == 1) {
            return ResultFactory.buildSuccessResult("生成订单成功！");
        } else {
            return ResultFactory.buildFailResult("库存已超上限，生成订单失败！");
        }
    }
    
    @ApiOperation(value = "删除采购订单")
    @PostMapping("/api/purchaseOrder/delete")
    public Result delPurchaseOrder(@RequestBody Map map) {
        orderService.deletePurchaseOrderPriceById((Integer) map.get("id"));
        return ResultFactory.buildSuccessResult("删除成功！");
    }
    
    @ApiOperation(value = "获取采购订单信息")
    @GetMapping("/api/purchaseOrderInfo/{id}")
    public Result getPurchaseOrderInfo(@PathVariable("id") int id) {
        PurchaseOrder purchaseOrder = orderService.getPurchaseOrderInfo(id);
        return ResultFactory.buildSuccessResult(purchaseOrder);
    }
}
