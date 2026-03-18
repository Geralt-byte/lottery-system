package com.xjtu.types.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author mlei@xjtu
 * @description 前端请求标准返回格式
 * @create 2026/3/19 01:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T> implements Serializable {

    private String code;
    private String info;
    private T data;
}
