<%@ page contentType="text/html; charset=UTF-8" %>
<%@  include file="patternTemplates/header.tpl" %>
    <div id="latest-events">
        <div class="container">
            <div class="event-inner">                
                <div class="row">                       
                    <div id="latest-event-content" class="col-sm-7 col-md-8"> 
                        <div class="bg">
                            <h2 class="heading"> הרשמה <strong>לאתר</strong> </h2 >       
                                 <div class="signup-area">
  			<form class="create-account-form" id="createAccount" name="createAccount" method="post" action="SignUp">
			  	<br>
			  	<div class="lable" id="whoAmILabel">מי אני?</div>
			  	<input type="radio" name="whoAmI" value="PR" checked>
				יחצן
				<input type="radio" name="whoAmI" value="Client">
				לקוח
				<br><br>
			  	
			  	<div class="lable" id="firstnameLabel">שם פרטי</div>
			  	<input type="text" name="firstName" id="firstName" required>
			  	<br>
			  	
			  	<div class="lable" id="lastnameLabel">שם משפחה</div>
			  	<input type="text" name="lastName" id="lastName" required>			
			  	<br>
			  	<br>
			  	<div class="lable" id="genderLabel">מין</div>
			  	<input type="radio" name="gender" value="Male" checked>
				זכר
				<input type="radio" name="gender" value="Female">
				נקבה
				<br><br>
				
				<div class="lable" id="birthdateLabel">תאריך לידה</div>
				<input name="birthdate" id="birthdate" required>
	  			<br>
				
				<div class="lable" id="phonenumberLabel">טלפון</div>
				<input type="text" name="phoneNumber" id="phoneNumber" required>
				<br>

				
				<div class="lable" id="emailLabel">דוא"ל</div>
				<input type="text" name="email" size="40" id="email" required>
	  			<br>

				<div class="lable" id="passwordLabel">סיסמה</div>
				<input type="password" name="password" id="password" required>
	  			<br>
				 
				<div class="lable" id="verifyPasswordLabel">אימות סיסמה</div>
				<input type="password" name="verifyPassword" id="verifyPassword" required>
	  			<br><br>
	  			
	  			<div class="captcha-area">
			       
	  			</div>
	  			
	  			<button type="submit" >הירשם</button>
	  			<input type=reset value="נקה"><br>
	  			
	  			<div class="message-area">
					<label id=loginFailed>
					</label>   			
  				</div>
			</form>
    	</div>	                      
                        </div>                                       
                    </div> 
                </div>                          
            </div>
        </div>
    </div>

    <%@  include file="patternTemplates/footer.tpl" %>


    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/jquery.prettyPhoto.js"></script>
    <script type="text/javascript" src="js/jquery.fitvids.js"></script>
    <script type="text/javascript" src="js/soundcloud.player.api.js"></script>
    <script type="text/javascript" src="js/sc-player.js"></script> 
    <script type="text/javascript" src="js/main.js"></script>
    <script type="text/javascript" src="js/coundown-timer.js"></script>
    <script type="text/javascript" src="js/jquery.backstretch.min.js"></script>
    <script type="text/javascript" src="js/switcher.js"></script>
    <script src="http://code.jquery.com/ui/1.11.0/jquery-ui.js"></script>
    
    
    <script src="js/datepicker-he.js"></script>
    
</body>
</html>