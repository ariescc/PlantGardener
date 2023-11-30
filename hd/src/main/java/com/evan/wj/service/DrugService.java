package com.evan.wj.service;

import com.evan.wj.dao.ApprovalDAO;
import com.evan.wj.dao.DrugDAO;
import com.evan.wj.dao.DrugPriceDAO;
import com.evan.wj.dto.ApprovalInfoDTO;
import com.evan.wj.dto.DrugDataDTO;
import com.evan.wj.dto.PaginationDTO;
import com.evan.wj.models.Approval;
import com.evan.wj.models.Drug;
import com.evan.wj.models.DrugPrice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DrugService {
    @Autowired
    DrugDAO drugDAO;
    
    @Autowired
    DrugPriceDAO drugPriceDAO;
    
    @Autowired
    ApprovalDAO approvalDAO;
    
    public PaginationDTO<Drug> queryByPage(int page, int limit) {
        List<Drug> drugs = drugDAO.findAll();
        List<Drug> curPageDrugList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < Math.min(limit * page, drugs.size()); i++) {
            curPageDrugList.add(drugs.get(i));
        }
        return new PaginationDTO<>(drugs.size(), curPageDrugList);
    }

    /**
     * 通过药品名称删除药品
     */
    public void delDrugById(int id) {
        drugDAO.deleteById(id);
    }

    /**
     * 获取药品详细信息
     * @param id 药品id
     * @return 药品实例
     */
    public Drug getDrugInfo(int id) {
        return drugDAO.findById(id);
    }

    /**
     * 通过 approvalId 查询 approval信息（含药品信息）
     * @param id approval id
     * @return ApprovalInfoDTO
     */
    public ApprovalInfoDTO getApprovalInfo(int id) {
        ApprovalInfoDTO approvalInfoDTO = new ApprovalInfoDTO();
        Approval approvalInDB = approvalDAO.findById(id);
        Drug drugInDB = drugDAO.findById(approvalInDB.getDrugId());
        approvalInfoDTO.setPicture(drugInDB.getPicture());
        approvalInfoDTO.setFileName(approvalInDB.getApprovalFileName());
        approvalInfoDTO.setDrugName(approvalInDB.getManufacturer());
        approvalInfoDTO.setManufacturer(approvalInDB.getManufacturer());
        approvalInfoDTO.setFileUrl(approvalInDB.getFileUrl());
        approvalInfoDTO.setApprovalNumber(approvalInDB.getApprovalNumber());
        return approvalInfoDTO;
    }
    
    public void addDrug(Drug drug) {
        drugDAO.save(drug);
    }

    /**
     * 通过关键词获得药品列表
     * @param keyword 药名关键词
     * @return 符合查询条件的药品列表
     */
    public List<Drug> fuzzySearchByKeyword(String keyword) {
        return drugDAO.findByNameLike(keyword);
    }

    /**
     * 分页查找药品价格
     * @param page
     * @param limit
     * @return
     */
    public PaginationDTO<DrugPrice> queryDrugPriceByPage(int page, int limit) {
        List<DrugPrice> priceList = drugPriceDAO.findAll();
        List<DrugPrice> curPageDrugPriceList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < Math.min(limit * page, priceList.size()); i++) {
            curPageDrugPriceList.add(priceList.get(i));
        }
        return new PaginationDTO<>(priceList.size(), curPageDrugPriceList);
    }
    
    public void addDrugPrice(DrugPrice drugPrice) {
        drugPriceDAO.save(drugPrice);
    }

    /**
     * 分页查询药品许可证
     * @param page
     * @param limit
     * @return
     */
    public PaginationDTO<Approval> queryApprovalListByPage(int page, int limit) {
        List<Approval> approvalList = approvalDAO.findAll();
        List<Approval> curPageApprovalList = new ArrayList<>();
        for (int i = (page - 1) * limit; i < Math.min(limit * page, approvalList.size()); i++) {
            curPageApprovalList.add(approvalList.get(i));
        }
        return new PaginationDTO<>(approvalList.size(), curPageApprovalList);
    }

    /**
     * 上传药品许可证文件
     * @param approval
     * @param approvalFile
     * @param request
     * @return
     * @throws FileNotFoundException
     */
    public String uploadDrugApproval(Approval approval,
                                     MultipartFile approvalFile,
                                     HttpServletRequest request) throws FileNotFoundException {
        log.info(approvalFile.getOriginalFilename());
        String approvalFileName = approvalFile.getOriginalFilename();
        
        if (approvalFileName == null) {
            return "上传文件为空文件！";
        }
        
        if (!approvalFileName.endsWith(".pdf")) {
            return "请上传 .pdf 许可证文件";
        }
        
        // 文件保存位置
        String realPath = ResourceUtils.getURL("classpath:").getPath() + "static/approval";
        realPath = realPath.substring(1);

        File folder = new File(realPath);
        if (!folder.exists()) {
            boolean ret = folder.mkdirs();
            if (!ret) {
                return "许可证文件存储目录异常！";
            }
        }
        
        String newName;
        // 文件下载时，请求路径中不能包含中文。所以，上传的文件名称不能包含中文，需要重命名
        newName = UUID.randomUUID() + ".pdf";
        
        // 新建许可证文件
        try {
            approvalFile.transferTo(new File(folder, newName));
        } catch (IOException e) {
            return e.toString();
        }
        
        String approvalFileUrl = request.getScheme() + "://" + request.getServerName() + ":" +
                request.getServerPort() + "/approval/" + newName;
        approval.setFileUrl(approvalFileUrl);
        approval.setApprovalFileName(newName);
        
        try {
            approvalDAO.save(approval);    
        } catch (Exception e) {
            return "该药品已登记，请在详情页修改信息！";
        }
        
        // 将文件URL存入数据库
        return approvalFileUrl;
    }

    /**
     * 新增药品许可证记录
     * @param approval
     * @return
     */
    public String addDrugApproval(Approval approval) {
        try {
            Approval approvalInDB = approvalDAO.findByDrugId(approval.getDrugId());
            if (approvalInDB == null) {
                approvalDAO.save(approval);
            } else {
                approvalInDB.setDrugName(approval.getDrugName());
                approvalInDB.setDrugId(approval.getDrugId());
                approvalInDB.setApprovalNumber(approval.getApprovalNumber());
                approvalInDB.setManufacturer(approval.getManufacturer());
                if (!"".equals(approvalInDB.getApprovalNumber()) && 0 != approvalInDB.getDrugId()
                     && !"".equals(approvalInDB.getManufacturer()) && !"".equals(approvalInDB.getApprovalNumber())) {
                    approvalDAO.save(approvalInDB);    
                }
            }
            return "保存成功！";
        } catch (Exception e) {
            return "该药品已登记，请在详情页修改信息！";
        }
    }

    /**
     * 根据 approval id 删除对应药品许可证信息
     * @param id
     */
    public void deleteApprovalById(int id) {
        approvalDAO.deleteById(id);
    }
    
    public void deleteDrugPriceById(int id) {
        drugPriceDAO.deleteById(id);
    }

    public List<DrugDataDTO> DrugDataFuzzySearchByKeyword(String keyword) {
        List<Drug> filterOriginDrugList = drugDAO.findByNameLike(keyword);
        List<DrugDataDTO> drugDataDTOList = new ArrayList<>();
        filterOriginDrugList.parallelStream().forEach(t -> {
            DrugPrice drugPrice = drugPriceDAO.findByDrugId(t.getId());
            DrugDataDTO drugDataDTO = new DrugDataDTO();
            drugDataDTO.setDrugId(t.getId());
            drugDataDTO.setDrugName(t.getName());
            drugDataDTO.setManufacturer(t.getManufacturer());
            drugDataDTO.setDiscount(drugPrice.getDiscount());
            drugDataDTO.setIncomePrice(drugPrice.getIncomePrice());
            drugDataDTO.setSellPrice(drugPrice.getSellPrice());
            drugDataDTO.setPicture(t.getPicture());
            drugDataDTO.setMaxInventoryQuantity(drugPrice.getMaxInventoryQuantity());
            drugDataDTOList.add(drugDataDTO);
        });
        return drugDataDTOList;
    }
}
