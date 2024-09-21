package com.zheng.travel.client.service.userfav;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zheng.travel.client.result.ResultEnum;
import com.zheng.travel.client.result.ex.TravelValidationException;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.entity.Hotel;
import com.zheng.travel.client.entity.TravelUser;
import com.zheng.travel.client.entity.UserFavHotel;
import com.zheng.travel.client.mapper.hotel.HotelMapper;
import com.zheng.travel.client.mapper.userfav.UserFavHotelMapper;
import com.zheng.travel.client.threadlocal.UserThrealLocal;
import com.zheng.travel.client.vo.UserHotelFavSearchVo;
import com.zheng.travel.client.vo.UserHotelFavVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class UserFavHotelServiceImpl extends ServiceImpl<UserFavHotelMapper, UserFavHotel> implements IUserFavHotelService {


    @Autowired
    private HotelMapper hotelMapper;


    /**
     * 查询用户的收藏的酒店信息
     *
     * @param userHotelFavSearchVo
     * @return
     */
    @Override
    public Page<Hotel> findUserFavHotelsPage(UserHotelFavSearchVo userHotelFavSearchVo) {
        TravelUser TravelUser = UserThrealLocal.get();
        // 设置分页
        IPage<Hotel> hotelIPage = new Page<>(userHotelFavSearchVo.getPageNo(), userHotelFavSearchVo.getPageSize());
        // 设置条件   kss_user_fav_hotel t1   kss_hotel t2
        QueryWrapper queryWrapper = new QueryWrapper();//where
        queryWrapper.eq("t2.status", 1);// t2.status = 1
        queryWrapper.eq("t2.isdelete", 0);// and t2.isdelete = 0
        queryWrapper.eq("t1.user_id", TravelUser.getId());// and t1.user_id = 1
        queryWrapper.like(Vsserts.isNotEmpty(userHotelFavSearchVo.getKeyword()), "t2.title", userHotelFavSearchVo.getKeyword());//and t2.title like '%x%'
        queryWrapper.orderByDesc("t1.create_time");//order by t1.create_time desc
        // 分页返回
        return this.hotelMapper.findUserFavHotelsPage(hotelIPage, queryWrapper);
    }


    /**
     * 判断用户是否收藏过此酒店
     *
     * @param hotelId
     * @return
     */
    @Override
    public boolean isUserCollect(Long hotelId) {
        // 1: 获取用户id
        TravelUser TravelUser = UserThrealLocal.get();
        if (TravelUser == null) {
            throw new TravelValidationException(ResultEnum.LOGIN_FAIL);
        }
        // 2: 根据用户id和酒店id查询是否收藏
        LambdaQueryWrapper<UserFavHotel> lambdaQueryWrapper = new LambdaQueryWrapper<UserFavHotel>();
        lambdaQueryWrapper.eq(UserFavHotel::getUserId, TravelUser.getId());
        lambdaQueryWrapper.eq(UserFavHotel::getHotelId, hotelId);
        // 3: 如果没收藏，就添加用户和酒店的收藏关系
        long count = this.count(lambdaQueryWrapper);
        boolean flag = count > 0;
        return flag;
    }


    /**
     * 用户收藏的酒店信息
     *
     * @param userHotelFavVo
     * @return
     */
    @Override
    @Transactional
    public boolean savaUserFavHotel(UserHotelFavVo userHotelFavVo) {
        // 1: 获取用户id
        TravelUser TravelUser = UserThrealLocal.get();
        if (TravelUser == null) {
            throw new TravelValidationException(ResultEnum.LOGIN_FAIL);
        }
        // 2: 根据用户id和酒店id查询是否收藏
        LambdaQueryWrapper<UserFavHotel> lambdaQueryWrapper = new LambdaQueryWrapper<UserFavHotel>();
        lambdaQueryWrapper.eq(UserFavHotel::getUserId, TravelUser.getId());
        lambdaQueryWrapper.eq(UserFavHotel::getHotelId, userHotelFavVo.getHotelId());
        // 3: 如果没收藏，就添加用户和酒店的收藏关系
        long count = this.count(lambdaQueryWrapper);
        if (count == 0) {
            UserFavHotel userFavHotel = new UserFavHotel();
            userFavHotel.setUserId(TravelUser.getId());
            userFavHotel.setHotelId(userHotelFavVo.getHotelId());
            boolean b = this.saveOrUpdate(userFavHotel);
            if (b) {
                // 自增1
                this.hotelMapper.updateAddCollections(userHotelFavVo.getHotelId());
            }
            return b;

        }
        // 4: 如果已经收藏直接返回
        return false;
    }


    /**
     * 取消收藏的酒店信息
     *
     * @param userHotelFavVo
     * @return
     */
    @Override
    public boolean removeUserFavHotel(UserHotelFavVo userHotelFavVo) {
        // 1: 获取用户id
        TravelUser TravelUser = UserThrealLocal.get();
        if (TravelUser == null) {
            throw new TravelValidationException(ResultEnum.LOGIN_FAIL);
        }
        // 2: 根据用户id和酒店id查询是否收藏
        LambdaQueryWrapper<UserFavHotel> lambdaQueryWrapper = new LambdaQueryWrapper<UserFavHotel>();
        lambdaQueryWrapper.eq(UserFavHotel::getUserId, TravelUser.getId());
        lambdaQueryWrapper.eq(UserFavHotel::getHotelId, userHotelFavVo.getHotelId());
        // 3: 如果没收藏，就添加用户和酒店的收藏关系
        long count = this.count(lambdaQueryWrapper);
        if (count > 0) {
            boolean flag = this.remove(lambdaQueryWrapper);
            if (flag) {
                // 自增1
                this.hotelMapper.updateMinusCollections(userHotelFavVo.getHotelId());
            }
            return flag;
        }
        // 4: 如果已经收藏直接返回
        return false;
    }
}
