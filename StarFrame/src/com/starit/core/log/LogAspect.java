package com.starit.core.log;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.starit.core.util.SecurityContextUtil;
import com.starit.web.model.OperLog;
import com.starit.web.model.User;
import com.starit.web.service.OperLogService;

/**
 * 记录系统日志Aspect
 *
 * @datetime 2010-8-18 上午11:49:03
 * @author libinsong1204@gmail.com
 */
public class LogAspect {
	private static Map<String, Method[]> methodCaches = new ConcurrentHashMap<String, Method[]>();

	//~ Instance fields ================================================================================================
	private Logger logger = LoggerFactory.getLogger(LogAspect.class);
	
	@Autowired
	private OperLogService operLogService;

	//~ Methods ========================================================================================================
	public Object doSystemLog(ProceedingJoinPoint point) throws Throwable {   
        OperationDescription annotation = getMethodAnnotation(point);
        if (annotation != null) {   
            String methodDescp = annotation.description();
            OperationType type = annotation.type();   
            if (logger.isDebugEnabled()) {   
            	String methodName = point.getSignature().getName();   
                logger.debug("Action Type:" + type.getType() + " Action method:" + methodName + " Description:" + methodDescp);   
            }   
            //取到当前的操作用户   
            User user=SecurityContextUtil.getCurrentUser();   
            if(user!=null){   
                try{   
                	OperLog operLog=new OperLog();   
                       
                	operLog.setCreatetime(new Date());   
                	operLog.setOperatorId(user.getOperatorId());   
                	operLog.setOperatorName(user.getOperatorName());   
                	operLog.setExeOperation(methodDescp);   
                	operLog.setExeType(type.getType());
                       
                	operLogService.insertEntity(operLog);
                }catch(Exception ex){   
                    logger.error(ex.getMessage());   
                }   
            }   
        }  
        return point.proceed();   
    } 
    
    /**
     * To get the defined method's annotation of the target class
     * @param point
     * @return
     * @throws ClassNotFoundException 
     */
    private OperationDescription getMethodAnnotation(ProceedingJoinPoint point) throws ClassNotFoundException{
        String pjpStr = "";
        String localStr = "";
        OperationDescription annotation = null;
        String className =  point.getTarget().getClass().toString();
        Method[] m = methodCaches.get(className);
        if(m == null) {
        	m = this.getTargetMethods(point);
        	methodCaches.put(className, m);
        }

        // To get the method name and arguments information of the target method
        pjpStr = point.getSignature().toLongString();

        // check to get the annotation
        for (int i = 0; i < m.length; i++) {
            if (point.getArgs().length == m[i].getGenericParameterTypes().length) {
                if (point.getSignature().getName().equals(m[i].getName())) {
                    if (point.getArgs().length > 0) {
                        localStr = m[i].toString();
                        if (pjpStr.equals(localStr)) {
                        	OperationDescription action = m[i].getAnnotation(OperationDescription.class);
                        	annotation = action;
                        }
                    } else {
                    	OperationDescription action = m[i].getAnnotation(OperationDescription.class);
                    	annotation = action;
                    }
                }
            }
        }

        return annotation;
    }
    
    /**
     * To get the target class 
     * @param point
     * @return
     * @throws ClassNotFoundException 
     */
    @SuppressWarnings("unchecked")
	private Class getTargetClass(ProceedingJoinPoint point) throws ClassNotFoundException{
        String classname = point.getTarget().getClass().toString().substring(6);
        Class cls = Class.forName(classname);
        return cls;
    }
    
    /**
     * To get the methods of the target class
     * @param point
     * @return
     * @throws ClassNotFoundException 
     */
    @SuppressWarnings("unchecked")
	private Method[] getTargetMethods(ProceedingJoinPoint point) throws ClassNotFoundException{
        Class cls = this.getTargetClass(point);
        Method[] methods = cls.getMethods();
        return methods;
    }
}
