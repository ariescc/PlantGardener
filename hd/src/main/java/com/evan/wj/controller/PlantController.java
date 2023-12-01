package com.evan.wj.controller;

import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.Plant;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.PlantService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
public class PlantController {
    @Autowired
    PlantService plantService;

    @ApiOperation(value = "获取单个植物详细信息")
    @GetMapping("/api/plantInfo/{id}")
    public Result getPlantInfo(@PathVariable("id") int id) {
        Plant plant = plantService.getPlantInfo(id);
        return ResultFactory.buildSuccessResult(plant);
    }

    @ApiOperation(value = "登记一种新植物信息")
    @PostMapping("/api/plant/add")
    public Result addPlant(@RequestBody Plant plant) {
        plantService.addNewPlant(plant);
        return ResultFactory.buildSuccessResult("新增成功！");
    }

    @ApiOperation(value = "删除植物")
    @PostMapping("/api/plant/del")
    public Result deletePlant(@RequestBody Map map) {
        plantService.delPlantById((Integer) map.get("id"));
        return ResultFactory.buildSuccessResult("删除成功！");
    }

    @ApiOperation(value = "分页查询植物信息")
    @GetMapping("/api/plant/list")
    public Result getAllPlants(@RequestParam("page") int page,
                                  @RequestParam("limit") int limit,
                                  @RequestParam("sort") String sort) {
        PaginationDTO<Plant> allPlants = plantService.getAllPlantsByPage(page, limit);
        return ResultFactory.buildSuccessResult(allPlants);
    }
}
