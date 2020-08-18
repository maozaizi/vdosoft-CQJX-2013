<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
	<div id="delete_reason" class="white_content">
		<span class="t"></span>
		<div  class="del_reason">
			<div class="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="remark">作废原因</label>
				<div class="controls">
					<textarea id="reason" name="reason" cols="54" rows="10"></textarea>
				</div>
			</div>
			<a class="btn btn-primary" href="#" id="deleteBtn">作废</a>
			<a class="btn" href="#" id="delCloseBtn">关闭</a>
		</div>
		</div>
		<span class="b"></span>		
	</div>
	<div id="fade" class="black_overlay"></div>
