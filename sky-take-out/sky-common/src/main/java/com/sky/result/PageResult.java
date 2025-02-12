package com.sky.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 封装分页查询结果
 */
@Data
//生成一个包含所有类字段的构造函数
@AllArgsConstructor
//生成一个无参构造函数
@NoArgsConstructor
//实现Serializable接口可以使得对象的状态能够被序列化和反序列化
public class PageResult implements Serializable {

    private long total; //总记录数

    private List records; //当前页数据集合

}
