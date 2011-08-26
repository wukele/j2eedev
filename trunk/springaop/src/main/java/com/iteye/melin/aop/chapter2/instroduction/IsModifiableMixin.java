package com.iteye.melin.aop.chapter2.instroduction;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.DelegatingIntroductionInterceptor;

public class IsModifiableMixin extends DelegatingIntroductionInterceptor
		implements IsModifiable {
	public boolean isModifiable() {
		return isModifiable;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
//		Authentication auth = SecurityContextHolder.getContext()
//				.getAuthentication();
		if (isModifiable) {
			StringBuilder sb = new StringBuilder();
			if ((invocation.getMethod().getName().startsWith("set"))
					&& (invocation.getArguments().length == 1)) {
				Method getter = getGetter(invocation.getMethod());
				//sb.append(auth == null ? "Anonymous" : auth.getName())
				sb.append("has try to modify the object state, method:")
						.append(getter.getName()).append(" and arguments:")
						.append(invocation.getArguments()[0]);
				if (getter != null) {
					Object value = getter.invoke(invocation.getThis(), null);
					if (value == null) {
						isModifiable = false;
					}
				}
			}
		}
		return super.invoke(invocation);
	}

	private Method getGetter(Method setter) {
        Method getter = (Method) setterMethodCache.get(setter);
        if (getter != null) {
            return getter;
        }
        String getterName = setter.getName().replaceFirst("set", "get");
        try {
            getter = setter.getDeclaringClass().getMethod(getterName, null);
            try {
                getter = setter.getDeclaringClass().getMethod(getterName, null);
                synchronized (setterMethodCache) {
                    setterMethodCache.put(setter, getter);
                }
                return getter;
            } catch (NoSuchMethodException ex) {
                return null;
            }
        } catch (NoSuchMethodException ex) {
            return null;
        }
    }

	private boolean isModifiable = true;
	private Map setterMethodCache = new HashMap();
}