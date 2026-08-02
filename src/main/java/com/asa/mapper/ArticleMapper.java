package com.asa.mapper;

import com.asa.pojo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {
    //新增
    @Insert("insert into article(title,content,cover_img,state,category_id,create_user,create_time,update_time) " +
            "values(#{title},#{content},#{coverImg},#{state},#{categoryId},#{createUser},#{createTime},#{updateTime})")
    void add(Article article);

    //查询
    List<Article> list(Integer userId, Integer categoryId, String state);

    //查找
    @Select("select * from article where id = #{id} and create_user = #{userId}")
    Article detail(Integer id, Integer userId);

    //更新
    @Update("update article set title = #{article.title},content = #{article.content},cover_img = #{article.coverImg}," +
            "state = #{article.state},category_id = #{article.categoryId},update_time = #{article.updateTime} " +
            "where id = #{article.id} and create_user = #{userId}")
    void update(@Param("article") Article article, @Param("userId") Integer userId);

    //删除
    @Delete("delete from article where id = #{id} and create_user = #{userId}")
    int delete(Integer id,  Integer userId);
}
