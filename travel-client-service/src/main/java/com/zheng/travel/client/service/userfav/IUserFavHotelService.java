package com.zheng.travel.client.service.userfav;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zheng.travel.client.entity.Hotel;
import com.zheng.travel.client.entity.UserFavHotel;
import com.zheng.travel.client.vo.UserHotelFavSearchVo;
import com.zheng.travel.client.vo.UserHotelFavVo;

public interface IUserFavHotelService extends IService<UserFavHotel> {


    /**
     * 查询用户的收藏的酒店信息
     * @param userHotelFavSearchVo
     * @return
     */
    Page<Hotel> findUserFavHotelsPage(UserHotelFavSearchVo userHotelFavSearchVo);


    /**
     * 判断用户是否收藏过此酒店
     * @return
     */
    boolean isUserCollect(Long hotelId);


    /**
     * 用户收藏的酒店信息
     *
     * @param userHotelFavVo
     * @return
     */
    boolean savaUserFavHotel(UserHotelFavVo userHotelFavVo);


    /**
     * 取消收藏的酒店信息
     *
     * @param userHotelFavVo
     * @return
     */

    boolean removeUserFavHotel(UserHotelFavVo userHotelFavVo);
}
