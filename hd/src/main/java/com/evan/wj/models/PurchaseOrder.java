package com.evan.wj.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 采购订单
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "purchase_order")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class PurchaseOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "drug_name")
    private String drugName;
    
    @Column(name = "drug_id")
    private int drugId;

    /**
     * 药品采购数量
     */
    private int quantity;

    /**
     * 生产日期
     */
    @Column(name = "production_date", columnDefinition = "DATETIME")
    private LocalDateTime productionDate;

    /**
     * 保质期（天数）
     */
    @Column(name = "shelf_life")
    private int shelfLife;
    
    /**
     * 供货商
     */
    private String manufacturer;

    /**
     * 采购价格
     */
    @Column(name = "income_price")
    private String incomePrice;

    /**
     * 总金额
     */
    @Column(name = "total_amount")
    private int totalAmount;

    /**
     * 采购日期
     */
    @Column(name = "purchase_time", columnDefinition = "DATETIME")
    private LocalDateTime purchaseTime;

    /**
     * 采购责任人
     */
    @Column(name = "person_in_charge")
    private String personInCharge;

    /**
     * 订单审批状态
     */
    @Column(name = "approvel_status")
    private int approvelStatus;
}
