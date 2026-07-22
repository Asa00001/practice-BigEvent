package com.asa.controller;

import com.asa.pojo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @GetMapping("list")
    public Result<String> list(/*@RequestHeader("Authorization") String token, HttpServletResponse response*/) {
//        //验证token
//        try {
//            Map<String, Object> claims = JwtUtil.parseToken(token);
//            return Result.success("全部文章数据……");
//        } catch (Exception e) {
//            //HTTP响应状态码为401
//            response.setStatus(401);
//            return Result.error("未登陆！");
//        }
        return Result.success("全部文章数据……");
    }
}
