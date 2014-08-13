<%@page import="ClubberLogic.UserType"%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Search for auction</title>
		<link href="CSS/bootstrap/css/bootstrap.css" rel="stylesheet" type="text/css">
	    <link rel="stylesheet" href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">		
		<script src="//code.jquery.com/jquery-1.10.2.js"></script>
		<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
		<script src="//ajax.aspnetcdn.com/ajax/jquery.validate/1.9/jquery.validate.min.js"></script>
	</head>
	<body dir="rtl">
		<div class="message">
		התוצאות ממויינות אוטומטית לפי האזורים שבהם יש לך ליין לא ניתן לראות מכרזים באזורים אחרים. 
		</div>
		<div class="search-area">
			<form class="search-auction-form" id="searchAuction" name="searchAuction" method="post" action="SearchAuction">
				
				<div class="search-filters">
				  	<label id="agesRangeLabel">טווח גילאים</label>
					<input type="text" name="agesRange" id="agesRange">
				  	<br>
				  	
				  	<label id="musicStyleLabel">סגנון מוזיקה</label>
				  	<div class="auction-music-style">
					</div>
					
				  	<label id="dayInWeekLabel">יום בשבוע</label>
				  	<input type="checkbox" id="dayInWeek" onClick="toggle(this)" checked> הכל
				  	<input type="checkbox" name="dayInWeek" value="sunday" checked>ראשון
				  	<input type="checkbox" name="dayInWeek" value="monday" checked>שני
				  	<input type="checkbox" name="dayInWeek" value="tuesday" checked>שלישי
				  	<input type="checkbox" name="dayInWeek" value="wednesday" checked>רביעי
				  	<input type="checkbox" name="dayInWeek" value="thursday" checked>חמישי
				  	<input type="checkbox" name="dayInWeek" value="friday" checked>שישי
				  	<input type="checkbox" name="dayInWeek" value="saturday" checked>שבת
				  	<br>
			  	</div>
				
				<input type="checkbox" name="searchByMyLines" id="searchByMyLines">
				חפש לפי התאמה לליינים שלי
				<br>
				<button type="submit" >חפש</button>
			</form>
		</div>	
	<script>
		
	$("#searchByMyLines").click(function() {
		var isChecked = $("#searchByMyLines").is(':checked');
		var checkboxes = $('.search-filters input[type=checkbox]');
		
		for(var i = 0; i < checkboxes.length; i++) {
			checkboxes[i].disabled = isChecked;
		}
	});	
	
	function toggle(source) {
		var name = source.id;
		checkboxes = document.getElementsByName(name);
		
		for(var i = 0; i <= checkboxes.length; i++){
			checkboxes[i].checked = source.checked;
		}
	}
	
	$(function() {
	
		musicStyleDiv = $(".auction-music-style");
	  	
	    $.ajax({
	        url: "GetDBData",
	        type: "post",
	        dataType: 'json',
	        data:{RequestType: "DBDataAuctionMusicStyle"},
	        success: function(data) {
				
	        	musicStyleDiv.append($('<label><input type="checkbox" id="musicStyle" onClick="toggle(this)" checked>הכל </label>'));
	        	
	        	for(var i=0; i < data.length; i++){
	        		
	        		var element = '<label><input type="checkbox" name="musicStyle" checked>' +data[i].Name+ '</label>' ;
	        		musicStyleDiv.append($(element));
	        	}
	        	
	        },
	        error: function(data){
	            	console.log("error");}
	    });
	});	
	
	$("#searchAuction").validate({
		  rules: {
			    agesRange: {
			    	number: true,
			      	range: [1,120]
			    }
			 }
	});
	
	// Set error messages  
	jQuery.extend(jQuery.validator.messages, {
		number: "גיל מינימלי אינו חוקי",
	  	range: "גיל מינימלי אינו חוקי"
	});
	</script>
	</body>
</html>