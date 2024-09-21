package com.zheng.travel.client.service.banner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zheng.travel.client.entity.Banner;
import com.zheng.travel.client.mapper.banner.BannerMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {

    /**
     * 查询轮播图
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public List<Banner> findIndexBaner(Integer pageNo, Integer pageSize) {
        // 1: 设置分页
        Page<Banner> page = new Page<>(pageNo, pageSize);
        // 2 :设置条件
        LambdaQueryWrapper<Banner> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Banner::getStatus, 1);
        lambdaQueryWrapper.eq(Banner::getIsdelete, 0);
        lambdaQueryWrapper.orderByAsc(Banner::getSorted);

        Page<Banner> advicePage = this.page(page, lambdaQueryWrapper);
        List<Banner> records = advicePage.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return new ArrayList<>();
        }
        return records;
    }
}
