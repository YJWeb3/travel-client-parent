package com.zheng.travel.client.controller.index;

import com.alibaba.fastjson.JSONObject;
import com.zheng.travel.client.service.advice.IAdviceService;
import com.zheng.travel.client.service.banner.IBannerService;
import com.zheng.travel.client.service.category.ICategoryService;
import com.zheng.travel.client.controller.APIBaseController;
import com.zheng.travel.client.entity.Advice;
import com.zheng.travel.client.entity.Banner;
import com.zheng.travel.client.entity.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 首页
 */
@RestController
@RequiredArgsConstructor
@Slf4j
@Api(tags="首页管理")
public class IndexController extends APIBaseController {
    // 类：不要在一个类中处理很多业务的功能，但是可以处理一类业务的功能。
    private final IAdviceService adviceService;
    private final IBannerService bannerService;
    private final ICategoryService categoryService;

    // 建立一个线程池对象
    ExecutorService executorService = Executors.newCachedThreadPool();
    /**
     * 查看首页数据列表信息
     *
     * @return
     */
    @PostMapping("/index/load")
    @ApiOperation("首页数据加载")
    public JSONObject findIndexData() throws  Exception{
        //计算器
        CountDownLatch countDownLatch = new CountDownLatch(3);
        // 统一返回
        JSONObject jsonObject = new JSONObject();
        // // 1: 查看轮播图
        executorService.submit(()-> {
            List<Banner> bannerList = bannerService.findIndexBaner(1, 6);
            jsonObject.put("bannerList", bannerList);
            countDownLatch.countDown();
        });

        // 2: 查询对应分类
        executorService.submit(()-> {
            List<Category> categoriesList = categoryService.findCategories();
            jsonObject.put("categoryList", categoriesList);
            countDownLatch.countDown();
        });

        // 3: 查看对应公告
        executorService.submit(()-> {
            List<Advice> adviceList = adviceService.findIndexAdvice(1, 6);
            jsonObject.put("adviceList", adviceList);
            countDownLatch.countDown();
        });

        //阻塞 ?什么时候释放就等countDownLatch.countDown()==0
        countDownLatch.await();

        return jsonObject;
    }



}












