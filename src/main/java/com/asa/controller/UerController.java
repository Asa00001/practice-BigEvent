package com.asa.controller;

import com.asa.pojo.Result;
import com.asa.pojo.User;
import com.asa.service.UserService;
import com.asa.utils.JwtUtil;
import com.asa.utils.Md5Util;
import com.asa.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/login")
    public Result<String> login(@Pattern (regexp = "^\\S{5,16}$")String username, @Pattern(regexp = "^\\S{5,16}$") String password){
        //根据用户名查询用户
        User loginUser = userService.findByUserName(username);
        //判断用户是否存在
        if (loginUser == null) {
            return Result.error("用户名错误！");
        }

        //判断密码是否正确
        //查找出的password是密文，需要对参数中的密码进行加密，再与数据库中的密文比较
        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getId());
            claims.put("username", loginUser.getUsername());

            String token = JwtUtil.genToken(claims);
            return Result.success(token);
        }

        return Result.error("密码错误！");
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(/*@RequestHeader("Authorization") String token*/) {
        //根据用户名查询用户
//        Map<String, Object> map = JwtUtil.parseToken(token);
//        String username = (String) map.get("username");

        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String)  map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }
}
