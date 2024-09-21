package com.zheng.travel.client.service.hotel;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zheng.travel.client.bo.HotelBo;
import com.zheng.travel.client.service.BaseService;
import com.zheng.travel.client.entity.Hotel;
import com.zheng.travel.client.vo.HotelVo;

public interface IHotelService extends IService<Hotel> , BaseService {


    /**
     * 查询和分页对应的酒店信息
     * @param hotelVo
     * @return
     */
    IPage<HotelBo> searchHotels(HotelVo hotelVo);


    /**
     * 查询和分页对应的酒店信息
     * @param hotelId
     * @return
     */
    HotelBo getHotels(Long  hotelId);

}
