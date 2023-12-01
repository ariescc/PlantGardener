package com.evan.wj.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

/**
 * 应聘人员信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "applicant")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Applicant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 教育背景
     */
    @Column(name = "edu_background")
    private String eduBackground;

    /**
     * 期望薪资
     */
    @Column(name = "forward_reward")
    private float forwardReward;

    /**
     * 期望工作地点
     */
    private String address;

    /**
     * 工作年限
     */
    @Column(name = "work_years")
    private int workYears;
}
