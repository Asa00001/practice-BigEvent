package com.asa.controller;

import com.asa.pojo.Result;
import com.asa.pojo.User;
import com.asa.service.UserService;
import com.asa.utils.JwtUtil;
import com.asa.utils.Md5Util;
import com.asa.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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

    @PutMapping("/update")
    public Result update(@RequestBody@Validated User user) {
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam@URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params) {
        //校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Result.error("缺少必要的参数！");
        }

        //原密码是否正确
        //调用userService，根据用户名拿到原密码，跟输入值比对
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String)map.get("username");
        User loginUser = userService.findByUserName(username);
        if(!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            return Result.error("原密码填写不正确！");
        }

        //newPwd与rePwd是否相同
        if(!rePwd.equals(newPwd)) {
            return Result.error("两次填写的新密码不一致！");
        }

        //调用service完成密码更新
        userService.updatePwd(newPwd);
        return Result.success();
    }
}
