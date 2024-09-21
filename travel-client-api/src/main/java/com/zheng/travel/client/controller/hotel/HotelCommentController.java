package com.zheng.travel.client.controller.hotel;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zheng.travel.client.bo.HotelCommentBo;
import com.zheng.travel.client.result.ex.TravelValidationException;
import com.zheng.travel.client.service.hotel.comment.IHotelCommentService;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.controller.APIBaseController;
import com.zheng.travel.client.entity.TravelUser;
import com.zheng.travel.client.threadlocal.UserThrealLocal;
import com.zheng.travel.client.vo.HotelCommentPageVo;
import com.zheng.travel.client.vo.HotelCommentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@Api(tags = "酒店评论管理")
@RefreshScope
public class HotelCommentController extends APIBaseController {


    @Autowired
    @Qualifier("hotelCommentServiceImpl")
    private IHotelCommentService hotelCommentService;

    /**
     * 保存酒店评论和回复
     *
     * @param hotelCommentVo
     * @return
     */
    @ApiOperation("保存评论和回复评论")
    @PostMapping("/hotel/comment/save")
    public HotelCommentBo saveHotelComment(@RequestBody HotelCommentVo hotelCommentVo) {
        return hotelCommentService.saveHotelComment(hotelCommentVo);
    }

    /**
     * 评论的删除
     *
     * @param commentId
     * @return
     */
    @ApiOperation("评论的删除")
    @PostMapping("/hotel/comment/del/{hotelid}/{id}")
    public boolean delHotelComment(@PathVariable("hotelid") Long hotelid,@PathVariable("id") Long commentId) {
        TravelUser TravelUser = UserThrealLocal.get();
        return hotelCommentService.delHotelComment(hotelid,commentId, TravelUser.getId());
    }


    /**
     * 评论的查询分页
     *
     * @param hotelCommentPageVo
     * @return
     */
    @ApiOperation("评论的查询分页")
    @PostMapping("/hotel/comment/load")
    public Page<HotelCommentBo> loadHotelComment(@RequestBody HotelCommentPageVo hotelCommentPageVo) {
        return hotelCommentService.loadHotelComment(hotelCommentPageVo);
    }

    /**
     * 子集评论的查询分页
     *
     * @param hotelCommentPageVo 注意：parentId必须填写
     * @return
     */
    @ApiOperation("评论的子查询分页")
    @PostMapping("/hotel/comment/loadmore")
    public Page<HotelCommentBo> loadHotelCommentChild(@RequestBody HotelCommentPageVo hotelCommentPageVo) {
        if (Vsserts.isNotNull(hotelCommentPageVo.getParentId()) &&
                hotelCommentPageVo.getParentId().equals(0L)) {
            throw new TravelValidationException(602, "子集不能是0");
        }
        return hotelCommentService.loadCommentChildren(hotelCommentPageVo.getHotelId(), hotelCommentPageVo.getParentId(),
                hotelCommentPageVo.getPageNo(), hotelCommentPageVo.getPageSize());
    }

}
