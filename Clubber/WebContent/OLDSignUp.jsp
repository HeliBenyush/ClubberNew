<%@page import="Utlis.Constants"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="net.tanesha.recaptcha.ReCaptcha" %>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory" %>
<% String messageText = (String)request.getAttribute(Constants.MESSAGE_TEXT); %>
<%request.setCharacterEncoding ("UTF-8"); %>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Sign Up</title>
		<link href="CSS/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css">
        <link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">		
		<script src="//code.jquery.com/jquery-1.10.2.js"></script>
		<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
		<script src="https://jqueryui.com/resources/demos/datepicker/datepicker-he.js"></script>		
  		<script src="//ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>
	</head>
	<body dir="rtl">
		<div class="explanation-area">
		</div>
		<div class="signup-area">
  			<form class="create-account-form" id="createAccount" name="createAccount" method="post" action="SignUp">
			  	
			  	<label id="whoAmILabel">מי אני?</label>
			  	<input type="radio" name="whoAmI" value="PR" checked>
				יחצן
				<input type="radio" name="whoAmI" value="Client">
				לקוח
				<br>
			  	
			  	<label id="firstnameLabel">שם פרטי</label>
			  	<input type="text" name="firstName" id="firstName" required>
			  	<br>
			  	
			  	<label id="lastnameLabel">שם משפחה</label>
			  	<input type="text" name="lastName" id="lastName" required>			
			  	<br>
			  	
			  	<label id="genderLabel">מין</label>
			  	<input type="radio" name="gender" value="Male" checked>
				זכר
				<input type="radio" name="gender" value="Female">
				נקבה
				<br>
				
				<label id="birthdateLabel">תאריך לידה</label>
				<input name="birthdate" id="birthdate" required>
	  			<br>
				
				<label id="phonenumberLabel">טלפון</label>
				<input type="text" name="phoneNumber" id="phoneNumber" required>
				<br>

				
				<label id="emailLabel">דוא"ל</label>
				<input type="text" name="email" id="email" required>
	  			<br>

				<!-- label id="passwordLabel">סיסמה</label>
				<input type="password" name="password" id="password" required>
	  			<br>
				
				<label id="verifyPasswordLabel">אימות סיסמה</label>
				<input type="password" name="verifyPassword" id="verifyPassword" required-->
	  			<br><br>
	  			
	  			<div class="captcha-area">
			       <%
			         ReCaptcha c = ReCaptchaFactory.newReCaptcha("YOUR_PUBLIC_KEY", "YOUR_PRIVATE_KEY", false);
			         out.print(c.createRecaptchaHtml(null, null));
			       %>
	  			</div>
	  			
	  			<button type="submit" >הירשם</button>
	  			<input type=reset value="נקה"><br>
	  			
	  			<div class="message-area">
					<label id=loginFailed>
					<% if(messageText != null){ %>
						<%=messageText %>
					<%} %>
					</label>   			
  				</div>
			</form>
    	</div>	 
    	
		<!--<script src="http://jquery.bassistance.de/validate/jquery.validate.js"></script>
		<script src="http://jquery.bassistance.de/validate/additional-methods.js"></script>-->	
	
		<script>
	
		// date picker
		$(function() {
			var date = new Date();
			var currentYear = date.getFullYear();
			$('#birthdate').datepicker({
				yearRange: "-120:+0",
				minDate: new Date(currentYear - 120, 1, 1),
				maxDate: date,
				changeMonth: true,
		      	changeYear: true,
		      	showOn: "button",
		        buttonImage: "images/calendar.gif",
		        buttonImageOnly: true});	      	
		});
	
		
		// Set error messages  
		jQuery.extend(jQuery.validator.messages, {
		    required: "שדה חובה",
		    email: 'כתובת דוא"ל אינה חוקית',
		    equalTo: "סיסמאות אינן תואמות",
		    date: "תאריך לידה אינו חוקי"
		    
		});
		

		$( "#createAccount" ).validate({
			  rules: {
			    email: {
			      	email: true
			    },
		
			    
				verifyPassword: {
		      		equalTo: "#password"
			  	},
			    
			    birthdate: {
			    	date: true
			    }
			 }
			});		

		</script>					
    </body>
</html>