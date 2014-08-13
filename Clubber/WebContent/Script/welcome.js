$(document).ready(
		function() {
			var date = new Date();
			var currentMonth = date.getMonth() + 1;
			var currentDay = date.getDate();
			var currentYear = date.getFullYear();
			var fullCurrentDateStr = currentYear + "-" + currentMonth + "-"
					+ currentDay;
			getMainLinesFromDB(fullCurrentDateStr);
		});

$(function() {
	var date = new Date();
	var currentMonth = date.getMonth();
	var currentDay = date.getDate();
	var currentYear = date.getFullYear();

	$('#datepicker').datepicker({
		onSelect : function(dateText, inst) {
			var day1 = $("#datepicker").datepicker('getDate').getDate();
			var month1 = $("#datepicker").datepicker('getDate').getMonth() + 1;
			var year1 = $("#datepicker").datepicker('getDate').getFullYear();
			var fullDate = year1 + "-" + month1 + "-" + day1;
			getMainLinesFromDB(fullDate);
		},
		minDate : new Date(currentYear, currentMonth, currentDay)
	});
});

function loadListDataFromDB(data, listName) {
	console.log("adding" + listName);
	$.each(data, function(index, val) {
		$('<option value="' + val.id + '">' + val.Name + '</option>').appendTo(
				$(listName));
	});
}

function loadCheckboxListDataFromDB(data, listName) {
	console.log("adding" + listName);
	$
			.each(
					data,
					function(index, val) {
						$(
								'<input id="'
										+ val.id
										+ '" type="checkbox" name="'
										+ listName
										+ '" value='
										+ val.id
										+ '/>'
										+ '<label style="padding-right:10px; font-weight:normal;" for="'
										+ val.id + '">' + val.Name
										+ '</label></br>')
								.appendTo($(listName));
					});
}

function dateChange (date){
	$('#lines_container').html('<img src="images/load.GIF" id="loader_image" >');
	getMainLinesFromDB(date);
}

function getMainLinesFromDB(fullDate) {
	console.log('function approched');
	
	console.log('Getting template from server');
	
	$.get( "patternTemplates/lineBox.tpl", function( data ) {
		 var lineTemplate = data;
		  console.log('template was loaded');
		  
		  var dataRequest = $.ajax({
				url : "GetDBData",
				type : "POST",
				dataType : 'json',
				data : {
					RequestType : "GetDBData-WelcomeLines",
					InDate : fullDate
					}
				});
		  
		  dataRequest.done(function(returnedData){
			  $.each(returnedData, function(){
				  console.log($(this)[0]);
				  $('#temp_container').html(lineTemplate);
				  var temp_template = $('#temp_container');
				  temp_template.find('.line_box_place').html($(this)[0]['m_StreetId']['Name']+' '+$(this)[0]['m_HouseNumber']+', '+$(this)[0]['m_CityId']['Name'] );
				  temp_template.find('.line_box_date').html();
				  temp_template.find('.line_box_hour').html();
				  temp_template.find('.line_box_line').html($(this)[0]['m_Lines'][0]['description']);
				  
				  
				  temp_template.find('.line_box_name').html($(this)[0]['m_Name']);
				  
				  
				  $('#lines_container').append($('#temp_container').html());
				  console.log($(this)[0].m_Name)
			  });
			  $('#temp_container').html('');
		  });
		 
		  
		});
	
	
	
	
	/*dataRequest.done(function(data) {
			console.log(data);
			if (data != null) {
				console.log("Loading data from DB");
				console.log(fullDate);
				//PrintWelcomeLinesTable(data);
			}
		},
		error : function(data) {

			console.log("error");
		}
	});*/
$('#loader_image').remove();
}
function PrintWelcomeLinesTable(data) {
	var $Lines = $('#Lines_Div');
	var currDate = 0;
	$Lines.html("");
	if (data.length > 0) {
		for (var i = 0; i < data.length; i++) {
			if (currDate != data[i].m_Lines[0].startDate) {
				$Lines.append('<div class = "hebrewDate">'
						+ dateConvertor(data[i].m_Lines[0].startDate)
						+ '</div></br>');
			}
			$Lines.append('<div class = "line_title">'
					+ data[i].m_Lines[0].m_LineName + ' ' + data[i].m_Name	+ '</div>');
			$Lines.append('<div class = "PicCell">' + ' Pic ' + '</div>');
			$Lines.append('<div class = "AgeCell">' + data[i].m_Lines[0].minAge	+ '</div>');
			$Lines.append('<div class = "DetailsCell">'	+ data[i].m_Lines[0].description + '</div>');
			$Lines.append('</div>');

			currDate = data[i].m_Lines[0].startDate;
		}
	} else {
		$Lines.append('<h1>אין אירועים זמינים</h1>');
	}
	$Lines.append('</div>');
}

