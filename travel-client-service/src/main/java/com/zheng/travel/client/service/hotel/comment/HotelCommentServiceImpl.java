package com.zheng.travel.client.service.hotel.comment;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zheng.travel.client.bo.HotelCommentBo;
import com.zheng.travel.client.redis.TravelRedisOperator;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.entity.HotelComment;
import com.zheng.travel.client.entity.HotelommentUserZan;
import com.zheng.travel.client.entity.TravelUser;
import com.zheng.travel.client.mapper.hotel.HotelCommentMapper;
import com.zheng.travel.client.service.hotel.async.HotelRelationService;
import com.zheng.travel.client.threadlocal.UserThrealLocal;
import com.zheng.travel.client.vo.HotelCommentPageVo;
import com.zheng.travel.client.vo.HotelCommentVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class HotelCommentServiceImpl extends ServiceImpl<HotelCommentMapper, HotelComment> implements IHotelCommentService {


    @Autowired
    private IHotelCommentUserZanServiceImpl hotelommentUserZanService;
    @Autowired
    private HotelRelationService hotelRelationService;
    @Autowired
    private TravelRedisOperator redisOperator;

//    /**
//     * 保存酒店评论
//     *
//     * @param hotelCommentPageVo
//     * @return
//     */
//    @Override
//    public Page<HotelCommentBo> loadHotelComment(HotelCommentPageVo hotelCommentPageVo) {
//        //1: 查询第一级的评论
//        Page<HotelCommentBo> hotelCommentBoPage = loadCommentChildren(hotelCommentPageVo.getHotelId(),
//                hotelCommentPageVo.getParentId(), hotelCommentPageVo.getPageNo(), hotelCommentPageVo.getPageSize());
//        //2：处理子集的评论
//        List<HotelCommentBo> hotelCommentBoList = hotelCommentBoPage.getRecords();
//        //3: 如果你有拆线呢到评论数据
//        if (Vsserts.isNotEmptyCollection(hotelCommentBoList)) {
//            // 用户点赞的评论查询出来
//            travelUser travelUser = UserThrealLocal.get();
//
//            List<HotelommentUserZan> userZansCommentList = new ArrayList<>();
//            if(Vsserts.isNotNull(travelUser)) {
//                // 获取用户点赞的评论
//                LambdaQueryWrapper<HotelommentUserZan> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//                lambdaQueryWrapper.eq(HotelommentUserZan::getUserid, travelUser.getId());
//                userZansCommentList = hotelommentUserZanService.list(lambdaQueryWrapper);
//            }
//
//            List<HotelommentUserZan> finalUserZansCommentList = userZansCommentList;
//            hotelCommentBoList = hotelCommentBoList.stream().peek(hc -> {
//                //1:  把每一评论的对应子评论查询处理。这里有问题为啥是分页对象呢.方便子集进行分页处理
//                Page<HotelCommentBo> pageChildrenBo = loadCommentChildren(hotelCommentPageVo.getHotelId(),
//                        hc.getId(), 1, 3);
//                hc.setPageChildrens(pageChildrenBo);
//
//                if(!CollectionUtils.isEmpty(finalUserZansCommentList)) {
//                    // 把没个评论和用户点赞评论进行过滤比较，如果能找到就返回1 如果找不到返回 0
//                    long count = finalUserZansCommentList.stream().filter(usercomment -> usercomment.getCommentId().equals(hc.getId())).count();
//                    if (count > 0) {
//                        hc.setIszan(1);
//                    } else {
//                        hc.setIszan(0);
//                    }
//                }else{
//                    hc.setIszan(0);
//                }
//            }).collect(Collectors.toList());
//            // 把处理好的结果返回到数据中
//            hotelCommentBoPage.setRecords(hotelCommentBoList);
//        }
//        // 4:返回
//        return hotelCommentBoPage;
//    }


    /**
     * 保存酒店评论
     *
     * @param hotelCommentPageVo
     * @return
     */
    @Override
    public Page<HotelCommentBo> loadHotelComment(HotelCommentPageVo hotelCommentPageVo) {
        //1: 查询第一级的评论
        Page<HotelCommentBo> hotelCommentBoPage = loadCommentChildren(hotelCommentPageVo.getHotelId(),
                hotelCommentPageVo.getParentId(), hotelCommentPageVo.getPageNo(), hotelCommentPageVo.getPageSize());
        //2：处理子集的评论
        List<HotelCommentBo> hotelCommentBoList = hotelCommentBoPage.getRecords();
        //3: 如果你有拆线呢到评论数据
        if (Vsserts.isNotEmptyCollection(hotelCommentBoList)) {
            // 用户点赞的评论查询出来
            TravelUser travelUser = UserThrealLocal.get();
            hotelCommentBoList = hotelCommentBoList.stream().peek(hc -> {
                //1:  把每一评论的对应子评论查询处理。这里有问题为啥是分页对象呢.方便子集进行分页处理
                Page<HotelCommentBo> pageChildrenBo = loadCommentChildren(hotelCommentPageVo.getHotelId(),
                        hc.getId(), 1, 3);
                hc.setPageChildrens(pageChildrenBo);

                // 2: 把redis中每个点赞的赞数返回
                String countStr = redisOperator.getHashValue("redis:user:comment:like", hc.getId() + "");
                if (Vsserts.isNotEmpty(countStr)) {
                    hc.setZannum(Integer.valueOf(countStr));
                } else {
                    hc.setZannum(0);
                }

                // 3: 获取某个用户是否点击某个评论的赞
                String hget = redisOperator.hget("redis:comment:like:user", travelUser.getId() + ":" + hc.getId());
                if (Vsserts.isNotEmpty(hget)) {
                    hc.setIszan(1);
                } else {
                    hc.setIszan(0);
                }
            }).collect(Collectors.toList());
            // 把处理好的结果返回到数据中
            hotelCommentBoPage.setRecords(hotelCommentBoList);
        }
        // 4:返回
        return hotelCommentBoPage;
    }

    /**
     * 查询酒店分页
     *
     * @param hotelId
     * @param parentId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @Override
    public Page<HotelCommentBo> loadCommentChildren(Long hotelId, Long parentId, Integer pageNo, Integer pageSize) {
        Page<HotelComment> page = new Page<>(pageNo, pageSize);
        //2:设置条件
        LambdaQueryWrapper<HotelComment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HotelComment::getHotelId, hotelId);
        lambdaQueryWrapper.eq(HotelComment::getParentId, parentId);
        //3:设置排序
        lambdaQueryWrapper.orderByDesc(HotelComment::getCreateTime);
        // 3: 查询返回
        return tranferPageBo(this.page(page, lambdaQueryWrapper), HotelCommentBo.class);
    }


    /**
     * 评论赞+1
     *
     * @param commentId
     * @return
     */
    @Override
    @Transactional
    public int updateCommentPlusZannum(@Param("commentId") Long commentId) {
        TravelUser travelUser = UserThrealLocal.get();
        LambdaQueryWrapper<HotelommentUserZan> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HotelommentUserZan::getUserid, travelUser.getId());
        lambdaQueryWrapper.eq(HotelommentUserZan::getCommentId, commentId);
        long count = hotelommentUserZanService.count(lambdaQueryWrapper);
        if (count == 0) {
            //1: 评论表zannum + 1
            int i = this.baseMapper.updateCommentPlusZannum(commentId);
            //2: 记录用户和评论关系 新增到hotelcommentuserzan表中
            HotelommentUserZan hotelommentUserZan = new HotelommentUserZan();
            hotelommentUserZan.setUserid(travelUser.getId());
            hotelommentUserZan.setCommentId(commentId);
            hotelommentUserZanService.saveOrUpdate(hotelommentUserZan);
            return i;
        } else {
            return 0;
        }
    }

    /**
     * 评论赞 - 1
     *
     * @param commentId
     * @return
     */
    @Override
    @Transactional
    public int updateCommentMiusZannum(@Param("commentId") Long commentId) {
        //1: 评论表zannum + 1
        int i = this.baseMapper.updateCommentMiusZannum(commentId);
        TravelUser travelUser = UserThrealLocal.get();
        //2: 记录用户和评论关系 新增到hotelcommentuserzan表中
        LambdaQueryWrapper<HotelommentUserZan> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HotelommentUserZan::getUserid, travelUser.getId());
        lambdaQueryWrapper.eq(HotelommentUserZan::getCommentId, commentId);
        hotelommentUserZanService.remove(lambdaQueryWrapper);
        return i;
    }

    @Override
    public HotelCommentBo saveHotelComment(HotelCommentVo hotelCommentVo) {
        // 或者用户的信息
        TravelUser travelUser = UserThrealLocal.get();
        // 1: 把用户填写的信息针对某个酒店评论的信息进行填写pojo
        HotelComment hotelComment = new HotelComment();
        // 用户前台输入的信息
        hotelComment.setHotelId(hotelCommentVo.getHotelId());
        hotelComment.setContent(hotelCommentVo.getContent());
        hotelComment.setParentId(hotelCommentVo.getParentId());

        if (hotelCommentVo.getParentId() != null && hotelCommentVo.getParentId().equals(0L)) {
            // 用户信息的获取
            hotelComment.setUserid(travelUser.getId());
            hotelComment.setUsernickname(travelUser.getNickname());
            hotelComment.setUseravatar(travelUser.getAvatar());
            // 保存用户评论到评论表中
            boolean b = this.saveOrUpdate(hotelComment);
            // 用redis来记录评论总数 -- 有坑
            // 1: redis集群 ---
            // 2: 本地缓存
            // 3: rabbitmq
            //redisOperator.increment("hotel:comment:count:"+hotelComment.getHotelId(),1L);
            hotelRelationService.countHotelComment(hotelCommentVo.getHotelId());
            return tranferBo(hotelComment, HotelCommentBo.class);
        } else {
            // 这个这是去补齐数据，如果不做的也可以
            HotelComment parentHotelComment = this.getById(hotelCommentVo.getParentId());
            if (Vsserts.isNotNull(parentHotelComment)) {
                hotelComment.setUserid(parentHotelComment.getUserid());
                hotelComment.setUsernickname(parentHotelComment.getUsernickname());
                hotelComment.setUseravatar(parentHotelComment.getUseravatar());
            }
            // 用户信息的获取
            hotelComment.setReplyUserid(travelUser.getId());
            hotelComment.setReplyUsernickname(travelUser.getNickname());
            hotelComment.setReplyUseravatar(travelUser.getAvatar());
            // 保存用户评论到评论表中
            boolean b = this.saveOrUpdate(hotelComment);
            // 3: rabbitmq
            hotelRelationService.countHotelComment(hotelCommentVo.getHotelId());
            return tranferBo(hotelComment, HotelCommentBo.class);
        }
    }

    @Override
    public boolean delHotelComment(Long hotelId, Long commentId, Long userId) {
        LambdaQueryWrapper<HotelComment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HotelComment::getUserid, userId);
        lambdaQueryWrapper.eq(HotelComment::getId, commentId);
        boolean remove = this.remove(lambdaQueryWrapper);
        hotelRelationService.delHotelComment(hotelId);
        return remove;
    }


    /**
     * 根据酒店ID查询评论数
     *
     * @param hotelId
     * @return
     */
    @Override
    public int countHotelComment(Long hotelId) {
        LambdaQueryWrapper<HotelComment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(HotelComment::getHotelId, hotelId);
        Long commentCount = this.count(lambdaQueryWrapper);
        return commentCount != null ? commentCount.intValue() : 0;
    }
}
