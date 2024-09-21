package com.zheng.travel.client.controller.user;

import com.zheng.travel.client.service.user.IUserService;
import com.zheng.travel.client.entity.TravelUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户模块")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @ApiImplicitParam(name = "id",value = "用户主键",required = true)
    @ApiOperation(value = "根据用户ID获取用户数据信息")
    @GetMapping("/getuser/{id}")
    public TravelUser getUser(@PathVariable("id")Long id){
        return userService.getById(id);
    }

}
