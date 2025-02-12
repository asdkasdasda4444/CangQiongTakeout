package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@Api(tags = "菜品相关接口")
@RestController
@RequestMapping("/admin/dish")
public class DishController {
        @Autowired
        private DishService dishService;
        @Autowired
        private RedisTemplate redisTemplate;

        /**
         * 新增菜品
         * @param dishDTO
         * @return
         */
        @ApiOperation("新增菜品")
        @PostMapping()
        public Result<String> save(@RequestBody DishDTO dishDTO){
                log.info("新增菜品数据：{}",dishDTO);
                dishService.saveWithFlavor(dishDTO);

                //清理Redis缓存
                String key="dish_"+dishDTO.getCategoryId();
                cleanCache(key);

                return Result.success("新增菜品成功");
        }
        @ApiOperation("菜品分页查询")
        @GetMapping("/page")
        public Result<PageResult> page(DishPageQueryDTO dto){
                log.info("菜品分页数据:{}",dto);
                PageResult pageResult=dishService.page(dto);
                return Result.success(pageResult);

        }
        @ApiOperation("菜品批量删除")
        @DeleteMapping()
        public Result<String> delete(@RequestParam List<Long> ids){
                log.info("新增菜品数据：{}",ids);
                dishService.deleteBatch(ids);
//              清理Redis缓存
                cleanCache("dish_*");
                return Result.success("删除菜品成功");
        }

        @ApiOperation("根据id查询菜品和口味信息")
        @GetMapping("/{id}")
        public Result<DishVO> getById(@PathVariable Long id){
                log.info("修改菜品id：{}",id);
                DishVO dishVO=dishService.getByIdWithFlavor(id);

                return Result.success(dishVO);
        }

        @ApiOperation("修改菜品信息")
        @PutMapping
        public Result<String> update(@RequestBody DishVO dishVO){
                log.info("修改后菜品的信息：{}",dishVO);
                dishService.updateWithFlavor(dishVO);
                //              清理Redis缓存
                cleanCache("dish_*");
                return Result.success("修改成功");
        }

        @GetMapping("/list")
        @ApiOperation("根据分类Id查询菜品")
        public Result<List<Dish>> getByCategoryId(Long categoryId){
                log.info("categoryId:{}",categoryId);
                List<Dish> dishList = dishService.getByCategoryId(categoryId);
                return Result.success(dishList);
        }
//        TODO:菜品的起售和停售待完善
        @ApiOperation("菜品的起售和停售")
        @PostMapping("/status/{status}")
        public Result startOrStop( @PathVariable Integer status, Long id){
                log.info("起售停售路径参数：{}->起售停售的id参数-->{}",status,id);
//              清理Redis缓存
                cleanCache("dish_*");
                return Result.success();
        }



        /**
         * 清理缓存Redis数据
         * @param pattern
         */
        private void cleanCache(String pattern){

                Set keys = redisTemplate.keys(pattern);
                redisTemplate.delete(keys);
        }
}
