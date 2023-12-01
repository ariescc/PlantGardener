package com.evan.wj.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

/**
 * 招聘信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "recruitment")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Recruitment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * 岗位名称
     */
    @Column(name = "post_name")
    private String postName;
    
    /**
     * 植物名称
     */
    @Column(name = "plant_name")
    private String plantName;

    /**
     * 植物 id
     */
    @Column(name = "plant_id")
    private int plantId;

    /**
     * 期望性别
     */
    private String sex;

    /**
     * 期望工作年限
     */
    @Column(name = "work_years")
    private int workYears;

    /**
     * 报酬
     */
    private float reward;

    /**
     * 工作地点
     */
    @Column(name = "work_address")
    private String workAddress;
}
