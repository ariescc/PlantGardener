package com.evan.wj.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 动态文章
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity
@Table(name = "article")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    /**
     * 文章题目
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 发布日期
     */
    @Column(name = "publish_date", columnDefinition = "DATETIME")
    private LocalDateTime publishDate;
    
    /**
     * 文章内容
     */
    @Lob
    private String content;
}
