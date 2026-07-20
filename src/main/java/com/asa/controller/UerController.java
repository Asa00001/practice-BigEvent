package com.asa.controller;

import com.asa.pojo.Result;
import com.asa.pojo.User;
import com.asa.service.UserService;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
public class UerController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$")String username, @Pattern(regexp = "^\\S{5,16}$")String password) {
            //查询用户（确认用户名是否被占用）
            // Validation 已经保证用户名格式正确，这里无需再次校验
            User u = userService.findByUserName(username);
            if (u ==  null) {
                //没有被占用
                //注册
                userService.register(username, password);
                return Result.success();
            } else {
                //用户名被占用
                return Result.error("用户名已被占用！");
            }
    }
}
