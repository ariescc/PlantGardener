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
@Table(name = "user_address")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * 用户 id
     */
    private int uid;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 邮编
     */
    private String postcode;

    /**
     * 门牌号
     */
    private String houseNumber;

    /**
     * 联系人
     */
    private String contactPerson;

    /**
     * 标签(家、公司、学校)
     */
    private String tag;
}
