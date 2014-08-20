<%@ page contentType="text/html; charset=UTF-8" %>
<%@page  import="java.util.*" %>

<!DOCTYPE html> 
<head> 
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title>Clubber</title>
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="css/font-awesome.min.css" rel="stylesheet">    
    <link href="css/prettyPhoto.css" rel="stylesheet"> 
    <link href="css/sc-player-standard.css" rel="stylesheet">
    <link href="css/main.css" rel="stylesheet">
    <link href="css/responsive.css" rel="stylesheet">
    <link href="css/presets/preset1.css" id="preset" rel="stylesheet" type="text/css">
    <link href="css/switcher.css" rel="stylesheet" type="text/css">

    <!--[if lt IE 9]>
	    <script src="js/html5shiv.js"></script>
	    <script src="js/respond.min.js"></script> 
    <![endif]-->       
    <link rel="shortcut icon" href="images/ico/favicon.html">
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="images/ico/apple-touch-icon-144-precomposed.html">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="images/ico/apple-touch-icon-114-precomposed.html">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="images/ico/apple-touch-icon-72-precomposed.html">
    <link rel="apple-touch-icon-precomposed" href="images/ico/apple-touch-icon-57-precomposed.html">
    <style>
    #ui-datepicker-div{
        background: #000;
        padding: 10px;
    }
    label{
    	width: 200px;
    }
    </style>
</head><!--/head-->
<body>
	<header id="navigation">      
        <div class="navbar" role="banner">
        	<div class="container">
                <div class="row">
                    <div class="col-sm-9">
                        <nav class="navbar-right collapse navbar-collapse">
                            <ul class="nav navbar-nav">                                                                     
                                <li><a href="Login.jsp">התחברות</a></li>
                                <li><a href="SignUp.jsp">הרשמה</a></li>
                                <li class="dropdown"><a href="#">לינק 2<i class="fa fa-angle-down"></i></a>
                                    <ul role="menu" class="sub-menu">
                                        <li><a href="about-us.html">לינק</a></li>
                                        <li><a href="#">לינק</a></li>
                                    </ul>
                                </li>    
                                <li><a href="index.jsp">בית</a></li>
                            </ul>
                        </nav>
                    </div>
                    
                    
                    <div class="col-sm-3">
                        <div class="navbar-header">
                            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                                <span class="sr-only">Toggle navigation</span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                                <span class="icon-bar"></span>
                            </button>
                            <a class="navbar-brand" href="index.jsp"><h1><strong>Clubber</strong></h1></a>
                        </div>
                    </div>
                </div> 
            </div>
            <div class = "login"	>
		<%
		String name = (String)session.getAttribute("firstName");
		if(name == null)
		{%>
        	<form action="Login" method="post">
        		Email: <input type="text" name="email" placeholder="Email">	Password: <input type="password" name="Password" placeholder="Password">
				<input type="submit" value="Login">
			</form>
			<%}
			else{
				out.println("ברוך הבא " + name);%>
        		<form action="Logout" method="post">
    			<input type="submit" value="Logout" />
				</form>
			<%}%>
			</div>
            
        </div>
    </header> <!--/#navigation-->

