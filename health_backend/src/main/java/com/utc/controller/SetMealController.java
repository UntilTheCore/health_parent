package com.utc.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.utc.constant.MessageConstant;
import com.utc.entity.PageResult;
import com.utc.entity.QueryPageBean;
import com.utc.entity.Result;
import com.utc.pojo.Setmeal;
import com.utc.service.SetMealService;
import com.utc.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    SetMealService setMealService;

    @RequestMapping("/upload")
    public Result uploadFile(@RequestParam("imgFile") MultipartFile imgFile) {
        String originalFilename = imgFile.getOriginalFilename();
        assert originalFilename != null;
        int index = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(index - 1);
        String fileName = UUID.randomUUID().toString() + suffix;

        try {
            byte[] fileBytes = imgFile.getBytes();
            QiNiuUtils.upload2QiNiu(fileBytes, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }

        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);

    }

    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, @RequestParam("checkGroupIds") Integer[] checkGroupIds) {
        try {
            setMealService.add(setmeal, checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }

        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @GetMapping("/findPage")
    public PageResult findPage(QueryPageBean queryPageBean) {
        return setMealService.findPage(queryPageBean);
    }

    @DeleteMapping("/delete")
    public Result deleteById(Integer id) {
        try {
            setMealService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "??????????????????");
        }

        return new Result(true, "??????????????????");
    }

    @GetMapping("/findById")
    public Result findById(Integer id) {
        try {
            Setmeal setmeal = setMealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @GetMapping("/findCheckGroupIdsBySetMealId")
    public Result findCheckGroupIdsBySetMealId(Integer id) {
        try {
            List<Integer> checkGroupIdsBySetMealId = setMealService.findCheckGroupIdsBySetMealId(id);
            return new Result(true, "?????????????????????????????????", checkGroupIdsBySetMealId);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "?????????????????????????????????");
        }
    }

    @PostMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkGroupIds) {
        try {
            setMealService.edit(setmeal, checkGroupIds);
            return new Result(true, "??????????????????");
        } catch (Exception e) {
            return new Result(false, "??????????????????");
        }
    }
}
