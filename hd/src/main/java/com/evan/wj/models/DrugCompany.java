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
@Table(name = "drug_company")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class DrugCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * 药企名称
     */
    private String name;

    /**
     * 药企地址
     */
    private String address;

    /**
     * 邮政编码
     */
    private String postcode;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 企业邮件
     */
    private String email;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 网址
     */
    private String website;
}
