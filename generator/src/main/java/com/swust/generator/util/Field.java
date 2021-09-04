package com.swust.generator.util;


import lombok.Data;

@Data
public class Field {
    private String name;//字段
    private String nameHump;//字段名小驼峰
    private String nameBigHump;//字段名大驼峰
    private String nameCn;//中文名
    private String type;//字段类型 char(1)
    private String javaType;//java类型
    private String comment;//注释|id
    private Boolean nullAble;//是否可以为空
    private Integer length;//字符串长度


}
