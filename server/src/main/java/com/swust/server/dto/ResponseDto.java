package com.swust.server.dto;

import lombok.Data;

/**
 * @author 雪瞳
 * @Slogan 忘时，忘物，忘我。
 * @Function
 * 统一返回接口信息
 */
@Data
public class ResponseDto<T> {
    //业务成功标识
    private boolean success = true;
    // 返回码
    private String code;
    // 返回信息
    private String message;
    // 泛型数据
    private T content;
}
