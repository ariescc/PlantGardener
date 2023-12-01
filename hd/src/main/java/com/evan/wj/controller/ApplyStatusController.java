package com.evan.wj.controller;

import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.ApplyStatus;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.ApplyStatusService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class ApplyStatusController {
    @Autowired
    ApplyStatusService applyStatusService;

    @ApiOperation(value = "获取应聘状态信息")
    @GetMapping("/api/applyStatus/{id}")
    public Result getApplyStatusInfo(@PathVariable("id") int id) {
        ApplyStatus applyStatus = applyStatusService.getApplyStatusInfo(id);
        return ResultFactory.buildSuccessResult(applyStatus);
    }

    @ApiOperation(value = "新增应聘状态")
    @PostMapping("/api/applyStatus/add")
    public Result addApplyStatus(@RequestBody Map map) {
        applyStatusService.addApplyStatus((Integer) map.get("id"));
        return ResultFactory.buildSuccessResult("新增成功！");
    }

    @ApiOperation(value = "删除应聘状态信息")
    @PostMapping("/api/applyStatus/del")
    public Result deleteApplyStatus(@RequestBody Map map) {
        applyStatusService.delApplyStatusById((Integer) map.get("id"));
        return ResultFactory.buildSuccessResult("删除成功！");
    }

    @ApiOperation(value = "分页应聘状态信息")
    @GetMapping("/api/applyStatus/list")
    public Result getAllApplyStatus(@RequestParam("page") int page,
                               @RequestParam("limit") int limit,
                               @RequestParam("sort") String sort) {
        PaginationDTO<ApplyStatus> allApplyStatus =
                applyStatusService.getAllApplyStatusByPage(page, limit);
        return ResultFactory.buildSuccessResult(allApplyStatus);
    }
}
