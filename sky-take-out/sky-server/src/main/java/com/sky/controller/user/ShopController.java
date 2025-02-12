package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * TODO:因为有两个相同名称的controller所以要区分
 * &#064;RestController("userShopController")
 * &#064;RestController("adminShopController")
 */



@RestController("userShopController")
@Slf4j
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
public class ShopController {
    public static final String KEY="SHOP_STATUS";
    @Autowired
    public RedisTemplate redisTemplate;
    /**
     * 获取店铺的营业状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺的营业状态")
    public Result<Integer> getStatus(){

//        TODO:不小心把储存在redis中的SHOP_STATUS值删了导致无法获取
//        Integer shopStatus = (Integer) redisTemplate.opsForValue().get(KEY);
//        log.info("店铺的营业状态{}",shopStatus==1?"营业总":"打烊中");
//        临时解决办法

        return Result.success( 1);
    }
}
