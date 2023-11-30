package com.evan.wj.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DrugDataDTO {
    private int drugId;
    private String drugName;
    private String manufacturer;
    private int maxInventoryQuantity;
    private float incomePrice;
    private float sellPrice;
    private float discount;
    private String picture;
}
