package com.evan.wj.models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table
public class SaleOrder {
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
     * 销售数量
     */
    private int quantity;

    /**
     * 销售单价
     */
    private float sellPrice;

    /**
     * 折扣
     */
    private float discount;

    /**
     * 总金额
     */
    private float totalAmount;

    /**
     * 采购用户
     */
    @Column(name = "purchase_user")
    private String purchaseUser;

    /**
     * 销售日期
     */
    @Column(name = "sell_date", columnDefinition = "DATETIME")
    private LocalDateTime sellDate;

    /**
     * 销售人员
     */
    @Column(name = "sale_stuff")
    private String saleStuff;

    /**
     * 销售人员id
     */
    @Column(name = "sale_stuff_id")
    private int saleStuffId;
}
