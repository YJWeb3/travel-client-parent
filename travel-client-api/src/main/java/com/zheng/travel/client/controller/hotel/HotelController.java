package com.zheng.travel.client.controller.hotel;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zheng.travel.client.bo.HotelBo;
import com.zheng.travel.client.bo.HotelTypeMiddleBo;
import com.zheng.travel.client.service.hotel.IHotelService;
import com.zheng.travel.client.service.hotel.comment.IHotelCommentService;
import com.zheng.travel.client.service.hoteltypemiddle.IHotelTypeMiddleService;
import com.zheng.travel.client.service.userfav.IUserFavHotelService;
import com.zheng.travel.client.utils.JsonUtil;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.controller.APIBaseController;
import com.zheng.travel.client.entity.HotelTypeMiddle;
import com.zheng.travel.client.vo.HotelVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = "酒店管理")
public class HotelController extends APIBaseController {

    private final IHotelService hotelService;
    private final IHotelCommentService hotelCommentServiceImpl;
    private final IHotelTypeMiddleService hotelTypeMiddleService;
    private final IUserFavHotelService userFavHotelService;

    /**
     * 查询和分页对应的酒店信息
     *
     * @param hotelVo
     * @return
     */
    @PostMapping("/hotel/load")
    @ApiOperation("酒店列表分页查询")
    public IPage<HotelBo> searchHotels(@RequestBody HotelVo hotelVo) {
        return hotelService.searchHotels(hotelVo);
    }


    /**
     * 查询和分页对应的酒店信息
     *
     * @param hotelId
     * @return
     */
    @PostMapping("/hotel/detail/{id}")
    @ApiOperation("酒店明细信息")
    public HotelBo getDetailHotels(@PathVariable("id") String hotelId) {
        Vsserts.isNotEmptyEx(hotelId, "酒店ID不能为空!!");
        HotelBo hotelBo = hotelService.getHotels(Long.parseLong(hotelId));
        if (Vsserts.isNotNull(hotelBo) && Vsserts.isNotEmpty(hotelBo.getImglists())) {
            List<String> stringList = new ArrayList<>();
            // 把主图放第一张
            stringList.add(hotelBo.getImg());
            String[] split = hotelBo.getImglists().split(",");
            for (String s : split) {
                stringList.add(s);
            }
            // 其他的图集往后面放
            hotelBo.setImagesList(stringList);
            // 把原来的清空
            hotelBo.setImglists("");
        }


        if (Vsserts.isNotNull(hotelBo)) {
            //处理酒店的服务项目
            String hotelServiceItem = hotelBo.getHotelServiceItem();
            if (Vsserts.isNotEmpty(hotelServiceItem)) {
                List<Map<String, Object>> serviceItemMaps = JsonUtil.string2Obj(hotelServiceItem, List.class, HashMap.class);
                serviceItemMaps = serviceItemMaps.stream().sorted((a, b) -> {
                    return (int) a.get("sort") - (int) b.get("sort");
                }).collect(Collectors.toList());
                hotelBo.setHotelServiceItem("");
                hotelBo.setServiceItems(serviceItemMaps);
            }

            // 查询对应的酒店房型
            List<HotelTypeMiddle> hotelTypeBos = hotelTypeMiddleService.findHotelTypeMiddle(Long.parseLong(hotelId));
            if (!CollectionUtils.isEmpty(hotelTypeBos)) {
                hotelTypeBos = hotelTypeBos.stream().peek(htype -> {
                    List<String> imagesList = new ArrayList<>();
                    imagesList.add(htype.getImg());
                }).collect(Collectors.toList());
            } else {
                hotelTypeBos = new ArrayList<>();
            }
            hotelBo.setHotelTypes(hotelTypeBos);
            // 查询用户是否收藏
            hotelBo.setIsCollect(userFavHotelService.isUserCollect(hotelBo.getId()));
        }

        // 查询酒店对应的评论数
        hotelBo.setComments(hotelCommentServiceImpl.countHotelComment(hotelBo.getId()));

        return hotelBo;
    }


    /**
     * 查询和分页对应的酒店信息
     * @param hotelTypeId
     * @return
     */
    @PostMapping("/hotel/type/{hotelTypeId}")
    @ApiOperation("查询房型信息")
    public HotelTypeMiddleBo getHotelTypeDetail(@PathVariable("hotelTypeId") String hotelTypeId) {
        return hotelTypeMiddleService.getHotelTypeById(Long.parseLong(hotelTypeId));
    }
}
