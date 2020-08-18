<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
<div>
      
        <div class="input-prepend btn-group">
        	<a href="#" class="btn disabled"><span>共(</span> <span> ${page.numPerPage}/${page.totalRows} </span> <span>)条</span><span>(</span><span> ${page.currentPage}/${page.totalPages} </span><span>)页</span></a>
        </div>
        
        <div class="input-prepend btn-group">
        
            <c:if test="${page.currentPage==1}"><a href="#" class="btn disabled"><i class="icon-step-backward"></i> 首页</a></c:if>
            <c:if test="${page.currentPage!=1}"><a href="${pageContext.request.contextPath}/api/${pageurl}?${page.action}&currentPageNO=1" class="btn"><i class="icon-step-backward"></i> 首页</a></c:if>
            
            
            <c:if test="${page.currentPage==1}"><a href="#" class="btn disabled"><i class="icon-backward"></i> 上一页</a></c:if>
            <c:if test="${page.currentPage!=1}"><a href="${pageContext.request.contextPath}/api/${pageurl}?${page.action}&currentPageNO=${page.currentPage-1}" class="btn"><i class="icon-backward"></i> 上一页</a></c:if>
            
            <c:if test="${page.currentPage==page.totalPages}"><a href="#" class="btn disabled">下一页 <i class="icon-forward"></i></a></c:if>
            <c:if test="${page.currentPage!=page.totalPages}"><a href="${pageContext.request.contextPath}/api/${pageurl}?${page.action}&currentPageNO=${page.currentPage+1}" class="btn">下一页 <i class="icon-forward"></i></a></c:if>
            
            <c:if test="${page.currentPage==page.totalPages}"><a href="#" class="btn disabled">末页 <i class="icon-fast-forward"></i></a></c:if>
            <c:if test="${page.currentPage!=page.totalPages}"><a href="${pageContext.request.contextPath}/api/${pageurl}?${page.action}&currentPageNO=${page.totalPages}" class="btn">末页 <i class="icon-fast-forward"></i></a></c:if>
        
        </div>
        
        <div class="btn-group input-prepend input-append">
        	<span class="add-on">到</span>
        	<input id="topage.page" class="span2" type="text" name="topage.page" placeholder="${page.currentPage}" />
        	<span class="add-on">页</span>
        	<a href="#" onclick="javascript:p=document.getElementById('topage.page').value;location.href='${pageContext.request.contextPath}/api/${pageurl}?${page.action}&currentPageNO='+p;" class="btn">GO</a>
        </div>

</div>




