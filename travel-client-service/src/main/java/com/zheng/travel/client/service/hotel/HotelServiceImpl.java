package com.zheng.travel.client.service.hotel;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zheng.travel.client.bo.HotelBo;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.entity.Hotel;
import com.zheng.travel.client.mapper.hotel.HotelMapper;
import com.zheng.travel.client.vo.HotelVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HotelServiceImpl extends ServiceImpl<HotelMapper, Hotel> implements IHotelService {

    @Override
    public IPage<HotelBo> searchHotels(HotelVo hotelVo) {
        // 1: 设置分页
        Page<Hotel> hotelPage = new Page<>(hotelVo.getPageNo(), hotelVo.getPageSize());
        // 2:设置条件
        LambdaQueryWrapper<Hotel> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Hotel::getStatus, 1);
        lambdaQueryWrapper.eq(Hotel::getIsdelete, 0);
        // 星级的动态SQL查询
        lambdaQueryWrapper.eq(Vsserts.isNotNull(hotelVo.getStarlevel()), Hotel::getStarlevel, hotelVo.getStarlevel());
        // 根据品牌搜索
        lambdaQueryWrapper.eq(Vsserts.isNotNull(hotelVo.getBrandId()), Hotel::getHotelBrandId, hotelVo.getBrandId());
        // 根据分类搜索
        lambdaQueryWrapper.eq(Vsserts.isNotNull(hotelVo.getCategoryId()), Hotel::getHotelCategoryId, hotelVo.getCategoryId());
        // 搜索城市编号
        lambdaQueryWrapper.eq(Vsserts.isNotEmpty(hotelVo.getAreacode()), Hotel::getAreacode, hotelVo.getAreacode());
        // 其他的就在这里进行范围查询 -- 包含头和包含尾
        //lambdaQueryWrapper.between(Vsserts.isNotNull(hotelVo.getStartPrice()) &&  Vsserts.isNotNull(hotelVo.getEndPrice()) &&  hotelVo.getStartPrice() >=100 && hotelVo.getEndPrice() <=500,Hotel::getRealprice, hotelVo.getStartPrice(),hotelVo.getEndPrice());
        // 其他的就在这里进行范围查询 -- 包含头和不包含尾
        // 0 >=price<100 100>=price<200 .... 400>=price<600
        if (Vsserts.isNotNull(hotelVo.getStartPrice()) && Vsserts.isNotNull(hotelVo.getEndPrice())) {
            lambdaQueryWrapper.ge(Hotel::getRealprice, hotelVo.getStartPrice());
            lambdaQueryWrapper.lt(Hotel::getRealprice, hotelVo.getEndPrice());
        }
        // price>=600 以上
        if (Vsserts.isNotNull(hotelVo.getStartPrice()) && Vsserts.isNull(hotelVo.getEndPrice())) {
            lambdaQueryWrapper.ge(Hotel::getRealprice, hotelVo.getStartPrice());
        }
        // 关键字搜索
        lambdaQueryWrapper.like(Vsserts.isNotEmpty(hotelVo.getKeyword()), Hotel::getTitle, hotelVo.getKeyword());
        // 3: 排序
        lambdaQueryWrapper.orderByDesc(Hotel::getCreateTime);
        // 4: 查询分页
        Page<Hotel> page = this.page(hotelPage, lambdaQueryWrapper);

        return tranferPageBo(page, HotelBo.class);
    }

    @Override
    public HotelBo getHotels(Long hotelId) {
        return tranferBo(this.getById(hotelId), HotelBo.class);
    }

}
