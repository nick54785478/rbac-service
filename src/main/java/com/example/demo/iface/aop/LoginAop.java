package com.example.demo.iface.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.config.context.ContextHolder;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * */
@Aspect
@Component
@Slf4j
public class LoginAop {

	/**
	 * 定義切入點，針對 LoginController 進行切入。
	 */
	@Pointcut("execution(* com.example.demo.iface.controller.LoginController.*(..))")
	public void pointCut() {

	}

	@Around("pointCut()")
	public Object getServiceHeader(ProceedingJoinPoint joinPoint) throws Throwable {
		// 嘗試取得 Service 的請求頭
		HttpServletRequest request = getCurrentHttpRequest();

		if (request != null) {
			String serviceHeader = request.getHeader(ContextHolder.SERVICE_HEADER_KEY);
			log.info("serviceHeader: {}", serviceHeader);
			ContextHolder.setService(serviceHeader);
		} else {
			log.warn("無法取得 HttpServletRequest，可能不是 HTTP 請求上下文");
		}

		return joinPoint.proceed();
	}

	/**
	 * 取得目前的 HttpServletRequest
	 */
	private HttpServletRequest getCurrentHttpRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
			return servletRequestAttributes.getRequest();
		}
		return null;
	}
}
