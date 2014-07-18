	$(function() {
		var date = new Date();
		var currentMonth = date.getMonth();
		var currentDate = date.getDate();
		var currentYear = date.getFullYear();
		$('#datepicker').datepicker({
			minDate: new Date(currentYear, currentMonth, currentDate)});
		ajaxAuctionFormDBData();
	});
	
	function loadListDataFromDB(data, listName)
	{
		console.log("adding"+ listName);
		 $.each(data, function(index, val) {
		            $('<option value="'+val.id+'">' + val.Name + '</option>').appendTo($(listName)) ;
		        });	
	}
	
	function loadCheckboxListDataFromDB(data, listName)
	{
		console.log("adding"+ listName);
		 $.each(data, function(index, val) {
		            $('<input id="'+val.id+'" type="checkbox" name="'+listName+'" value=' + val.id + '/>' +
		            		'<label style="padding-right:10px; font-weight:normal;" for="'+val.id+'">' + val.Name + '</label></br>').appendTo($(listName)) ;
		        });	
	}

	function ajaxAuctionFormDBData() {
	    $.ajax({
	        url: "GetDBData",
	        type: "post",
	        dataType: 'json',
	        data:{RequestType: "GetDBData-NewAuction"},
	        success: function(data) {
	            if (data != null) {
	                console.log("Loading data from DB");  
	                loadListDataFromDB(data.eventTypes, '#event-type' );
	                loadCheckboxListDataFromDB(data.musicStyles, '#musicStyle' );
	                loadListDataFromDB(data.area, '#area' );
	                loadListDataFromDB(data.businessType, '#business-type' );
	                loadListDataFromDB(data.certainBusiness, '#certain-business' );
	                loadListDataFromDB(data.sittsType, '#sitts-type' );
	            }},
	        error: function(data){
	            	console.log("error");}
	    });
	}

	function mandatoryEventDateCheck() {
		
		if ($("#datepicker").datepicker("getDate") == null)
		{
			$("#event-date-error").removeClass().addClass('error-label-displayed');
			$("#datepicker").focus();
			return false;
		}
		else
		{
			$("#event-date-error").removeClass().addClass('error-label-hidden');
			return true;
		}
	}
	
	function newAuctionClicked()
	{
		if (mandatoryFieldsCheck()== true)
		{
			ajaxNewAuctionCreation();
		}
	}
	
	function mandatoryFieldsCheck()
	{
		var res= new Boolean();
		res= true;
		res= mandatoryCheck("general-description", "general-description-error") && res;
		res= mandatoryCheck("min-age" , "min-age-error") && res;
		res= mandatoryCheck("guests-quantity","guests-quantity-error") && res;
		res= mandatoryEventDateCheck() && res;
		return res;
	}
	
	
	function mandatoryCheck(fieldName,errorLabelID)
	{
		if (($("#" +fieldName).val() == null) || ($("#" +fieldName).val().trim() == ""))
		{
			$("#" +errorLabelID).removeClass().addClass('error-label-displayed');
			$("#" +fieldName).focus();

			return false;
		}
		else
		{
			$("#" +errorLabelID).removeClass().addClass('error-label-hidden');
			return true;			
		}
	}
	
	function replaceAll(find, replace, str) 
	{
		  return str.replace(new RegExp(find, 'g'), replace);
	}
	
	function ajaxNewAuctionCreation()
	{	
		var musicStyles= $('#musicStyle').find('input').serialize();
		var final= replaceAll("%23musicStyle=","",musicStyles);
		var final2= replaceAll("%2F","",final); 
		
		var eventType=  $('#event-type')[0].value;
		var date= $('#datepicker')[0].value;
		var isDateFlexible= $('#is-flexible-date')[0].value;
		var guestsQuantity=  $('#guests-quantity')[0].value;
		var exceptionsDescription= $('#exceptions-description')[0].value;
		var minAge= $('#min-age')[0].value;
		var area= $('#area')[0].value;
		var businessType= $('#business-type')[0].value;
		var certainBusiness= $('#certain-business')[0].value;
		var smoking= $('#smoking')[0].value;
		var sittsType= $('#sitts-type')[0].value;
		var generalDescription= $('#general-description')[0].value;
		
	    $.ajax({
	        url: "NewAuction",
	        type: "post",
	        dataType: 'json',
	        data: {EventType: eventType, MusicStyleList: final2, Datepicker: date, IsFlexibleDate: isDateFlexible,
	        	GuestsQuantity:guestsQuantity, ExceptionsDescription: exceptionsDescription, MinAge: minAge,
	        	Area: area, BusinessType: businessType, CertainBusiness:certainBusiness, Smoking: smoking, SittsType:sittsType, GeneralDescription:generalDescription},
	        success: function(data){
	        	console.log("Auction creation succedded");}
	        });
		
	}
	