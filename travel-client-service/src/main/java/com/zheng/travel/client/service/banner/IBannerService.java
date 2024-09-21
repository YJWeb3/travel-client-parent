package com.zheng.travel.client.service.banner;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zheng.travel.client.entity.Banner;

import java.util.List;

public interface IBannerService extends IService<Banner> {

    /**
     * 查询轮播图
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<Banner> findIndexBaner(Integer pageNo, Integer pageSize);
}
