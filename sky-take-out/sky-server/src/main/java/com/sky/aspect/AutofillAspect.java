package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

//定义切面类
@Aspect
@Slf4j
@Component
public class AutofillAspect {
    /*
    定义切点
     */


    /**
      execution(类型 方法定位)：
     execution(* com.sky.mapper.*.*(..))

      execution 表达式是用于匹配方法执行的一种方式。
      * 通配符用于匹配任意返回类型。
      com.sky.mapper.*.* 匹配任意类下的任意方法。
      (..) 匹配任意参数列表。

     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /*
    定义切面
     */
    //前置切面
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始公共字段填充");
//      获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill=signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType=autoFill.value();
//       获取到当前被拦截的方法的参数-实体对象
        Object[] args= joinPoint.getArgs();
//       防止空指针
        if( args == null || args.length==0){
            return;
        }
        Object entity=args[0];
//       准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId= BaseContext.getCurrentId();
//       根据不同的操作类型为对应的属性通过反射来赋值
        if(operationType == OperationType.INSERT){

            //        为四个公共字段赋值
            try {
                //getClass() : 用于获取当前实体的（运行时）所属类
                //getDeclaredMethod(方法名称,参数类型) : 用于获取指定名称和参数类型的方法。
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
            //         通过反射为对象赋值

            //method.invoke(entity,now) : 通过反射机制调用 实体(entity) 对象所属类中的 method 方法并
                //                                 传递  赋值(now) 作为方法的参数
                setCreateTime.invoke(entity,now);
                setUpdateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateUser.invoke(entity,currentId);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operationType==OperationType.UPDATE) {
            //        为两个公共字段赋值
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class);
            //         通过反射为对象赋值
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


}
