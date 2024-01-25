package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 公共控制器
 *
 * @author 周简coding~~~
 * @date 2024/01/23
 */
@RestController
@RequestMapping("/admin/common")
@Api("通用接口")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;


    /**
     * 文件上传接口
     *
     * @param file 文件
     * @return String
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> upload(MultipartFile file){
        String upload;
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = null;
            if (originalFilename != null) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String objectName = UUID.randomUUID() + extension;
            upload = aliOssUtil.upload(file.getBytes(), objectName);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error(MessageConstant.UPLOAD_FAILED); //文件上传失败
        }
        return Result.success(upload);
    }

}
