package com.zheng.travel.client.service.category;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zheng.travel.client.entity.Category;
import com.zheng.travel.client.mapper.category.CategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 查询分类
     *
     * @return
     */
    @Override
    public List<Category> findCategories() {
        // 1: 在缓存中获取分类信息
        List<Category> cacheList = (List<Category>)
                redisTemplate.opsForValue().get(CATEGORY_REDIS_KEY);
        // 2: 如果没有命中到
        //CollectionUtils.isEmpty(cacheList)
        if (true) {
            LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Category::getStatus, 1);
            lambdaQueryWrapper.orderByAsc(Category::getSorted);
            // 直接从数据查询
            List<Category> list = this.list(lambdaQueryWrapper);
            // 查询完毕放入缓存中
            redisTemplate.opsForValue().set(CATEGORY_REDIS_KEY, list);
            return list;
        }
        // 3：如果缓存有数据，直接返回
        return cacheList;
    }
}
