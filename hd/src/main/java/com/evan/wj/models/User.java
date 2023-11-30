package com.evan.wj.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity  // 表示这是一个实体类
@Table(name = "user")  // 表示对应的表名为 user
//因为是做前后端分离，而前后端数据交互用的是 json 格式。 那么 User 对象就会被转换为 json 数据。
//  而本项目使用 jpa 来做实体类的持久化，jpa 默认会使用 hibernate, 在 jpa 工作过程中，就会创造代理类来继承 User ，
// 并添加 handler 和 hibernateLazyInitializer 这两个无须 json 化的属性，所以这里需要用 JsonIgnoreProperties 把这两个属性忽略掉
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty(message = "用户名不能为空")
    private String username;

    private String password;

    private String salt;

    /**
     * Real name
     */
    private String name;

    /**
     * Phone Number
     */
    private String phone;

    @Email(message = "请输入正确的邮箱")
    private String email;

    /**
     * User Status
     */
    private boolean enabled;
    
    private String birthday;

    /**
     * 头像链接(url)
     */
    private String avatar;
    
    /**
     * 职业
     */
    private String occupation;

    /**
     * 性别
     */
    private String sex;

    /**
     * 个性签名
     */
    private String personSignature;

    /**
     * 角色名称
     */
    private String rolename;
}
