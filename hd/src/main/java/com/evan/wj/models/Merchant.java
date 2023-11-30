package com.evan.wj.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "merchant")
@JsonIgnoreProperties({"handler", "hibernatLazyInitializer"})
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @NotEmpty(message = "商户名称不能为空")
    private String name;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 店铺地址
     */
    private String address;

    /**
     * 营业时间
     */
    private String businessHours;

    /**
     * 人均消费
     */
    private int perConsumption;
}
