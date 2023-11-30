package com.evan.wj.controller;

import com.evan.wj.dto.ApprovalInfoDTO;
import com.evan.wj.dto.DrugDataDTO;
import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.Approval;
import com.evan.wj.models.Drug;
import com.evan.wj.models.DrugPrice;
import com.evan.wj.result.Result;
import com.evan.wj.result.ResultFactory;
import com.evan.wj.service.DrugService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class DrugController {
    @Autowired
    DrugService drugService;

    /**
     * 获取药品信息
     * @param id 药品id
     * @return
     */
    @GetMapping("/api/druginfo/{id}")
    public Result getDrugInfo(@PathVariable("id") int id) {
        Drug drug = drugService.getDrugInfo(id);
        return ResultFactory.buildSuccessResult(drug);
    }
    
    @PostMapping("/api/adddrug")
    public Result addDrug(@RequestBody Drug drug) {
        drugService.addDrug(drug);
        return ResultFactory.buildSuccessResult("新增成功！");
    }

    @PostMapping("/api/drugdel")
    public Result deleteDrug(@RequestBody Map map) {
        log.info(String.valueOf(map.get("id")));
        drugService.delDrugById((Integer) map.get("id"));
        return ResultFactory.buildSuccessResult("删除成功！");
    }
    
    @ApiOperation(value = "药品许可证中模糊查找药品库药品")
    @GetMapping("/api/approval/drugSearch")
    public Result getDrugOptionsByfuzzySearch(@RequestParam("keyword") String keyword) {
        List<Drug> filterDrugList = drugService.fuzzySearchByKeyword(keyword);
        return ResultFactory.buildSuccessResult(filterDrugList);
    }
    
    @ApiOperation(value = "查询药品所有的整合数据")
    @GetMapping("/api/drugData/fuzzySearch")
    public Result getDrugDataByFuzzySearch(@RequestParam("keyword") String keyword) {
        List<DrugDataDTO> filterDrugDataList = drugService.DrugDataFuzzySearchByKeyword(keyword);
        return ResultFactory.buildSuccessResult(filterDrugDataList);
    }
    
    @ApiOperation(value = "获取许可证的详细信息")
    @GetMapping("/api/approvalInfo/{id}")
    public Result getApprovalInfo(@PathVariable("id") int id) {
        ApprovalInfoDTO approvalInfoDTO = drugService.getApprovalInfo(id);
        return ResultFactory.buildSuccessResult(approvalInfoDTO);
    }
    
    @ApiOperation(value = "分页查询许可证列表")
    @GetMapping("/api/approval/list")
    public Result getApprovalList(@RequestParam("page") int page,
                                  @RequestParam("limit") int limit,
                                  @RequestParam("sort") String sort) {
        PaginationDTO<Approval> approvalList = drugService.queryApprovalListByPage(page, limit);
        return ResultFactory.buildSuccessResult(approvalList);
    }
    
    @ApiOperation(value = "许可证文件上传")
    @PostMapping("/api/approval/upload")
    public Result uploadApproval(HttpServletRequest request,
                                 @RequestParam(value = "drugId") int drugId,
                                 @RequestParam(value = "drugName") String drugName,
                                 @RequestParam(value = "manufacturer") String manufacturer,
                                 @RequestParam(value = "approvalNumber") String approvalNumber,
                                 @RequestParam(value = "file") MultipartFile approvalFile) throws FileNotFoundException {
//        log.info(String.valueOf(drugId) + ", " + drugName + ", " + manufacturer + ", " + approvalNumber);
//        log.info(approvalFile.toString());
        Approval approval = new Approval();
        approval.setDrugName(drugName);
        approval.setDrugId(drugId);
        approval.setManufacturer(manufacturer);
        approval.setApprovalNumber(approvalNumber);
        return ResultFactory.buildSuccessResult(drugService.uploadDrugApproval(approval, approvalFile, request));
    }
    
    @ApiOperation(value = "新增药品许可证记录")
    @PostMapping("/api/addApproval")
    public Result addDrugApproval(@RequestBody Approval approval) {
        return ResultFactory.buildSuccessResult(drugService.addDrugApproval(approval));
    }
    
    @ApiOperation(value = "根据id删除药品许可证信息")
    @PostMapping("/api/approval/delete")
    public Result deleteApproval(@RequestBody Map map) {
        drugService.deleteApprovalById((Integer) map.get("id"));
        return ResultFactory.buildSuccessResult("删除成功！");
    }
    
    @ApiOperation(value = "获取药品定价列表")
    @GetMapping("/api/drugPrice/list")
    public Result getDrugPriceList(@RequestParam("page") int page,
                                   @RequestParam("limit") int limit,
                                   @RequestParam("sort") String sort) {
        PaginationDTO<DrugPrice> drugPriceList = drugService.queryDrugPriceByPage(page, limit);
        return ResultFactory.buildSuccessResult(drugPriceList);
    }

    @ApiOperation(value = "新增药品定价")
    @PostMapping("/api/drugPrice/add")
    public Result addDrugPrice(@RequestBody DrugPrice drugPrice) {
        drugService.addDrugPrice(drugPrice);
        return ResultFactory.buildSuccessResult("新增成功！");
    }
    
    @ApiOperation(value = "删除单条药品价格信息")
    @PostMapping("/api/drugPrice/delete")
    public Result deleteDrugPrice(@RequestBody Map map) {
        drugService.deleteDrugPriceById((Integer) map.get("id"));
        return ResultFactory.buildSuccessResult("删除成功！");
    }
}