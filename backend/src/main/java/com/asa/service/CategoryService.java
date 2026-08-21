package com.asa.service;

import com.asa.pojo.Category;
import com.asa.pojo.Result;

import java.util.List;

public interface CategoryService {
    //新增分类
    void add(Category category);

    //列表查询
    List<Category> list();

    //根据id查询分类信息
    Category findById(Integer id);

    //更新分类
    void update(Category category);

    //删除指定分类
    Result delete(Integer id);
}
