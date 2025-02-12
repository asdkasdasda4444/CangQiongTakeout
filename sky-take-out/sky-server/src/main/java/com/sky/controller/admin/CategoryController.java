package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Employee;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController(("adminCategoryController"))
@Api(tags = "分类相关接口")
@RequestMapping("/admin/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @ApiOperation("菜品分类回显")
    @GetMapping("/list")
    public Result<List> list(Integer type){
        List<Category> list = categoryService.list(type);
        return Result.success(list);

    }

    /**
     * 根据类型查询分类
     * @param categoryPageQueryDTO
     * @return
     */

    @ApiOperation("套餐分页查询")
    @GetMapping("/page")
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("接受的page参数：{}",categoryPageQueryDTO);
        PageResult pageQuery=categoryService.pageQuery(categoryPageQueryDTO);
        return pageQuery;
    }



}
