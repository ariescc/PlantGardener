package com.evan.wj.controller;

import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.ApplyStatus;
import com.evan.wj.models.Butlar;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.ApplyStatusService;
import com.evan.wj.service.ButlarService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class ButlarController {
    @Autowired
    ButlarService butlarService;

    @ApiOperation(value = "获取管家信息")
    @GetMapping("/api/butlar/{id}")
    public Result getButlarInfo(@PathVariable("id") int id) {
        Butlar butlar = butlarService.getButlarInfo(id);
        return ResultFactory.buildSuccessResult(butlar);
    }

    @ApiOperation(value = "新增管家信息")
    @PostMapping("/api/butlar/add")
    public Result addNewButlar(@RequestBody Butlar butlar) {
        butlarService.addNewButlar(butlar);
        return ResultFactory.buildSuccessResult("新增成功！");
    }

    @ApiOperation(value = "删除管家信息")
    @PostMapping("/api/butlar/del")
    public Result deleteButlar(@RequestBody Map map) {
        butlarService.delButlarById((Integer) map.get("id"));
        return ResultFactory.buildSuccessResult("删除成功！");
    }

    @ApiOperation(value = "分页查询管家信息")
    @GetMapping("/api/butlar/list")
    public Result getAllButlars(@RequestParam("page") int page,
                                    @RequestParam("limit") int limit,
                                    @RequestParam("sort") String sort) {
        PaginationDTO<Butlar> allButlars =
                butlarService.getAllButlarsByPage(page, limit);
        return ResultFactory.buildSuccessResult(allButlars);
    }
}
