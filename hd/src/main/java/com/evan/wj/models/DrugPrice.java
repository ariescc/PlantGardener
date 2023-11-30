package com.evan.wj.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "drug_price")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class DrugPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * 药品id
     */
    @Column(name = "drug_id")
    private int drugId;

    /**
     * 药品名称
     */
    @Column(name = "drug_name")
    private String drugName;

    /**
     * 供货商名称
     */
    private String manufacturer;

    /**
     * 采购价格
     */
    @Column(name = "income_price")
    private float incomePrice;

    /**
     * 销售价格
     */
    @Column(name = "sell_price")
    private float sellPrice;

    /**
     * 折扣（0.1~0.9)
     */
    private float discount;

    /**
     * 采购最大库存
     */
    @Column(name = "max_inventory_quantity")
    private int maxInventoryQuantity;

    /**
     * 价格更新日期
     */
    @Column(name = "update_time", columnDefinition = "DATETIME")
    private LocalDateTime updateTime;
}
