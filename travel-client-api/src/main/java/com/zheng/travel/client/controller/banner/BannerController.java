package com.zheng.travel.client.controller.banner;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zheng.travel.client.anno.login.LoginCheck;
import com.zheng.travel.client.localcache.EhCacheService;
import com.zheng.travel.client.service.banner.IBannerService;
import com.zheng.travel.client.controller.APIBaseController;
import com.zheng.travel.client.entity.Banner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Api(tags = "轮播图管理")
@LoginCheck
public class BannerController extends APIBaseController {

    private final IBannerService bannerService;
    private final EhCacheService ehCacheService;


    @ApiOperation("查询轮播图列表")
    @PostMapping("/banner/load")
    public List<Banner> loadBanners() {
        List<Banner> bannerList = (List<Banner>)ehCacheService.getCache("bannerList");
        if(bannerList ==null){
            LambdaQueryWrapper<Banner> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Banner::getStatus,1);
            queryWrapper.eq(Banner::getIsdelete,0);
            bannerList = bannerService.list(queryWrapper);
            ehCacheService.setCache("bannerList", bannerList);
        }
        return bannerList;
    }

}
