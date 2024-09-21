package com.zheng.travel.client.controller.user;

import com.zheng.travel.client.bo.TravelUserBo;
import com.zheng.travel.client.config.middle.minio.MinIOConfig;
import com.zheng.travel.client.config.middle.minio.MinIoUploadService;
import com.zheng.travel.client.controller.APIBaseController;
import com.zheng.travel.client.service.user.IUserService;
import com.zheng.travel.client.service.weixin.WeixinData;
import com.zheng.travel.client.utils.fn.asserts.Vsserts;
import com.zheng.travel.client.entity.TravelUser;
import com.zheng.travel.client.threadlocal.UserThrealLocal;
import com.zheng.travel.client.vo.TravelUserSettingVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Api(tags = "用户中心个人设置")
@RestController
@RequiredArgsConstructor
public class UserSettingController extends APIBaseController {

    private final IUserService userService;
    private final MinIoUploadService minIoUploadService;
    private final MinIOConfig minIOConfig;


    @ApiOperation("修改用户头像接口")
    @PostMapping("/user/modifty/avatar/{userid}")
    public TravelUserBo updateAvatar(@PathVariable("userid") Long userId,
                                  @RequestParam("filename") String filename,
                                  @RequestParam(value = "bucketname", defaultValue = "userapp") String bucketname,
                                  MultipartFile file) throws Exception {
        Vsserts.isNullEx(userId, "没有找到修改用户");
        //给上传的头像取一个名字，不建议使用原始图片作为图片名，
        // 为什么：上传都喜欢把文件名用一些随机数或者uuid获取写工具方法重新成功。防止冲突
        // 比如A 用户本地电脑：1.jpg  B 比如B 用户本地电脑：1.jpg
        long fileSize = file.getSize();
        String contentType = file.getContentType();
        // 过滤判断
        // 压缩方法
        String fileName = null;
        if (Vsserts.isNotEmpty(filename)) {
            fileName = filename;
        } else {
            String oldname = file.getOriginalFilename();
            String ext = oldname.substring(oldname.lastIndexOf("."));
            fileName = UUID.randomUUID().toString() + ext;
        }

        if (Vsserts.isEmpty(bucketname)) {
            bucketname = minIOConfig.getBucketName();
        }

        minIoUploadService.createBucket(bucketname);
        minIoUploadService.uploadFile(bucketname, fileName, file.getInputStream());
        String imgUrl = minIOConfig.getFileHost()
                + "/"
                + bucketname
                + "/"
                + fileName;

        TravelUser TravelUser = new TravelUser();
        TravelUser.setId(userId);
        TravelUser.setAvatar(imgUrl);
        userService.saveOrUpdate(TravelUser);

        // 返回
        TravelUserBo TravelUserBo = new TravelUserBo();
        TravelUserBo.setAvatar(imgUrl);
        return TravelUserBo;
    }

    @ApiOperation("修改用户背景")
    @PostMapping("/user/modifty/bgimg/{userid}")
    public TravelUserBo updateBgImg(@PathVariable("userid") Long userId,
                                 @RequestParam("bucketname") String bucketname,
                                 @RequestParam(value = "filename",defaultValue = "userapp") String filename,
                                 MultipartFile file) throws Exception {
        Vsserts.isNullEx(userId, "没有找到修改用户");
        String fileName = null;
        if (Vsserts.isNotEmpty(filename)) {
            fileName = filename;
        } else {
            String oldname = file.getOriginalFilename();
            String ext = oldname.substring(oldname.lastIndexOf("."));
            fileName = UUID.randomUUID().toString() + ext;
        }
        if (Vsserts.isEmpty(bucketname)) {
            bucketname = minIOConfig.getBucketName();
        }
        minIoUploadService.createBucket(bucketname);
        minIoUploadService.uploadFile(bucketname, fileName, file.getInputStream());
        String imgUrl = minIOConfig.getFileHost()
                + "/"
                + bucketname
                + "/"
                + fileName;
        TravelUser TravelUser = new TravelUser();
        TravelUser.setId(userId);
        TravelUser.setBgImg(imgUrl);
        userService.saveOrUpdate(TravelUser);
        // 返回
        TravelUserBo TravelUserBo = new TravelUserBo();
        TravelUserBo.setBgImg(imgUrl);
        return TravelUserBo;
    }


    @ApiOperation("修改用户其他的信息")
    @PostMapping("/user/modifty/info")
    public TravelUserBo updateUserInfo(@RequestBody TravelUserSettingVo userSettingVo) throws Exception {
        Vsserts.isNullEx(userSettingVo.getUserId(), "没有找到修改用户");
        TravelUser TravelUser = userService.getById(userSettingVo.getUserId());
        BeanUtils.copyProperties(userSettingVo, TravelUser);
        TravelUser.setId(userSettingVo.getUserId());
        userService.updateById(TravelUser);
        // 返回
        TravelUserBo TravelUserBo = new TravelUserBo();
        BeanUtils.copyProperties(TravelUser, TravelUserBo);
        return TravelUserBo;
    }


    @ApiOperation("同步微信授权的用户信息")
    @PostMapping("/user/update/weixin")
    public TravelUserBo updateweixinuserinfo(@RequestBody WeixinData weixinData) throws Exception {
        TravelUser TravelUser = UserThrealLocal.get();//这里确实完整的信息 20 update
        TravelUser modifyUser = new TravelUser();
        modifyUser.setId(TravelUser.getId());
        modifyUser.setNickname(weixinData.getNickName());
        modifyUser.setAvatar(weixinData.getAvatarUrl());
        modifyUser.setCountry(weixinData.getCountry());
        modifyUser.setProvince(weixinData.getProvince());
        modifyUser.setCity(weixinData.getCity());
        modifyUser.setSex(Integer.parseInt(weixinData.getGender()));
        boolean b = userService.updateById(modifyUser);
        if (b) {
            // 返回
            TravelUserBo TravelUserBo = new TravelUserBo();
            BeanUtils.copyProperties(modifyUser, TravelUserBo);
            return TravelUserBo;
        }
        return null;
    }

}
