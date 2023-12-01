package com.evan.wj.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

/**
 * 管家信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "butlar")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Butlar {
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
     * 月薪
     */
    @Column(name = "monthly_reward")
    private float monthlyReward;

    /**
     * 照看植物类别
     */
    @Column(name = "plant_id")
    private int plantId;

    /**
     * 工作地点
     */
    @Column(name = "work_address")
    private String workAddress;
}
