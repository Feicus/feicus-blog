package mblog.aop;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 日志切面
 */
@Slf4j
@Aspect
@Component
public class LoggingAspect {
    /**
     * 定义所有service bean的切点
     */
    @Pointcut("execution(* mblog.modules.blog.service.impl.*.*(..))" )
    public void servicePointcut() {

    }

    /**
     * 打印service层抛出的异常信息
     *
     * @param joinPoint 异常参数
     * @param e         异常信息
     */
    @AfterThrowing(pointcut = "servicePointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.error("Exception in {}.{}() with cause = \'{}\' and exception = \'{}\'", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), e.getCause() != null ? e.getCause() : "NULL", e.getMessage(), e);
    }

    /**
     * 打印目标方法的入参和出参信息
     *
     * @param joinPoint 目标切点
     * @return 返回参数
     */
    @Around("servicePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("入参: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(), JSONObject.toJSON(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.info("出参: {}.{}() with result = {}", joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(), JSONObject.toJSON(result));
            return result;
        } catch (IllegalArgumentException e) {
            log.error("非法参数异常: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
                    joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
            throw e;
        }
    }
}
