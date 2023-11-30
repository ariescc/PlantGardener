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
@Table(name = "drug_inventory")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class DrugInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "drug_name")
    private String drugName;
    
    @Column(name = "drug_id")
    private int drugId;

    /**
     * 供货商
     */
    private String manufacturer;

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
     * 库存数量
     */
    @Column(name = "inventory_quantity")
    private int inventoryQuantity;

    /**
     * 最大库存
     */
    @Column(name = "max_inventory")
    private int maxInventoryQuantity;

    /**
     * 最新出库时间
     */
    @Column(name = "update_time", columnDefinition = "DATETIME")
    private LocalDateTime updateTime;

    /**
     * 审批状态
     */
    @Column(name = "approval_status")
    private float approvalStatus;
}
