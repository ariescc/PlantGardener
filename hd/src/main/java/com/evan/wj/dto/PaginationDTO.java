package com.evan.wj.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaginationDTO<T> {
    /**
     * 页码
     */
    private int total;
    
    private List<T> items;
}
