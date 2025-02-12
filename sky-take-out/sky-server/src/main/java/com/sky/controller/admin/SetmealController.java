package com.sky.controller.admin;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController("adminSetmealController")
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealVO
     * @return
     */
    @PostMapping
    @ApiOperation("新增套餐")
    @CacheEvict(cacheNames = "setmealCache", key = "#setmealVO.categoryId")
    public Result<String> save(@RequestBody SetmealVO setmealVO){
        log.info("接收到的参数SetmealVO：{}",setmealVO);
        setmealService.saveWithDish(setmealVO);
        return Result.success("新增套餐成功");
    }

    /**
     * 套餐的分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        log.info("分页查询参数:{}",setmealPageQueryDTO);
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

//    @DeleteMapping
//    @ApiOperation("批量删除套餐")
//    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
//
//    @GetMapping("/{id}")
//    @ApiOperation("根据id查询套餐")
//
//    @PutMapping
//    @ApiOperation("修改套餐")
//    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
//
//    @PostMapping("/status/{status}")
//    @ApiOperation("套餐的起售停售")
//    @CacheEvict(cacheNames = "setmealCache",allEntries = true)
//
//


}
