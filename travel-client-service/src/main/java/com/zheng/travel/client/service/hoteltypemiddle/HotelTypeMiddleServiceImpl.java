package com.zheng.travel.client.service.hoteltypemiddle;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zheng.travel.client.bo.HotelTypeMiddleBo;
import com.zheng.travel.client.enums.StatusEnum;
import com.zheng.travel.client.service.BaseService;
import com.zheng.travel.client.entity.HotelTypeMiddle;
import com.zheng.travel.client.mapper.hotel.HotelTypeMiddleMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class HotelTypeMiddleServiceImpl extends ServiceImpl<HotelTypeMiddleMapper, HotelTypeMiddle>
        implements IHotelTypeMiddleService, BaseService {


    /**
     * 根据酒店ID查询对应的房型列表信息
     *
     * @param hotelId
     * @return
     */
    @Override
    public List<HotelTypeMiddle> findHotelTypeMiddle(Long hotelId) {
        LambdaQueryWrapper<HotelTypeMiddle> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HotelTypeMiddle::getHotelId, hotelId);
        lambdaQueryWrapper.eq(HotelTypeMiddle::getStatus, StatusEnum.YES.getValue());
        lambdaQueryWrapper.orderByAsc(HotelTypeMiddle::getSorted);
        return this.list(lambdaQueryWrapper);
    }

    @Override
    public HotelTypeMiddleBo getHotelTypeById(Long hotelTypeId) {
        return tranferBo(this.getById(hotelTypeId), HotelTypeMiddleBo.class);
    }
}
