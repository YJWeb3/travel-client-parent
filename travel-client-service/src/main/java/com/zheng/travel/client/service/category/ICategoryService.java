package com.zheng.travel.client.service.category;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zheng.travel.client.entity.Category;

import java.util.List;


public interface ICategoryService extends IService<Category> {


    String CATEGORY_REDIS_KEY = "TRAVEL:category:index";

    /**
     * 查询公告
     * @return
     */
    List<Category> findCategories();
}
