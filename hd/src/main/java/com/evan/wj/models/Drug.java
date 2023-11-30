package com.evan.wj.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "drug_source")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Drug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    private String name;

    /**
     * 生产厂商
     */
    private String manufacturer;

    /**
     * 药物剂型
     */
    @Column(name = "dosage_form")
    private String dosageForm;

    /**
     * 药品规格
     */
    private String specification;

    /**
     * 功效
     */
    private String efficacy;

    /**
     * 用法
     */
    @Column(length = 512, name = "usage_method")
    private String usageMethod;

    /**
     * 
     */
    @Column(length = 512, name = "adverse_reaction")
    private String adverseReaction;

    /**
     * 药品图片
     */
    private String picture;

    /**
     * 药品别名
     */
    @Column(name = "another_name")
    private String anotherName;

    /**
     * 批准文号
     */
    @Column(name = "approval_number")
    private String approvalNumber;

    /**
     * 参考价格
     */
    @Column(name = "reference_price", precision = 2)
    private Float referencePrice;
}
