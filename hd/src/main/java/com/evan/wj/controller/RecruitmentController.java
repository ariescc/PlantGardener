package com.evan.wj.controller;

import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.Plant;
import com.evan.wj.models.Recruitment;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.PlantService;
import com.evan.wj.service.RecruitmentService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class RecruitmentController {
    @Autowired
    RecruitmentService recruitmentService;

    @ApiOperation(value = "获取单条招聘信息")
    @GetMapping("/api/recruitment/{id}")
    public Result getRecruitmentInfo(@PathVariable("id") int id) {
        Recruitment recruitment = recruitmentService.getRecruitmentInfo(id);
        return ResultFactory.buildSuccessResult(recruitment);
    }

    @ApiOperation(value = "发布新的招聘信息")
    @PostMapping("/api/recruitment/add")
    public Result addNewRecruitment(@RequestBody Recruitment recruitment) {
        recruitmentService.addNewRecruitment(recruitment);
        return ResultFactory.buildSuccessResult("新增成功！");
    }

    @ApiOperation(value = "删除招聘信息")
    @PostMapping("/api/recruitment/del")
    public Result deleteRecruitment(@RequestBody Map map) {
        recruitmentService.delRecruitmentById((Integer) map.get("id"));
        return ResultFactory.buildSuccessResult("删除成功！");
    }

    @ApiOperation(value = "分页查询招聘信息")
    @GetMapping("/api/recruitment/list")
    public Result getAllRecruitments(@RequestParam("page") int page,
                               @RequestParam("limit") int limit,
                               @RequestParam("sort") String sort) {
        PaginationDTO<Recruitment> allRecruitments = 
                recruitmentService.getAllRecruitmentsByPage(page, limit);
        return ResultFactory.buildSuccessResult(allRecruitments);
    }
}
