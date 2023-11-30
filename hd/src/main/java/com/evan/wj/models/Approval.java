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
@Table(name = "drug_approval")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Approval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * 药品名称
     */
    private String drugName;

    /**
     * 药品 ID
     */
    @Column(unique = true)
    private int drugId;

    /**
     * 生产厂商
     */
    private String manufacturer;

    /**
     * 批准文号
     */
    private String approvalNumber;

    /**
     * 文件路径
     */
    private String fileUrl;

    /**
     * 药品许可证文件名称
     */
    @Column(name = "approval_filename")
    private String approvalFileName;
}
