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
@Table(name = "dishes")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Dishes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int Id;

    /**
     * 菜品名称
     */
    private String name;

    /**
     * 价格
     */
    private double price;

    /**
     * 销量
     */
    private int saleVolume;
}
