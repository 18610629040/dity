package com.dity.common.aop.aspect;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dity.common.aop.annotation.Log;
import com.dity.common.aop.service.LogService;
import com.dity.common.utils.IDUtils;


@Aspect
@Component
public class LogAspect {
	
    @Autowired
    private LogService logService;
    
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@annotation(com.dity.common.aop.annotation.Log)")
    public void logAspect() {
        // nothing to do
    }

    @SuppressWarnings("unchecked")
	@AfterReturning(pointcut = "logAspect()", returning = "ret")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
    	
    	try {
    		MethodSignature ms = (MethodSignature) joinPoint.getSignature();
            Method method = ms.getMethod();
            Map<String, Object> O_DATA = (Map<String, Object>) ret;
            Log log = method.getAnnotation(Log.class);
            if (log != null) {
                Map<String, Object> param = new HashMap<>(16);
                HttpServletRequest request =
                        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                                .getRequest();
                String url = request.getRequestURL().toString();
                param.put("URL",url);
                
                HttpSession session = request.getSession();
                Map<String, Object> user = (Map<String, Object>) session.getAttribute("userInfo");
                if("1".equals(String.valueOf(O_DATA.get("O_RUNSTATUS")))) {
                	param.put("USER_ID", user.get("USER_ID"));
                	param.put("CONTENT", log.content());
                } else {
                	if("login".equals(method.getName())) {
                		param.put("USER_ID", request.getParameter("USER_ID"));
                	} else if("register".equals(method.getName())) {
                		param.put("USER_ID", request.getParameter("USER_ID"));
                	} else {
                		param.put("USER_ID", user.get("USER_ID"));
                	}
                	param.put("CONTENT", log.content()+"{"+O_DATA.get("O_MSG")+"}");
                }
                param.put("METHOD_NAME", method.getName());
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                param.put("TIMESTR", format.format(new Date()));
                param.put("ID", IDUtils.createID());
                logService.insertLog(param);
            }
		} catch (Exception e) {
			logger.error("日志记录失败"+ ret);
		}
    }
}
