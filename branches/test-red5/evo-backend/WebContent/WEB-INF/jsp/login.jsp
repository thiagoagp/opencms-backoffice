<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s"   uri="/struts-tags" %>
<fmt:setLocale value="${pageLocale}"/>

<fmt:bundle basename="eni.virtualoffice.translation">

<html>
<c:import url="common/header.jsp" />

<body>
    <div class="mainWrapper">
		<div class="login">
		
		    <div class="header">
		        <fmt:message key="login.header"/>
		    </div>
		
		    <c:if test="${not empty errorMessageKey}">
		        <div class="error"><fmt:message key="${errorMessageKey}" /></div>
		    </c:if>
		
	    	<s:form action="login" method="POST">
			   <div class="label"><fmt:message key="login.username"/>:</div>
			   <div class="value"><input type="text" name="username" value="${username}" /></div>
			   <div class="clear"></div>
			   
			   <div class="label"><fmt:message key="login.password"/>:</div>
			   <div class="value"><input type="password" name="password" value="" /></div>
			   <div class="clear"></div>
	
			   <div class="label">&nbsp;</div>
			   <div class="value"><input type="submit" value="<fmt:message key="login.send"/>" /></div>
			   <div class="clear"></div>
			</s:form>
		
		</div>
	</div>
	
	<c:import url="common/footer.jsp" />
</body>
</html>

</fmt:bundle>