<%@page import="Utlis.Constants"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<% String loginFailedMessage = (String)request.getAttribute(Constants.LOGIN_FAILED); %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Login</title>
	<link href="CSS/bootstrap/css/bootstrap.css" rel="stylesheefdsgsdt" type="text/css">	
	<script src="//code.jquery.com/jquery-1.10.2.js"></script>
	<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
	<script src="//ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>
</head>
<body dir="rtl">
	<div class="exist-user-area">
		<form class="login-form" id="loginForm" name="loginForm" method="post" action="Login">
			<label id="emailLabel">דוא"ל</label>
			<input type="text" name="email" id="email" required>
  			<br>

			<label id="passwordLabel">סיסמה</label>
			<input type="password" name="Password" id="Password" required>
  			<br><br>
  			<div class="captcha-area"></div>
  			<button type="submit" >התחבר</button>
  			<div class="message-area">
				<label id=loginFailed>
					<% if(loginFailedMessage != null){ %>
						<%=loginFailedMessage %>
					<%} %>
				</label>   			
  			</div>
		</form>
	</div>
	<div class="forget-password-area">
		<!--  redirect to passwordRetrival jsp page  -->
		<a href="PasswordRetrival.jsp">שחזור סיסמה</a>
	</div>
	<div class="subscribe-area">
		<!--  redirect to signUp jsp page  -->
		<a href="SignUp.jsp">הירשם</a>
	</div>
	<script>
	
	// Set default messages  
	jQuery.extend(jQuery.validator.messages, {
	    required: "שדה חובה",
	    email: 'כתובת דוא"ל אינה חוקית' 
	});	
	
	// validate fields
	$( "#loginForm" ).validate({
		  rules: {
		    email: {
		      	email: true
		    }
		 }
		});		
	
	</script>		    				
</body>
</html>