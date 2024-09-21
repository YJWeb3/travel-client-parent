package com.zheng.travel.client.mapper.hotel;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zheng.travel.client.entity.Hotel;
import org.apache.ibatis.annotations.Param;

public interface HotelMapper extends BaseMapper<Hotel> {


    /**
     * 查询用户收藏的酒店并分页
     *
     * @param page
     * @param wrapper
     * @return
     */
    Page<Hotel> findUserFavHotelsPage(IPage<Hotel> page, @Param(Constants.WRAPPER) Wrapper<Hotel> wrapper);

    /*
     * @Author 徐柯
     * @Description 收藏数+1
     * @Date 21:03 2022/4/26
     * @Param [hotelId]
     * @return int
     **/
    int updateAddCollections(@Param("hotelId") Long hotelId);

    /**
     * 收藏数 - 1
     *
     * @param hotelId
     * @return
     */
    int updateMinusCollections(@Param("hotelId") Long hotelId);

}
