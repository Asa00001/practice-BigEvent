package com.asa.service.impl;

import com.asa.mapper.ArticleMapper;
import com.asa.pojo.Article;
import com.asa.pojo.PageBean;
import com.asa.pojo.Result;
import com.asa.service.ArticleService;
import com.asa.utils.ThreadLocalUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public void add(Article article) {
        //补充属性值
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        article.setCreateUser(userId);

        articleMapper.add(article);
    }

    @Override
    public PageBean<Article> list(Integer pageNum, Integer pageSize, Integer categoryId, String state) {
        //创建pageBean对象
        PageBean<Article> pb = new PageBean<>();

        //开启分页查询PageHelper
        PageHelper.startPage(pageNum, pageSize);

        //调用Mapper
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        List<Article> as = articleMapper.list(userId, categoryId, state);
        //page中提供了方法，可以获取pagehelper分页查询后得到的总分页条数和当前页数据
        Page<Article> p = (Page<Article>) as;

        //将数据填充进pageBean中
        pb.setTotal(p.getTotal());
        pb.setItems(p.getResult());
        return pb;
    }

    @Override
    public Article detail(Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        return articleMapper.detail(id, userId);
    }

    @Override
    public void update(Article article) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.update(article, userId);
    }

    @Override
    public Result delete(Integer id) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        int row = articleMapper.delete(id, userId);
        if(row == 0) {
            return Result.error("文章不存在！");
        }
        return Result.success();
    }
}
