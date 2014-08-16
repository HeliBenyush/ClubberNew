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
		<script src="//cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js" type="text/javascript"></script>
		<link href="CSS/dataTable.css" rel="stylesheet" type="text/css">		
		<style type="text/css">
		table.auctions-table {
			font-family: verdana,arial,sans-serif;
			font-size:11px;
			color:#333333;
			border-width: 1px;
			border-color: #a9c6c9;
			border-collapse: collapse;
		}
		table.auctions-table th {
			border-width: 1px;
			padding: 8px;
			border-style: solid;
			border-color: #a9c6c9;
		}
		table.auctions-table td {
			border-width: 1px;
			padding: 8px;
			border-style: solid;
			border-color: #a9c6c9;
		}
		</style>

	</head>
	<body dir="rtl">
		<div class="message">
		כל המכרזים שפורסמו במערכת:
		</div>
		<div class="search-by-my-lines-area">
			<input type="checkbox" name="searchByMyLines" id="searchByMyLines">
			סנן לפי התאמה לליינים שלי
			<br>
		</div>	
      	<div class='all-auctions-container'>
      	
      	</div>   
	<script>
	function formattedDate(date) {
	    var d = new Date(date || Date.now()),
	        month = '' + (d.getMonth() + 1),
	        day = '' + d.getDate(),
	        year = d.getFullYear();

	    if (month.length < 2) month = '0' + month;
	    if (day.length < 2) day = '0' + day;

	    return [day, month, year].join('/');
	}
	
	$("#searchByMyLines").change(function() {
		if(this.checked){
			getAuctionsByMyLines();
		}
		else{
			getAllAuctions();
		}
	});	
	
	function getAuctionsByMyLines(){
	    $.ajax({
	        url: "GetDBData",
	        type: "post",
	        dataType: 'json',
	        data: {RequestType: "searchByMyLines"},
	        success: function(data) {
				  showAuctions(data);
	        },
	        error: function(data){
	            	console.log("error");}
	    });
	}
	
	function showAuctions(data){
		console.log("adding auctions");
		var counterDescription= " הצעות התקבלו  ";
		
		$(".all-auctions-container").html("");

		var table= '<table class="auctions-table" id="auctionsTable" cellspacing="2">'
			  +'<thead><tr><th>סוג אירוע</th><th>תאריך</th><th>גיל מינימלי</th><th>איזור</th><th>מספר הצעות שהוצעו</th></tr></thead><tbody>';

		for (var item in data) {			
			table += '<tr id=' +data[item].id+' title="לחץ כאן כדי לראות את פרטי המכרז" onclick="auctionClicked('+data[item].id + ');">'
					 +'<td>'+ data[item].eventType.Name+ '</td>'
					 +'<td>'+ formattedDate(data[item].eventDate) +'</td>'
					 +'<td>'+data[item].minAge+'</td>'
					 +'<td>'+data[item].area.Name+'</td>'
					 +'<td>'+data[item].offerNumber+counterDescription +'</td></tr>';
		}
		
		table += '</tbody></table>';
		$(".all-auctions-container").append($(table));
	}
			
	function auctionClicked(auctionID)
	{
		  $.ajax({
		        url: "AuctionOfferItemClicked",
		        type: "post",
		        dataType: 'json',
		        data:{ClickedItemType: "AuctionItemClicked", ItemID:auctionID},
		        success: function(data) {
		        	 console.log("redirect to auction management page");
		             window.location.href = 'AuctionManagement.jsp';
		            },
		        error: function(data){
		            	console.log("error");}
		    });
	}
	
	function getAllAuctions(){
	    $.ajax({
	        url: "GetDBData",
	        type: "post",
	        dataType: 'json',
	        data: {RequestType: "DBDataAllAuctions"},
	        success: function(data) {
				  showAuctions(data);
	        },
	        error: function(data){
	            	console.log("error");}
	    });
	}
	
	$(function onLoad(){
		getAllAuctions();
	});
	
	</script>	
	</body>
</html>