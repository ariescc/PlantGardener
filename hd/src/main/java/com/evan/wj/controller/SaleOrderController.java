package com.evan.wj.controller;

import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.SaleOrder;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.SaleOrderService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 销售订单
 */
@RestController
@Slf4j
public class SaleOrderController {
    @Autowired
    SaleOrderService saleOrderService;
    
    @ApiOperation(value = "所有销售订单查询")
    @GetMapping("/api/saleOrder/list")
    public Result getAllSaleOrderList(@RequestParam("page") int page,
                                      @RequestParam("limit") int limit,
                                      @RequestParam("sort") String sort) {
        PaginationDTO<SaleOrder> saleOrderList = saleOrderService.queryAllSaleOrdersByPage(page, limit);
        return ResultFactory.buildSuccessResult(saleOrderList);
    }
    
    @ApiOperation(value = "通过销售人员id查询名下所有订单")
    @GetMapping("/api/saleOrder/myorders")
    public Result getSaleOrderListByStuff(@RequestParam("page") int page,
                                          @RequestParam("limit") int limit,
                                          @RequestParam("sort") String sort) {
        PaginationDTO<SaleOrder> saleOrderList = saleOrderService.querySaleOrdersByStuff(page, limit);
        return ResultFactory.buildSuccessResult(saleOrderList);
    }
    
    @ApiOperation(value = "新建销售订单")
    @PostMapping("/api/saleOrder/add")
    public Result addNewSaleOrder(@RequestBody SaleOrder saleOrder) {
        int ret = saleOrderService.addNewSaleOrder(saleOrder);
        if (ret == 0) {
            return ResultFactory.buildFailResult("药品没有库存");
        } else if (ret == 1) {
            return ResultFactory.buildFailResult("药品库存余量不足");
        } else {
            return ResultFactory.buildSuccessResult("新建销售订单成功");
        }
    }
    
    @ApiOperation(value = "删除销售订单")
    @PostMapping("/api/saleOrder/delete")
    public Result deleteSaleOrder(@RequestBody Map map) {
        saleOrderService.deleteSaleOrderById((SaleOrder) map.get("id"));
        return ResultFactory.buildSuccessResult("删除成功！");
    }
}
