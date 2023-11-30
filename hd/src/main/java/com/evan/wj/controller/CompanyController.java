package com.evan.wj.controller;

import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.Drug;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.DrugService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class CompanyController {
    @Autowired
    DrugService drugService;
    
    @GetMapping("/api/druglist")
    public Result getDrugList(@RequestParam("page") int page,
                              @RequestParam("limit") int limit,
                              @RequestParam("sort") String sort) {
        PaginationDTO<Drug> result = drugService.queryByPage(page, limit);
        return ResultFactory.buildSuccessResult(result);
    }
}
