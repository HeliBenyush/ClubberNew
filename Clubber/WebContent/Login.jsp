<%@page import="Utlis.Constants"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<% String loginFailedMessage = (String)request.getAttribute(Constants.LOGIN_FAILED); %>



<%@  include file="patternTemplates/header.tpl" %>



    <div id="latest-events">
        <div class="container">
            <div class="event-inner">                
                <div class="row">                       
                    <div id="latest-event-content" class="col-sm-7 col-md-8"> 
                        <div class="bg">
                            <h2 class="heading"> התחברות לאתר </h2 >       
                            <form class="login-form" id="loginForm" name="loginForm" method="post" action="Login">
                            <% if(loginFailedMessage != null){ %>
								<%=loginFailedMessage %>
							<%} %> <br> <br>
                            <input type="text" name="email" placeholder="שם משתמש" required><br><br>
                            <input type="password" name="Password" placeholder="סיסמא" required><br><br>
                            <button type="submit">התחבר</button><br>
                            <a href="PasswordRetrival.jsp" style="color:white; font-size: 13px;">שכחתי סיסמא\ שם משתמש</a><br>
                            <a href="SignUp.jsp">הירשם</a>
                            <br>
                            
                            </form>                   
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