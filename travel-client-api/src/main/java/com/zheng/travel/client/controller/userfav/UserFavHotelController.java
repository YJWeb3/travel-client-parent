package com.zheng.travel.client.controller.userfav;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zheng.travel.client.anno.login.LoginCheck;
import com.zheng.travel.client.controller.APIBaseController;
import com.zheng.travel.client.service.userfav.IUserFavHotelService;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.entity.Hotel;
import com.zheng.travel.client.vo.UserHotelFavSearchVo;
import com.zheng.travel.client.vo.UserHotelFavVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@LoginCheck
@Api(tags = "用户酒店收藏")
public class UserFavHotelController extends APIBaseController {

    @Autowired
    private IUserFavHotelService userFavHotelService;


    /**
     * 查询用户的收藏的酒店信息
     *
     * @param userHotelFavSearchVo
     * @return
     */
    @PostMapping("/userfav/hotel/load")
    public Page<Hotel> findUserFavHotelsPage(@RequestBody UserHotelFavSearchVo userHotelFavSearchVo) {
        return userFavHotelService.findUserFavHotelsPage(userHotelFavSearchVo);
    }

    /**
     * 用户收藏酒店
     *
     * @param userHotelFavVo
     * @return
     */
    @ApiOperation("用户收藏酒店")
    @PostMapping("/userfav/hotel/save")
    public boolean savaUserFavHotel(@RequestBody UserHotelFavVo userHotelFavVo) {
        Vsserts.isNullEx(userHotelFavVo.getHotelId(), "查无此酒店信息");
        return userFavHotelService.savaUserFavHotel(userHotelFavVo);
    }

    /**
     * 用户取消收藏
     *
     * @param userHotelFavVo
     * @return
     */
    @ApiOperation("用户取消收藏酒店")
    @PostMapping("/userfav/hotel/remove")
    public boolean removeUserFavHotel(@RequestBody UserHotelFavVo userHotelFavVo) {
        Vsserts.isNullEx(userHotelFavVo.getHotelId(), "查无此酒店信息");
        return userFavHotelService.removeUserFavHotel(userHotelFavVo);
    }

}
