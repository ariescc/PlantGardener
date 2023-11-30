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
@Table(name = "supplier")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * 联系人
     */
    private String connectperson;

    /**
     * 资质
     */
    private String certification;

    /**
     * 联系方式
     */
    private String phone;

    /**
     * 供货商企业名称
     */
    private String name;
}
