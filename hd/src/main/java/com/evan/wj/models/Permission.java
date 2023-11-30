package com.evan.wj.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "permission")
@ToString
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * permission name
     */
    private String name;

    /**
     * Permission's description
     */
    private String desc_;

    /**
     * The path which trigger permission check.
     */
    private String url;
}
