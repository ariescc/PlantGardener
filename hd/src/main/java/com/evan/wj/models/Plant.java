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
@Table(name = "plant")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Plant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * 植物名称
     */
    private String name;

    /**
     * 植物图片
     */
    private String picture;

    /**
     * 科属
     */
    @Column(name = "plant_type")
    private String plantType;

    /**
     * 描述
     */
    private String description;

    /**
     * 生长环境
     */
    private String environment;
}
