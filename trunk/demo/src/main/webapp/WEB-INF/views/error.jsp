<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<%@ page isErrorPage="true"%>
<% 
response.setStatus(HttpServletResponse.SC_OK);
String path=request.getContextPath(); 
%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd"> 
<html> 
	<head> 
		<meta http-equiv="Content-Type" content="text/html; charset=GB18030"> 
		<title>Insert title here</title> 
	</head> 
	<body> 
		<div><B>系统执行发生错误，信息描述如下：</B></div> 
		<div><B>错误状态代码是：</B>${pageContext.errorData.statusCode}</div> 
		<div><B>错误发生页面是：</B>${pageContext.errorData.requestURI}</div> 
		<div><B>错误信息：</B>${pageContext.exception}</div> 
		<div> 
			<B>错误堆栈信息：</B><br/>
			<%  StackTraceElement[] trace = (StackTraceElement[]) pageContext.getException().getStackTrace(); 
				for (int i=0; i < trace.length; i++) {
			%>
			<%=
					trace[i]
			%>
			<%
				}
			%>
		</div> 
	</body> 
</html>
