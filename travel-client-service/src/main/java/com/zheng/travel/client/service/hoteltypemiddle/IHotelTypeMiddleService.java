package com.zheng.travel.client.service.hoteltypemiddle;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zheng.travel.client.bo.HotelTypeMiddleBo;
import com.zheng.travel.client.entity.HotelTypeMiddle;

import java.util.List;

public interface IHotelTypeMiddleService extends IService<HotelTypeMiddle> {

    /**
     * 根据酒店ID查询对应的房型列表信息
     * @param hotelId
     * @return
     */
    List<HotelTypeMiddle> findHotelTypeMiddle(Long hotelId);


    /**
     * 根据房型ID查询酒店房型信息
     * @param hotelTypeId
     * @return
     */
    HotelTypeMiddleBo getHotelTypeById(Long hotelTypeId);
}
