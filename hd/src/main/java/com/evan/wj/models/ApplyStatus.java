package com.evan.wj.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

/**
 * 求职状态
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "apply_status")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class ApplyStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * 用户id
     */
    private int uid;

    /**
     * 招聘信息id
     */
    private int rid;

    /**
     * 求职状态
     * 0: 被拒绝
     * 1: 已通过审核
     * 2: 申请中
     */
    private int status;
}