function ajaxAuctionFormDBData() {
	$.ajax({
		url : "GetDBData",
		type : "post",
		dataType : 'json',
		data : {
			RequestType : "GetDBData-NewAuction"
		},
		success : function(data) {
			if (data != null) {
				console.log("Loading data from DB");
				loadListDataFromDB(data.eventTypes, '#event-type');
				loadCheckboxListDataFromDB(data.musicStyles, '#musicStyle');
				loadListDataFromDB(data.area, '#area');
				loadListDataFromDB(data.businessType, '#business-type');
				loadListDataFromDB(data.certainBusiness, '#certain-business');
				loadListDataFromDB(data.sittsType, '#sitts-type');
			}
		},
		error : function(data) {
			console.log("error");
		}
	});
}

function newAuctionClicked() {
	if (mandatoryFieldsCheck() == true) {
		ajaxNewAuctionCreation();
	}
}

function printMusicStyle(i_MusicStylesArray){
	var musicStr = "";
	for(var i = 0 ; i < MusicStylesArray.length; i++)
		{
			musicStr = musicStr + ", " + i_MusicStylesArray[i]; 
		}
	return musicStr;
}
function mandatoryFieldsCheck() {
	var res = new Boolean();
	res = true;
	res = mandatoryCheck("general-description", "general-description-error")
			&& res;
	res = mandatoryCheck("min-age", "min-age-error") && res;
	res = mandatoryCheck("guests-quantity", "guests-quantity-error") && res;
	res = mandatoryEventDateCheck() && res;
	return res;
}

function mandatoryCheck(fieldName, errorLabelID) {
	if (($("#" + fieldName).val() == null)
			|| ($("#" + fieldName).val().trim() == "")) {
		$("#" + errorLabelID).removeClass().addClass('error-label-displayed');
		$("#" + fieldName).focus();

		return false;
	} else {
		$("#" + errorLabelID).removeClass().addClass('error-label-hidden');
		return true;
	}
}

function replaceAll(find, replace, str) {
	return str.replace(new RegExp(find, 'g'), replace);
}

function dayConvertor(i_Day)
{
	var hebrewDaysNames = new Array("ראשון", "שני", "שלישי", "רביעי", "חמישי",
			"שישי", "שבת");
	return hebrewDaysNames[(i_day - 1)];
}
function dateConvertor(i_Date) {
	var hebrewDaysNames = new Array("ראשון", "שני", "שלישי", "רביעי", "חמישי",
			"שישי", "שבת");
	var date = new Date(i_Date), day = date.getDay(), fullYear = date.getFullYear(), month = date.getMonth(), dayInMonth = date
			.getDate();

	// המערכים שמאפשרים את המרת השמות לשמות עבריים
	
	var hebrewMonthsNames = new Array("דצמבר", "נובמבר", "אוקטובר", "ספטמבר",
			"אוגוסט", "יולי", "יוני", "מאי", "אפריל", "מרץ", "פברואר", "ינואר");

	// מדפיס את היום
	var getDay = " יום " + hebrewDaysNames[day];

	// מדפיס את החודש
	var getMonth = hebrewMonthsNames[(11 - month)];

	// מדפיס את היום בחודש
	$("#dayInMonth").text(dayInMonth);

	// מדפיס את השנה המלאה
	$("#fullYear").text(fullYear);

	return (getDay + " " + dayInMonth + " " + getMonth + " " + fullYear);
}

function ajaxNewAuctionCreation() {
	var musicStyles = $('#musicStyle').find('input').serialize();
	var final = replaceAll("%23musicStyle=", "", musicStyles);
	var final2 = replaceAll("%2F", "", final);

	var eventType = $('#event-type')[0].value;
	var date = $('#datepicker')[0].value;
	var isDateFlexible = $('#is-flexible-date')[0].value;
	var guestsQuantity = $('#guests-quantity')[0].value;
	var exceptionsDescription = $('#exceptions-description')[0].value;
	var minAge = $('#min-age')[0].value;
	var area = $('#area')[0].value;
	var businessType = $('#business-type')[0].value;
	var certainBusiness = $('#certain-business')[0].value;
	var smoking = $('#smoking')[0].value;
	var sittsType = $('#sitts-type')[0].value;
	var generalDescription = $('#general-description')[0].value;

	$.ajax({
		url : "NewAuction",
		type : "post",
		dataType : 'json',
		data : {
			EventType : eventType,
			MusicStyleList : final2,
			Datepicker : date,
			IsFlexibleDate : isDateFlexible,
			GuestsQuantity : guestsQuantity,
			ExceptionsDescription : exceptionsDescription,
			MinAge : minAge,
			Area : area,
			BusinessType : businessType,
			CertainBusiness : certainBusiness,
			Smoking : smoking,
			SittsType : sittsType,
			GeneralDescription : generalDescription
		},
		success : function(data) {
			console.log("Auction creation succedded");
		}
	});

}