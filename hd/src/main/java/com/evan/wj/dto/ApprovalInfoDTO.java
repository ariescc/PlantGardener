package com.evan.wj.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 许可证信息前后端传递
 */
@Data
@ToString
public class ApprovalInfoDTO {
    private int drugId;
    private String drugName;
    private String manufacturer;
    private String picture;
    private String fileName;
    private String fileUrl;
    private String approvalNumber;
}