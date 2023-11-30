package com.evan.wj.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "menu")
@ToString
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    private int roleId;
    
    /**
     * menu access path
     */
    private String path;

    /**
     * menu's name
     */
    private String name;

    /**
     * Menu's name in Chinese
     */
    private String nameZh;

    /**
     * Menu icon class (ElementUI)
     */
    private String iconCls;

    /**
     * Front-end component name corresponding to Menu
     */
    private String component;

    /**
     * Parent Menu
     */
    private int parentId;

    /**
     * Children Menu
     */
    @Transient
    private List<Menu> children;
}
