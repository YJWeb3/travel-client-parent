package com.zheng.travel.client.controller.hotel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zheng.travel.client.service.hotel.collect.IHotelUserCollectService;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.controller.APIBaseController;
import com.zheng.travel.client.entity.HotelUserCollect;
import com.zheng.travel.client.entity.TravelUser;
import com.zheng.travel.client.threadlocal.UserThrealLocal;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = "酒店关联管理")
public class HotelRelationController extends APIBaseController {

    private final IHotelUserCollectService hotelUserCollectService;


    /**
     * 收藏酒店
     *
     * @param hotelId
     * @return
     */
    @PostMapping("/hotel/collect/{id}")
    @ApiOperation("酒店收藏")
    public boolean collectHotel(@PathVariable("id") String hotelId) {
        Vsserts.isEmptyEx(hotelId, "数据不能为空!!!");
        TravelUser TravelUser = UserThrealLocal.get();
        LambdaQueryWrapper<HotelUserCollect> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HotelUserCollect::getUserid, TravelUser.getId());
        lambdaQueryWrapper.eq(HotelUserCollect::getHotelid, new Long(hotelId));
        HotelUserCollect hotelUserCollect = hotelUserCollectService.getOne(lambdaQueryWrapper);
        if (Vsserts.isNotNull(hotelUserCollect)) {
            hotelUserCollect.setStatus(1);
            hotelUserCollectService.updateById(hotelUserCollect);
        } else {
            hotelUserCollect = new HotelUserCollect();
            hotelUserCollect.setStatus(1);
            hotelUserCollect.setUserid(TravelUser.getId());
            hotelUserCollect.setHotelid(new Long(hotelId));
            hotelUserCollectService.saveOrUpdate(hotelUserCollect);
        }
        // 这里hotel的收藏数+1
        redisOperator.increment("hotel:collect:total:"+hotelId,1);
        return true;
    }


    /**
     * 取消收藏
     *
     * @param hotelId
     * @return
     */
    @PostMapping("/hotel/uncollect")
    @ApiOperation("酒店取消收藏")
    public boolean uncollectHotel(@PathVariable("id") String hotelId) {
        Vsserts.isEmptyEx(hotelId, "数据不能为空!!!");
        TravelUser TravelUser = UserThrealLocal.get();
        HotelUserCollect hotelUserCollect = new HotelUserCollect();
        hotelUserCollect.setStatus(0);
        UpdateWrapper<HotelUserCollect> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("hotelid", new Long(hotelId));
        updateWrapper.set("userid", TravelUser.getId());
        hotelUserCollectService.update(hotelUserCollect, updateWrapper);
        // 这里hotel的收藏数 - 1
        redisOperator.decrement("hotel:collect:total:"+hotelId,1);
        return true;
    }

}
