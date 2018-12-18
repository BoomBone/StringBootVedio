package com.em.api.controller;

import com.em.pojo.Users;
import com.em.service.UserService;
import com.em.utils.IMoocJSONResult;
import com.em.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 */
@RestController
@Api(value = "用户注册登录的接口", tags = {"注册和登录"})
public class RegisterLoginController {

    @Autowired
    private UserService userService;

    /**
     * @param user
     * @return 注册
     * @throws Exception
     */
    @ApiOperation(value = "用于注册", notes = "用户注册的接口")
    @PostMapping("/register")
    public IMoocJSONResult register(@RequestBody Users user) throws Exception {
        //1.判断用户名密码必须不为空
        if (StringUtils.isBlank(user.getUsername()) ||
                StringUtils.isBlank(user.getPassword())) {
            return IMoocJSONResult.errorMsg("用户名和密码不能为空");
        }
        //2.判断用户名是否存在
        boolean userNameIsExist = userService.queryUsernameIsExist(user.getUsername());

        //3.保存用户，注册信息
        if (!userNameIsExist) {
            user.setNickname(user.getUsername());
            user.setPassword(MD5Utils.getMD5Str(user.getPassword()));
            user.setFansCounts(0);
            user.setReceiveLikeCounts(0);
            userService.saveUser(user);

        } else {
            return IMoocJSONResult.errorMsg("用户名已经存在");
        }
        return IMoocJSONResult.ok();
    }

    @ApiOperation(value = "用于登录", notes = "用户登录的接口")
    @PostMapping("/login")
    public IMoocJSONResult login(@RequestBody Users user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();
        //1.用户名和密码不为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return IMoocJSONResult.errorMsg("用户名和密码不能为空");
        }
        //2.判断用户是否存在
        Users userResult = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));

        //3.返回参数
        if (userResult != null) {
            userResult.setPassword("");
            return IMoocJSONResult.ok(userResult);
        } else {
            return IMoocJSONResult.errorMsg("用户名或密码不正确，请重试");
        }
    }
}
