package com.asa.service;

import com.asa.pojo.Article;
import com.asa.pojo.PageBean;
import com.asa.pojo.Result;
import jakarta.validation.constraints.NotNull;

public interface ArticleService {
    //新增文章
    void add(Article article);

    //条件分页列表查询
    PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state);

    //查看文章详情
    Article detail(@NotNull Integer id);

    //更新文章
    void update(Article article);

    // 删除文章
    Result delete(@NotNull Integer id);
}
