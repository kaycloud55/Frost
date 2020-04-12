package com.kaycloud.frost.aop;

import android.util.Log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Created by jiangyunkai on 2020/4/12
 */
@Aspect
public class PerformanceAop {

    @Around("execution(* com.kaycloud.frost.FrostApplication.on**(..))")
    public Object getTime(ProceedingJoinPoint joinPoint) {
        Log.i("PerformanceAop", "6666");
        Object result = null;
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}
