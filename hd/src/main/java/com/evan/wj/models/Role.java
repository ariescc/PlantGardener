package com.evan.wj.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "role")
@ToString
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * Role name
     */
    private String name;

    /**
     * Role name in Chinese
     */
    @Column(name = "name_zh")
    private String nameZh;

    /**
     * Role status
     */
    private boolean enabled;

    /**
     * Permissions owned by current role.
     */
    @Transient
    private List<Permission> perms;

    /**
     *  Menus owned by current role.
     */
    @Transient
    private List<Menu> menus;
}
