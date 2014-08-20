
package ClubberLogic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.catalina.connector.Request;

import Utlis.AuctionManagementData;
import Utlis.IdWithName;
import Utlis.LineManagementData;
import Utlis.NewAuctionData;

public class DAL {
	private static Connection conn;
	private static Statement stmt;
	
	public DAL()
	{
	}
	
	private static void connectToDBServer()
	{
		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/clubber_db?useUnicode=true&characterEncoding=UTF8", "root", "a");
			stmt= conn.createStatement();	
		} 
		catch (SQLException e) {
		e.printStackTrace();} catch (ClassNotFoundException e) {
			e.printStackTrace();
			disconnectFromDBServer();
		}	
	}
	
	private static void disconnectFromDBServer()
	{
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static List<AuctionData> getAllMyAuctionsData() throws ParseException
	{
		List<AuctionData> data= new LinkedList <AuctionData>();
		
		
		connectToDBServer();
		
		try {
			
			ResultSet rs = stmt.executeQuery("select auc.*, event_type.Name as event_type_name, COALESCE(counter,0) as counter from event_type,"
					+ " Auction auc left join (Select auction_id, count(id) as counter from  offers) offers1 on offers1.auction_id = auc.id where auc.Event_Type = event_type.id order by auc.Event_Date");
			
			while (rs.next())
			{
				AuctionData auction= new AuctionData();
				auction.setEventDate(rs.getDate("Event_Date"));				
				auction.setDescription(rs.getString("Description"));
				auction.setEventType(new IdWithName(rs.getInt("auc.Event_Type"),rs.getString("event_type_name") ));
				auction.setId(rs.getInt("id"));
				auction.setOfferNumber(rs.getInt("counter"));
				data.add(auction);
			}		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			disconnectFromDBServer();
		}
		return data;	
	}
	
	public static AuctionData getReviewedAuctionData(Integer currAuctionID )
	{
		AuctionData auction= new AuctionData();
		connectToDBServer();
		
		try {
			
			/*load auction data*/
			ResultSet rs = stmt.executeQuery("select auc.*, event_type.Name as event_type_name, auction_status.Name,businesses.Name, COALESCE(counter,0) as counter from event_type,"
					+ " Auction auc left join (Select auction_id, count(id) as counter from  offers) offers1 on offers1.auction_id = auc.id where auc.Event_Type = event_type.id and "
					+"auction_status.id = auc.Auction_Status and auc.Certain_Business= businesses.id order by auc.Event_Date");
			auction.setEventDate(rs.getDate("Event_Date"));				
			auction.setDescription(rs.getString("Description"));
			auction.setEventType(new IdWithName(rs.getInt("auc.Event_Type"),rs.getString("event_type_name") ));
			auction.setId(rs.getInt("id"));
			auction.setOfferNumber(rs.getInt("counter"));
			auction.setAuctionStatus(new IdWithName(rs.getInt("auc.Auction_Status"),rs.getString("auction_status.Name")));
			auction.setCertainBusiness(new IdWithName(rs.getInt("auc.Certain_Business"),rs.getString("businesses.Name")));
			auction.setDateFlexible(rs.getBoolean("Is_Date_Flexible"));
			auction.setExceptionsDescription(rs.getString("Exceptions_Description"));
			auction.setGuestesQuantiny(rs.getInt("Guestes_Quantiny"));
			auction.setSmoking(rs.getBoolean("Smoking"));
			auction.setMinAge(rs.getInt("Minimum_Age"));

			/*load auction areas*/
			/*load auction music styles*/
			/*load auction business types*/
			/*load auction sitting types*/
			rs = stmt.executeQuery("select * from areas, treats where treats.id= ot.treat_id and ot.Offer_id=");
			
			while (rs.next())
			{
				
			}			
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			disconnectFromDBServer();
		}

		return auction;
	}
	
	
	public static OfferData getReviewedOfferData(Integer currOfferID, Integer currAuctionID )
	{
		OfferData offer= new OfferData();
		connectToDBServer();
		
		try {
			
			/*load offer data*/
			ResultSet rs = stmt.executeQuery("select * from offer_status, Line,Offers off, Users where off.Line_id = Line.id and Users.id= off.Pr_id and offer_status.id=off.Offer_Status and off.ID="+currOfferID);
			while (rs.next())
			{
				offer.setOfferStatusId(new IdWithName(rs.getInt("offer_status.id"),rs.getString("Offer_Status.displayName")));
				offer.setId(rs.getInt("off.id"));	
				offer.setDescription(rs.getString("off.Description"));
				offer.setExpirationDate(rs.getDate("off.Expiration_Date"));
				offer.setAuctionId(currOfferID);
				offer.setLineId(new IdWithName(rs.getInt("off.Line_id"), rs.getString("Line.Name")));
				offer.setMaxArrivalHour(rs.getTime("off.Max_Arrival_Hour"));
				offer.setPrId(new IdWithName(rs.getInt("off.Pr_id"), rs.getString("Users.First_Name") + " " + rs.getString("Users.Last_Name")));
				offer.setSubmitDate(rs.getDate("off.Created_On"));
			}
			
			/*load offer treats*/
			rs = stmt.executeQuery("select * from offer_treats ot, treats where treats.id= ot.treat_id and ot.Offer_id=" +currOfferID);
			
			while (rs.next())
			{
				offer.getOfferTreats().add(new IdWithName(null, rs.getString("treats.Name")));
			}			
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			disconnectFromDBServer();
		}

		return offer;
	}
	
	public static List <UserMessagesData> getAllMassages(Integer currAuctionID)
	{
		List <UserMessagesData> messages= new LinkedList<UserMessagesData>();
		connectToDBServer();
		
		try {
			
			/*load messages*/
			ResultSet rs = stmt.executeQuery("select * from Messages where Messages.Auction_id="+currAuctionID);
			while (rs.next())
			{
				UserMessagesData message= new UserMessagesData();
				message.setAuctionId(currAuctionID);
				message.setCreatedOn(rs.getTimestamp("Created_On"));
				message.setDescription(rs.getString("Description"));
				message.setFromUserId(rs.getInt("From_User_id"));
				message.setId(rs.getInt("id"));
				message.setToUserId(rs.getInt("To_User_id"));
				
				messages.add(message);
			}
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			disconnectFromDBServer();
		}
		
		return messages;
		
	}
	
	public static boolean addNewMessage(String description, Integer auctionId) throws Exception
	{
		connectToDBServer();
		
		try {
			//created on = now :
			Calendar calendar = Calendar.getInstance();
			Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
			
			//create new message in db:
			String sqlMessageInsertion= String.format("insert into Messages values(%d,%d,%d,%d,'%s','%s')",
					null,1,2,auctionId,currentTimestamp,description);
			
			stmt.executeUpdate(sqlMessageInsertion);
			return true;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		finally
		{
			disconnectFromDBServer();
		}
	}
	
	public static AuctionManagementData getAllAuctionOffersData(Integer currAuctionID) throws ParseException
	{
		List<OfferData> offers= new LinkedList <OfferData>();
		AuctionData auction = new AuctionData();
		AuctionManagementData data;
		connectToDBServer();
		
		try {
			
			ResultSet rs = stmt.executeQuery("select * from Line,Offers off, Users where off.Line_id = Line.id and Users.id= off.Pr_id and off.Auction_ID="+currAuctionID + " order by off.id");
			while (rs.next())
			{
				OfferData offer= new OfferData();
				offer.setId(rs.getInt("off.id"));
				offer.setAuctionId(currAuctionID);	
				offer.setDescription(rs.getString("off.Description"));
				offer.setExpirationDate(rs.getDate("off.Expiration_Date"));
				offer.setId(rs.getInt("off.id"));
				offer.setLineId(new IdWithName(rs.getInt("off.Line_id"), rs.getString("Line.Name")));
				offer.setMaxArrivalHour(rs.getTime("off.Max_Arrival_Hour"));
				offer.setPrId(new IdWithName(rs.getInt("off.Pr_id"), rs.getString("Users.First_Name") + " " + rs.getString("Users.Last_Name")));
				offer.setSubmitDate(rs.getDate("off.Created_On"));
				offers.add(offer);
			}
			

			rs = stmt.executeQuery("select * from Auction auc ,Areas, event_type where auc.id=" +currAuctionID+ " and Areas.id= auc.area  and auc.Event_Type = event_type.id");
			while (rs.next())
			{
				auction.setId(currAuctionID);
				auction.setArea(new IdWithName(rs.getInt("Areas.id"),rs.getString("Areas.Name")));
				auction.setAuctionStatus(new IdWithName (rs.getInt("Auction_Status"),null));
				auction.setDateFlexible(rs.getBoolean("Is_Date_Flexible"));
				auction.setDescription(rs.getString("auc.Description"));
				auction.setEventDate(rs.getDate("Event_Date"));
				auction.setEventType(new IdWithName (rs.getInt("auc.Event_Type"),rs.getString("event_type.Name")));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			disconnectFromDBServer();
		}
		
		data= new AuctionManagementData(offers,auction);
		return data;	
	}
	
	
	public static NewAuctionData getAllNewAuctionData ()
	{
		connectToDBServer();
		NewAuctionData data= new NewAuctionData();
		String query;
			
		query = "Select * from Event_Type;";
		data.setEventTypes(GetIdAndNameData(query));
		
		query = "Select * from Music_Style;";
		data.setMusicStyles(GetIdAndNameData(query));
		
		query = "Select * from Areas;";
		data.setArea(GetIdAndNameData(query));
		
		query = "Select * from Business_Type;";
		data.setBusinessType(GetIdAndNameData(query));
		
		query = "Select * from Businesses;";
		data.setCertainBusiness(GetIdAndNameData(query));
		
		query = "Select * from Sitts_Type;";
		data.setSittsType(GetIdAndNameData(query));
		
		disconnectFromDBServer();
	
		
		return data;
	}
	
	public static LinkedList<IdWithName> GetIdAndNameData(String query)
	{
		
		LinkedList<IdWithName> data= new LinkedList<IdWithName> ();  
		try 
		{
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next())
			{
				data.add(new IdWithName(rs.getInt("Id"), rs.getString("Name")));
			}
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		} 

		return data;
	}
	
	public static void addNewAuction(AuctionData auction) throws Exception
	{
		connectToDBServer();
		
		DateFormat df = new SimpleDateFormat("dd-MMM-yy HH:mm:ss a");
		String date= df.format(auction.getEventDate());
		Date date2= df.parse(date);
		java.sql.Date sqlDate = new java.sql.Date(date2.getTime());
		
		try {
			//create new auction in db:
			String sqlAuctionInsertion= String.format("insert into Auction values(%d,%d,'%s','%s',%d,'%s','%s',%d,%d,'%s',%d,'%s',%d,%d, %s)",
					null,auction.getMinAge(),auction.getExceptionsDescription(),auction.getGuestesQuantiny(),getValidID(auction.getEventType()), 
					sqlDate,auction.getIsDateFlexible(),getValidID( auction.getArea()),
					getValidID(auction.getCertainBusiness()),auction.getDescription(),getValidID(auction.getDetailsToDisplay()),
					auction.isSmoking(), getValidID(auction.getAuctionStatus()),auction.getCreatedBy(), null);
			
			stmt.executeUpdate(sqlAuctionInsertion, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs= stmt.getGeneratedKeys();
			rs.next();
			Integer auctionId= rs.getInt(1);
			//add relevant records to auction music style table:
			for(IdWithName item: auction.getMusicStyle())
			{
				String sqlMusicStyles= String.format("insert into Auction_Music_Style values(%d,%d,%d)", null,auctionId , item.getId());
				stmt.executeUpdate(sqlMusicStyles);
			}
			
			//add relevant records to auction business type table:
			for(IdWithName item: auction.getBusinessType())
			{
				String sqlBusinessType= String.format("insert into auction_business_type values(%d,%d,%d)", null,auctionId , item.getId());
				stmt.executeUpdate(sqlBusinessType);
			}	
			
			//add relevant records to auction sits type table:
			for(IdWithName item: auction.getSittsType())
			{
				String sqlSittsType= String.format("insert into auction_sits_type values(%d,%d,%d)", null,auctionId , item.getId());
				stmt.executeUpdate(sqlSittsType);
			}	
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally
		{
			disconnectFromDBServer();
		}
	}


	private static Integer getValidID(IdWithName fieldToCheck)
	{
		Integer res=null;
		
		if (fieldToCheck != null)
		{
			res = fieldToCheck.getId();
		}
		
		return res;
	}
	
	/*
	 * Maayan: 	24/4/2014
	 * 			* add function - insertNewUser
	 */
	public static boolean insertNewUser(UserData userData, UserType userType) throws ParseException {
		
		boolean isSucceed = true;

		connectToDBServer();

		DateFormat df = new SimpleDateFormat("dd-MMM-yy HH:mm:ss a");
		String date= df.format(userData.getBirthDate());
		Date date2= df.parse(date);
		java.sql.Date sqlDate = new java.sql.Date(date2.getTime());		
		
		String sql= "INSERT INTO USERS(User_Type, First_Name, Last_Name, Gender, Phone_Number, Email, Password, Birth_Date)"
				+ " VALUES('"+userType+"','"+userData.getFirstName()+"','"+userData.getLastName()+"','"+userData.getGender()+"','"+userData.getPhoneNumber()+"','"+userData.getEmail()+"','"+userData.getPassword()+"','"+sqlDate+"')";
		try {
			stmt.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			isSucceed = false;
			e.printStackTrace();
		}
		finally
		{
			disconnectFromDBServer();
		}		
		
		return isSucceed;
	}

	/*
	 * Maayan: 	24/4/2014
	 * 			* add function - getUserLoginAttempts
	 */	
	public static int getUserLoginAttempts(String email) {
	
		int loginAttempts = 0;
		
		connectToDBServer();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT Login_Attempts FROM clubber_db.users;");
			while (rs.next())
			{
				loginAttempts = rs.getInt("Login_Attempts");
			}		
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		finally{
			disconnectFromDBServer();
		}
				
		return loginAttempts;
	}

	
	/*
	 * Orel & Maayan: 	25/4/2014
	 * 			* add function - increaseLoginAttemptsDB
	 */		
	public static void increaseLoginAttemptsDB(String email) {
		
		connectToDBServer();
		
		try {
			stmt.executeUpdate("UPDATE clubber_db.users "
							   + "SET Login_attempts = Login_attempts + 1 "
							   + "WHERE Email ='" + email + "'");	
		} 
		catch (SQLException e) {
			e.printStackTrace();
			
		}
		finally{
			disconnectFromDBServer();
		}
	}
	
	public static void updateLoginAttemptTimeStamp(String email)
	{
		connectToDBServer();
		
		Date now = new Date();
		long expiredDate = now.getTime() + (3600 * 3 * 1000); 
        
		try {
			stmt.executeUpdate("UPDATE clubber_db.users "
							   + "SET LoginAttemptTimeStamp = " + expiredDate
							   + " WHERE Email ='" + email + "'");	
		} 
		catch (SQLException e) {
			e.printStackTrace();
			
		}
		finally{
			disconnectFromDBServer();
		}
	}
	
	public static long getLoginAttemptTimeStamp(String email)
	{
		long loginAttemptsTimeStamp = 0;

		connectToDBServer();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT LoginAttemptTimeStamp FROM clubber_db.users");
			while (rs.next())
			{
				loginAttemptsTimeStamp = rs.getLong("LoginAttemptTimeStamp");
			}		
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		finally{
			disconnectFromDBServer();
		}
		
		return loginAttemptsTimeStamp;		
	}

	/*
	 * Maayan: 	24/4/2014
	 * 			* add function - isEmailUnique
	 */			
	public static boolean isEmailUnique(String userEmail) {

		boolean isEmailUnique = true;
		
		connectToDBServer();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT Email "
										   + "FROM clubber_db.users "
										   + "WHERE Email ='" + userEmail + "'");
			if (rs.next())
			{
				isEmailUnique = false;
			}		
		} 
		catch (SQLException e) {
			e.printStackTrace();
			isEmailUnique = false;
			
		}
		finally{
			disconnectFromDBServer();
		}
				
		return isEmailUnique;		
		
	}	
	
	/*
	 * Orel & Maayan: 25/4/2014
	 * 				  * add function - isEmailExists
	 */
	public static boolean isEmailExists(String email) {
		
		boolean isExists = false;
		
		connectToDBServer();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT Email "
										   + "FROM clubber_db.users "
										   + "WHERE Email ='" + email + "'");
			if (rs.next())
			{
				isExists = true;
			}		
		} 
		catch (SQLException e) {
			e.printStackTrace();
			
		}
		finally{
			disconnectFromDBServer();
		}
		
		return isExists;
	}
	
	/*
	 * Orel & Maayan: 25/4/2014
	 * 				  * add function - isPasswordMatchesEmail
	 */
	public static boolean isPasswordMatcheEmail(String email, String password) {
		
		boolean isMatch = true;
		
		connectToDBServer();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT Password "
										   + "FROM clubber_db.users "
										   + "WHERE Email ='" + email + "'");
			if (rs.next())
			{
				String userPassword = rs.getString("Password");
				
				if(userPassword.equals(password) == false)
				{
					isMatch = false;
				}
			}		
		} 
		catch (SQLException e) {
			e.printStackTrace();
			isMatch = false;
			
		}
		finally{
			disconnectFromDBServer();
		}
		
		return isMatch;
	}

	public static void updateUserPassword(String email, String password) {
		// TODO Auto-generated method stub
		
		connectToDBServer();
		
		try {
			stmt.executeQuery("UPDATE clubber_db.users "
						   + "SET Password =" + password
						   + "WHERE Email ='" + email + "'");	
		} 
		catch (SQLException e) {
			e.printStackTrace();
			
		}
		finally{
			disconnectFromDBServer();
		}
	}
	
	public static PR getUserProfileData(String email)
	{
		
		PR pr = new PR();
		connectToDBServer();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT * "
					   + "FROM clubber_db.users "
					   + "WHERE Email ='" + email + "'");
			
			while (rs.next())
			{
				pr.setFirstName(rs.getString("First_Name"));
				pr.setLastName(rs.getString("Last_Name"));
				pr.setGender(rs.getString("Gender"));
				pr.setPhoneNumber(rs.getString("Phone_Number"));
				pr.setEmail(rs.getString("Email"));
				pr.setPassword(rs.getString("Password"));
				pr.setBirthDate(rs.getDate("Birth_Date"));
			}
			
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally{
			disconnectFromDBServer();
		}
		
		return pr;
		
	}
	
	public static boolean updateUserDetails(UserData userData) throws ParseException {
		// TODO Auto-generated method stub
		
		boolean isSucceed = true;
		
		connectToDBServer();
		
		DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
		String date= df.format(userData.getBirthDate());
		Date date2= df.parse(date);
		java.sql.Date sqlDate = new java.sql.Date(date2.getTime());

		String sql = "UPDATE clubber_db.users "
				   + "SET First_Name = '" + userData.getFirstName() + "'"
				   + ", Last_Name = '" + userData.getLastName() + "'"
				   + ", Gender = '" + userData.getGender() + "'"
				   + ", Phone_Number = '" + userData.getPhoneNumber() + "'"
				   + ", Password = '" + userData.getPassword() + "'"
				   + ", Birth_Date = '" + sqlDate + "'"
				   + " WHERE Email ='" + userData.getEmail() + "'";
		
		try {
			stmt.executeUpdate(sql);	
		} 
		catch (SQLException e) {
			isSucceed = false;
			e.printStackTrace();
			
		}
		finally{
			disconnectFromDBServer();
		}
		
		return isSucceed;
	}
	
	public static UserReviews getPrProfileReview(String email) {
		// TODO Auto-generated method stub
		
		UserReviews reviews = new UserReviews();
		List<UserReviews> totalReviews = new ArrayList<>();
		
		connectToDBServer();

		try {

			ResultSet rs = stmt.executeQuery("SELECT Availability, Realiability, Treats "
					   + "FROM clubber_db.pr_review "
					   + "WHERE PR_id IN "
					   + "(SELECT id from clubber_db.users "
					   + "WHERE Email ='" + email + "')");
			
			
			while (rs.next())
			{
				UserReviews temp = new UserReviews();
				temp.setAvailability(rs.getInt("Availability"));
				temp.setRealiability(rs.getInt("Realiability"));
				temp.setTreats(rs.getInt("Treats"));
				
				totalReviews.add(temp);
			}
			
			for (UserReviews userReviews : totalReviews) {
				reviews.setAvailability(reviews.getAvailability()+userReviews.getAvailability());
				reviews.setRealiability(reviews.getRealiability()+userReviews.getRealiability());
				reviews.setTreats(reviews.getTreats()+userReviews.getTreats());
			
			}
			
			reviews.setAvailability(reviews.getAvailability()/totalReviews.size());
			reviews.setRealiability(reviews.getRealiability()/totalReviews.size());
			reviews.setTreats(reviews.getTreats()/totalReviews.size());
			reviews.setGeneral((reviews.getAvailability() + reviews.getRealiability() + reviews.getTreats()) / 3);
		
		} 
		catch (SQLException e) {
			e.printStackTrace();
			
		}
		finally{
			disconnectFromDBServer();
		}
		
		
		return reviews;
	}

	public static UserReviews getClientProfileReview(String email) {
		// TODO Auto-generated method stub
		UserReviews reviews = new UserReviews();
		List<UserReviews> totalReviews = new ArrayList<>();
		
		connectToDBServer();

		try {

			ResultSet rs = stmt.executeQuery("SELECT Punctuality, Realiability "
					   + "FROM clubber_db.customer_review "
					   + "WHERE Customer_id IN "
					   + "(SELECT id from clubber_db.users "
					   + "WHERE Email ='" + email + "')");
			
			while (rs.next())
			{
				UserReviews temp = new UserReviews();
				temp.setPunctuality(rs.getInt("Punctuality"));
				temp.setRealiability(rs.getInt("Realiability"));
				
				totalReviews.add(temp);
			}
			
			for (UserReviews userReviews : totalReviews) {
				reviews.setPunctuality(reviews.getPunctuality()+userReviews.getPunctuality());
				reviews.setRealiability(reviews.getRealiability()+userReviews.getRealiability());
			
			}
			
			reviews.setPunctuality(reviews.getPunctuality()/totalReviews.size());
			reviews.setRealiability(reviews.getRealiability()/totalReviews.size());
			reviews.setGeneral((reviews.getPunctuality() + reviews.getRealiability()) / 2);
		
		} 
		catch (SQLException e) {
			e.printStackTrace();
			
		}
		finally{
			disconnectFromDBServer();
		}
		
		return reviews;

	}

	public static List<LineManagementData> getCustomerRecomendedLines(String email) {

		List<LineManagementData> lines = null;
		
		connectToDBServer();

		try {

			ResultSet rs = stmt.executeQuery("select * "
											+ "From users U, auction A, line L, businesses B, city C, streets S "
											+ "where email = '" + email + "' and "
											+ "U.id = A.Created_By and "
											+ "A.area = B.Area and "
											+ "C.Area_id = A.area and "
											+ "S.City_id = C.id and "
											+ "S.id = B.Street and "
											+ "B.id = L.Business_id");
			
			while (rs.next())
			{
				
				lines = new ArrayList<>();
				LineData lineData = new LineData();
				BusinessData businessData = new BusinessData();
				
				lineData.setId(rs.getInt("L.id"));
				lineData.setM_LineName(rs.getString("L.Name"));
				lineData.setBusiness(new IdWithName(rs.getInt("L.Business_id"), rs.getString("B.Name")));
				lineData.setMinAge(rs.getInt("L.Min_Age"));
				lineData.setDescription(rs.getString("L.Description"));
				lineData.setEntranceFee(rs.getString("L.Entrance_Fee"));
				lineData.setDj(rs.getString("L.DJ"));
				lineData.setOpeningHour(rs.getTime("L.Opening_Hour").toString());
				//line.setPhoto(rs.getString("line_photo"));
				
				businessData.setM_Id(rs.getInt("B.id"));
				businessData.setM_Name(rs.getString("B.Name"));
				businessData.setM_StreetId(new IdWithName(rs.getInt("S.id"), rs.getString("S.Name")));
				businessData.setM_CityId(new IdWithName(rs.getInt("C.id"), rs.getString("C.Name")));
				businessData.setM_HouseNumber(rs.getInt("B.Structure_Number"));

				LineManagementData lineManagementData = new LineManagementData(lineData, businessData);
				lines.add(lineManagementData);
			}
		
		} 
		catch (SQLException e) {
			e.printStackTrace();
			
		}
		finally{
			disconnectFromDBServer();
		}
		
		return lines;
	}

	
	public static ArrayList<BusinessData> getWelcomeScreenEvents(String i_Date) throws ParseException {
		final String NEW_FORMAT = "yyyy-MM-dd";
		String parsedDate, dateStr;
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); ;
		Date date = formatter.parse(i_Date);
		DateFormat sqlFormatter = new SimpleDateFormat(NEW_FORMAT);
		parsedDate = sqlFormatter.format(date);
				   
		   ArrayList<BusinessData> data = new ArrayList<BusinessData>();
		// access date fields
			
		   
			connectToDBServer();
			
			try 
			{
				ResultSet rs = stmt.executeQuery("select * "
												+ "from line L, Businesses B, areas a, city c, streets s, business_type t "
												+ "where L.Business_id = B.id AND L.Line_End_Date >= '"+parsedDate+"' And"
											    +" B.Area = a.id and "
											    + "B.city = c.id and "
									 		    + "B.street = s.id and "
											    + "B.Business_Type = t.id");	

				while (rs.next())
				{
					dateStr = (rs.getString("L.Line_Start_Date"));
					BusinessData bData = new BusinessData();
					//set business data
					bData.setM_Id(rs.getInt("b.id"));
					bData.setM_Name(rs.getString("b.name"));
					bData.setM_StreetId(new IdWithName(rs.getInt("b.street"), rs.getString("s.Name")));
					bData.setM_HouseNumber(rs.getInt("b.structure_number"));
					bData.setM_PhoneNumber(rs.getString("b.Business_Phone_Number"));
					bData.setM_Description(rs.getString("b.Description"));
					bData.setM_BusinessTypeId(new IdWithName(rs.getInt("b.Business_Type"), rs.getString("t.Name")));
					bData.setM_CityId(new IdWithName(rs.getInt("b.city"), rs.getString("c.Name")));
					bData.setM_AreaId(new IdWithName(rs.getInt("b.area"), rs.getString("a.Name")));
					
					LineData lData = new LineData();
					date = formatter.parse(i_Date);
					lData.setM_LineName(rs.getString("L.name"));
					lData.setDescription(rs.getString("L.Description"));
					lData.setDj(rs.getString("L.DJ"));
					lData.setEntranceFee(rs.getString("L.entrance_fee"));
					lData.setMinAge(rs.getInt("L.Min_Age"));
					lData.setStartDate(date);
					
					bData.getM_Lines().add(lData);
					
					data.add(bData);
				}		
			} catch (SQLException e) {
				e.printStackTrace();
			}
			finally{
				disconnectFromDBServer();
			}
			return data;
		}

	public static void unlockUser(String email) {
		
		connectToDBServer();
		
		try {
			stmt.executeUpdate("UPDATE clubber_db.users "
							   + "SET Login_attempts = 0 , LoginAttemptTimeStamp = 0 "
							   + "WHERE Email ='" + email + "'");	
		} 
		catch (SQLException e) {
			e.printStackTrace();
			
		}
		finally{
			disconnectFromDBServer();
		}		
		
	}
	
	public static UserType getUserType(String email) {
		
		UserType userType = UserType.Client;
		
		connectToDBServer();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT User_Type "
										   + "FROM clubber_db.users "
										   + "WHERE Email ='" + email + "'");
			if (rs.next())
			{
				userType = UserType.valueOf(rs.getString("User_Type"));
			}		
		} 
		catch (SQLException e) {
			e.printStackTrace();
			
		}
		finally{
			disconnectFromDBServer();
		}
		
		return userType;
	}

	public static BusinessData getBusinessData(int businessId) throws ParseException{

		connectToDBServer();
			
		BusinessData businessData = null;
		
		try 
		{
			ResultSet rs = stmt.executeQuery("SELECT * "
					   + "FROM Businesses B, areas A, city C, streets S, business_type T "
					   + "WHERE B.id ='" + businessId + "' and "
					   +" B.area = A.id and "
					   + "B.city = C.id and "
					   + "B.street = S.id and "
					   + "B.Business_Type = T.id");			


			while (rs.next())
			{
				businessData = new BusinessData();
				//set business data
				businessData.setM_Id(rs.getInt("b.id"));
				businessData.setM_Name(rs.getString("b.name"));
				businessData.setM_StreetId(new IdWithName(rs.getInt("b.street"), rs.getString("s.Name")));
				businessData.setM_HouseNumber(rs.getInt("b.structure_number"));
				businessData.setM_PhoneNumber(rs.getString("b.Business_Phone_Number"));
				businessData.setM_Description(rs.getString("b.Description"));
				businessData.setM_BusinessTypeId(new IdWithName(rs.getInt("b.Business_Type"), rs.getString("t.Name")));
				businessData.setM_CityId(new IdWithName(rs.getInt("b.city"), rs.getString("c.Name")));
				businessData.setM_AreaId(new IdWithName(rs.getInt("b.area"), rs.getString("a.Name")));
			}
			
			ResultSet rs1 = stmt.executeQuery("SELECT * "
					   + "FROM Businesses B, line L "
					   + "WHERE B.id ='" + businessId + "' and "
					   + "B.id =  L.Business_id");
			
			while(rs1.next()) 
			{
				LineData lineData= new LineData();
				lineData.setM_LineName(rs1.getString("L.name"));
				lineData.setDescription(rs1.getString("L.Description"));
				lineData.setDj(rs1.getString("L.DJ"));
				lineData.setMinAge(rs1.getInt("L.Min_Age"));
				lineData.setStartDate(rs1.getDate("L.Line_Start_Date"));
			
				businessData.getM_Lines().add(lineData);
			}			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally{ 
			disconnectFromDBServer();
		}

		return businessData;
		
	}

	public static boolean updateBusinessDetails(BusinessData businessData) {
		// TODO Auto-generated method stub
		boolean isSucceed = true;
		
		connectToDBServer();
		
		String sql = "UPDATE businesses B, streets S "
				   + "SET B.Name = '" + businessData.getM_Name() + "'"
				   + ", B.Area = '" + businessData.getM_AreaId().getId() + "'"
				   + ", B.City = '" + businessData.getM_CityId().getId() + "'"
				   + ", B.Street = '" + businessData.getM_StreetId().getId() + "'"
				   + ", B.Structure_Number = '" + businessData.getM_HouseNumber() + "'"
   				   + ", B.Business_Type = '" + businessData.getM_BusinessTypeId().getId() + "'"
  				   + ", B.Business_Phone_Number = '" + businessData.getM_PhoneNumber() + "'"
  				   + ", B.Description = '" + businessData.getM_Description() + "'"
				   + ", S.City = '" + businessData.getM_CityId().getId() + "'"
				   + ", S.Name = '" + businessData.getM_StreetId().getName() + "'"  				   
				   + " WHERE B.id ='" + businessData.getM_Id() + "' and "
				   + " S.id ='" + businessData.getM_StreetId().getId() + "'";
		
		try {
			stmt.executeUpdate(sql);	
		} 
		catch (SQLException e) {
			isSucceed = false;
			e.printStackTrace();
			
		}
		finally{
			disconnectFromDBServer();
		}
		
		return isSucceed;
	}
	
	public static ArrayList<BusinessData> getAllBusinesses(){
		
		ArrayList<BusinessData> businesses = new ArrayList<>();
		
		connectToDBServer();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT * "
										   + "FROM businesses b, areas a, city c, streets s, business_type t "
										   + "WHERE b.area = a.id and "
										   + "b.city = c.id and "
										   + "b.street = s.id and "
										   + "b.Business_Type = t.id");

			while (rs.next())
			{
				BusinessData businessData = new BusinessData();
				businessData.setM_Id(rs.getInt("id"));
				businessData.setM_Name(rs.getString("name"));
				businessData.setM_StreetId(new IdWithName(rs.getInt("b.street"), rs.getString("s.Name")));
				businessData.setM_HouseNumber(rs.getInt("structure_number"));
				businessData.setM_PhoneNumber(rs.getString("Business_Phone_Number"));
				businessData.setM_Description(rs.getString("Description"));
				businessData.setM_BusinessTypeId(new IdWithName(rs.getInt("b.Business_Type"), rs.getString("t.name")));
				businessData.setM_CityId(new IdWithName(rs.getInt("b.city"), rs.getString("c.Name")));
				businessData.setM_AreaId(new IdWithName(rs.getInt("b.area"), rs.getString("a.Name")));
				
				businesses.add(businessData);
			}		
		} 
		catch (SQLException e) {
			e.printStackTrace();
			
		}
		finally{
			disconnectFromDBServer();
		}		
		
		return businesses;
		
	}

	public static ArrayList<IdWithName> getBusinessAreasData() {

		ArrayList<IdWithName> areasList = new ArrayList<>();
		
		connectToDBServer();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT * "
										   + "FROM areas ");

			while (rs.next())
			{
				int areadId = rs.getInt("id");
				String areaName = rs.getString("Name");
						
				IdWithName areaIdWithName = new IdWithName(areadId, areaName);
				areasList.add(areaIdWithName);
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			disconnectFromDBServer();
		}		
		
		return areasList;
	}

	public static ArrayList<IdWithName> getBusinessCitiesData(int areadId) {

		ArrayList<IdWithName> citiesList = new ArrayList<>();
		
		connectToDBServer();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT * "
										   + "FROM city "
										   + "WHERE Area_id = " + areadId);

			while (rs.next())
			{
				int cityId = rs.getInt("id");
				String cityName = rs.getString("Name");
						
				IdWithName cityIdWithName = new IdWithName(cityId, cityName);
				citiesList.add(cityIdWithName);
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			disconnectFromDBServer();
		}		
		
		return citiesList;
		
	}

	public static ArrayList<IdWithName> getBusinessesTypeData() {

		ArrayList<IdWithName> typesList = new ArrayList<>();
		
		connectToDBServer();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT * "
										   + "FROM business_type ");

			while (rs.next())
			{
				int typeId = rs.getInt("id");
				String typeName = rs.getString("Name");
						
				IdWithName typeIdWithName = new IdWithName(typeId, typeName);
				typesList.add(typeIdWithName);
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			disconnectFromDBServer();
		}		
		
		return typesList;
	}

	public static ArrayList<AuctionData> getAuctionsByPrLines(String email) {
		email = "orelsharabi8@gmail.com";
		ArrayList<AuctionData> auctionList = new ArrayList<>();
		int createdById = 0;

		connectToDBServer();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT * "
										   + "FROM auction A, line L, users U, businesses B, event_type ET, areas AR, details_to_display D, auction_status S "
										   + "WHERE L.PR_id =  U.id and "
										   + "U.Email = '" + email + "' and "
										   + "A.Minimum_Age > L.Min_Age and "
										   + "B.id = L.Business_id and "
										   + "B.Area = A.Area and "
										   + "B.Business_Type = A.Business_Type and "
										   + "A.Certain_Business = B.id and "
										   + "A.Event_Type = ET.id and "
										   + "A.Area = AR.id and "
										   + "A.Details_To_Display = D.id and "
										   + "A.Auction_Status = S.id and "
										   + "S.id = 1 and "										   
										   + "A.Event_Date BETWEEN L.Line_Start_Date AND L.Line_End_Date");

			while (rs.next())
			{
				AuctionData auctionData = new AuctionData();
				auctionData.setId(rs.getInt("A.id"));
				auctionData.setMinAge(rs.getInt("A.Minimum_Age"));
				auctionData.setExceptionsDescription(rs.getString("A.Exceptions_Description"));
				auctionData.setGuestesQuantiny(rs.getInt("A.Guestes_Quantiny"));
				auctionData.setEventType(new IdWithName(rs.getInt("ET.id"), rs.getString("ET.Name")));
				auctionData.setEventDate(rs.getDate("A.Event_Date"));
				auctionData.setDateFlexible(rs.getBoolean("A.Is_Date_Flexible"));
				auctionData.setArea(new IdWithName(rs.getInt("AR.id"), rs.getString("AR.Name")));
				auctionData.setCertainBusiness(new IdWithName(rs.getInt("B.id"), rs.getString("B.Name")));
				auctionData.setDescription(rs.getString("A.Description"));
				auctionData.setDetailsToDisplay(new IdWithName(rs.getInt("D.id"), rs.getString("D.Name")));
				auctionData.setSmoking(rs.getBoolean("A.Smoking"));
				auctionData.setAuctionStatus(new IdWithName(rs.getInt("S.id"), rs.getString("S.Name")));
				createdById = rs.getInt("A.Created_By");
				
				auctionList.add(auctionData);
			}
			
			for (int i = 0; i < auctionList.size(); i++) {
				// get all auction_id music style		
				ResultSet rs1 = stmt.executeQuery("SELECT * "
						   + "FROM line_music_style LMS, auction_music_style AMS, music_style MS "
						   + "WHERE AMS.Music_Style_Id = LMS.Music_Style_Id and "
						   + "AMS.Music_Style_Id = MS.id and "
						   + "LMS.Music_Style_Id = MS.id and "
						   + "AMS.Auction_Id = "+ auctionList.get(i).getId());

				List<IdWithName> musicStyles = new LinkedList<>();
				
				while(rs1.next())
				{
					IdWithName musicStyle = new IdWithName(rs1.getInt("MS.id"), rs1.getString("MS.Name"));
					musicStyles.add(musicStyle);
				}
				
				auctionList.get(i).setMusicStyle(musicStyles);
			}
			
			for (int i = 0; i < auctionList.size(); i++) {
			
				// get all auction_id businesses type
				ResultSet rs2 = stmt.executeQuery("SELECT * "
						   + "FROM auction A, business_type BT "
						   + "WHERE A.Business_Type = BT.id and "
				   		   + "A.id = "+ auctionList.get(i).getId());						   

				List<IdWithName> businessesType = new LinkedList<>();
				
				while(rs2.next())
				{
					IdWithName businessType = new IdWithName(rs2.getInt("BT.id"), rs2.getString("BT.Name"));
					businessesType.add(businessType);
				}
				
				auctionList.get(i).setBusinessType(businessesType);
			}
			
			for (int i = 0; i < auctionList.size(); i++) {
				
				// get all auction_id seats type
				ResultSet rs3 = stmt.executeQuery("SELECT * "
						   + "FROM auction A, sitts_type ST "
						   + "WHERE A.Seats_Type = ST.id and "
		   		   		   + "A.id = "+ auctionList.get(i).getId());				

				List<IdWithName> seatsType = new LinkedList<>();
				
				while(rs3.next())
				{
					IdWithName seatType = new IdWithName(rs3.getInt("ST.id"), rs3.getString("ST.Name"));
					seatsType.add(seatType);
				}
				
				auctionList.get(i).setSittsType(seatsType);
			}
			
			for (int i = 0; i < auctionList.size(); i++) {
				ResultSet rs4 = stmt.executeQuery("SELECT * "
						   + "FROM auction A, users U "
						   + "WHERE U.id = " +  createdById +" and "
				   		   + "A.id = "+ auctionList.get(i).getId());				

				if(rs4.next())
				{
					auctionList.get(i).setCreatedBy(new IdWithName(createdById, rs4.getString("U.First_Name")));
				}
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			disconnectFromDBServer();
		}		
		
		return auctionList;		
	}

	public static ArrayList<IdWithName> getMusicStyleData() {

		ArrayList<IdWithName> musicStyleList = new ArrayList<>();

		connectToDBServer();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT * "
										   + "FROM music_style MS ");

			while (rs.next())
			{
				Integer id = rs.getInt("MS.id");
				String name = rs.getString("MS.Name");
				IdWithName styleIdWithName = new IdWithName(id, name); 
				musicStyleList.add(styleIdWithName);
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			disconnectFromDBServer();
		}		
		
		return musicStyleList;				
	}

	public static ArrayList<AuctionData> getAllAuctions() {
		ArrayList<AuctionData> auctionList = new ArrayList<>();

		connectToDBServer();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT * "
										   + "FROM auction A, users U, businesses B, event_type ET, areas AR, details_to_display D, auction_status S "
										   + "WHERE U.id = A.Created_By and "
										   + "B.Business_Type = A.Business_Type and "
										   + "A.Event_Type = ET.id and "
										   + "A.Area = AR.id and "
										   + "A.Details_To_Display = D.id and "
										   + "S.id = 1 and "										   
										   + "A.Auction_Status = S.id");
			while (rs.next())
			{
				AuctionData auctionData = new AuctionData();
				auctionData.setId(rs.getInt("A.id"));
				auctionData.setMinAge(rs.getInt("A.Minimum_Age"));
				auctionData.setExceptionsDescription(rs.getString("A.Exceptions_Description"));
				auctionData.setGuestesQuantiny(rs.getInt("A.Guestes_Quantiny"));
				auctionData.setEventType(new IdWithName(rs.getInt("ET.id"), rs.getString("ET.Name")));
				auctionData.setEventDate(rs.getDate("A.Event_Date"));
				auctionData.setDateFlexible(rs.getBoolean("A.Is_Date_Flexible"));
				auctionData.setArea(new IdWithName(rs.getInt("AR.id"), rs.getString("AR.Name")));
				auctionData.setCertainBusiness(new IdWithName(rs.getInt("B.id"), rs.getString("B.Name")));
				auctionData.setDescription(rs.getString("A.Description"));
				auctionData.setDetailsToDisplay(new IdWithName(rs.getInt("D.id"), rs.getString("D.Name")));
				auctionData.setSmoking(rs.getBoolean("A.Smoking"));
				auctionData.setAuctionStatus(new IdWithName(rs.getInt("S.id"), rs.getString("S.Name")));
				auctionData.setCreatedBy(new IdWithName(rs.getInt("A.Created_By"), rs.getString("U.First_Name")));
				
				auctionList.add(auctionData);
			}
			
			for (int i = 0; i < auctionList.size(); i++) {
				// get all auction_id music style		
				ResultSet rs1 = stmt.executeQuery("SELECT * "
						   + "FROM line_music_style LMS, auction_music_style AMS, music_style MS "
						   + "WHERE AMS.Music_Style_Id = LMS.Music_Style_Id and "
						   + "AMS.Music_Style_Id = MS.id and "
						   + "LMS.Music_Style_Id = MS.id and "
						   + "AMS.Auction_Id = "+ auctionList.get(i).getId());

				List<IdWithName> musicStyles = new LinkedList<>();
				
				while(rs1.next())
				{
					IdWithName musicStyle = new IdWithName(rs1.getInt("MS.id"), rs1.getString("MS.Name"));
					musicStyles.add(musicStyle);
				}
				
				auctionList.get(i).setMusicStyle(musicStyles);
			}
			
			for (int i = 0; i < auctionList.size(); i++) {
				
				// get all auction_id businesses type
				ResultSet rs2 = stmt.executeQuery("SELECT * "
						   + "FROM auction A, business_type BT "
						   + "WHERE A.Business_Type = BT.id and "
						   + "A.id = "+ auctionList.get(i).getId());

				List<IdWithName> businessesType = new LinkedList<>();
				
				while(rs2.next())
				{
					IdWithName businessType = new IdWithName(rs2.getInt("BT.id"), rs2.getString("BT.Name"));
					businessesType.add(businessType);
				}
				
				auctionList.get(i).setBusinessType(businessesType);
			}
			
			for (int i = 0; i < auctionList.size(); i++) {
				
				// get all auction_id seats type
				ResultSet rs3 = stmt.executeQuery("SELECT * "
						   + "FROM auction A, sitts_type ST "
						   + "WHERE A.Seats_Type = ST.id and "
				   		   + "A.id = "+ auctionList.get(i).getId());				

				List<IdWithName> seatsType = new LinkedList<>();
				
				while(rs3.next())
				{
					IdWithName seatType = new IdWithName(rs3.getInt("ST.id"), rs3.getString("ST.Name"));
					seatsType.add(seatType);
				}
				
				auctionList.get(i).setSittsType(seatsType);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			disconnectFromDBServer();
		}		
	
		return auctionList;	
	}

	public static AuctionData getAuctionById(String i_AuctionId) {

		AuctionData auctionData = new AuctionData();
		connectToDBServer();
		
		try {
			ResultSet rs = stmt.executeQuery("SELECT * "
										   + "FROM auction A, users U, businesses B, event_type ET, areas AR, details_to_display D, auction_status S "
										   + "WHERE A.id = '"+ i_AuctionId +"' + U.id = A.Created_By and "
										   + "B.Business_Type = A.Business_Type and "
										   + "A.Event_Type = ET.id and "
										   + "A.Area = AR.id and "
										   + "A.Details_To_Display = D.id and "
										   + "A.Auction_Status = S.id");
			while (rs.next())
			{				
				auctionData.setId(rs.getInt("A.id"));
				auctionData.setMinAge(rs.getInt("A.Minimum_Age"));
				auctionData.setExceptionsDescription(rs.getString("A.Exceptions_Description"));
				auctionData.setGuestesQuantiny(rs.getInt("A.Guestes_Quantiny"));
				auctionData.setEventType(new IdWithName(rs.getInt("ET.id"), rs.getString("ET.Name")));
				auctionData.setEventDate(rs.getDate("A.Event_Date"));
				auctionData.setDateFlexible(rs.getBoolean("A.Is_Date_Flexible"));
				auctionData.setArea(new IdWithName(rs.getInt("AR.id"), rs.getString("AR.Name")));
				auctionData.setCertainBusiness(new IdWithName(rs.getInt("B.id"), rs.getString("B.Name")));
				auctionData.setDescription(rs.getString("A.Description"));
				auctionData.setDetailsToDisplay(new IdWithName(rs.getInt("D.id"), rs.getString("D.Name")));
				auctionData.setSmoking(rs.getBoolean("A.Smoking"));
				auctionData.setAuctionStatus(new IdWithName(rs.getInt("S.id"), rs.getString("S.Name")));
				auctionData.setCreatedBy(new IdWithName(rs.getInt("A.Created_By"), rs.getString("U.First_Name")));
				
			}		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			disconnectFromDBServer();
		}
		 return auctionData;
	}

	public static boolean addNewBusiness(BusinessData businessData) {
		boolean isSucceed = true;

		connectToDBServer();
		
		String sql= "INSERT INTO businesses(Name, Street, Structure_Number, Business_Phone_Number, Description, Business_Type, City, Area) "
				+ "VALUES ('"+ businessData.getM_Name()+ "','" + businessData.getM_StreetId().getId() +"','"+ businessData.getM_HouseNumber() +"','" + businessData.getM_PhoneNumber()+"','"+ businessData.getM_Description() +"','"+ businessData.getM_BusinessTypeId().getId() +"','"+ businessData.getM_CityId().getId() +"','"+ businessData.getM_AreaId().getId()+"')";
		
		String sql1= "INSERT INTO streets(City_id, Name) "
				+ "VALUES ('"+ businessData.getM_CityId().getId()+ "','" + businessData.getM_StreetId().getName() +"')";
		
		try {
			stmt.executeUpdate(sql);
			stmt.executeUpdate(sql1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			isSucceed = false;
			e.printStackTrace();
		}
		finally
		{
			disconnectFromDBServer();
		}		
		
		return isSucceed;
	}

	public static boolean addNewLine(LineData lineData) throws ParseException {
		boolean isSucceed = true;
		DateFormat df = new SimpleDateFormat("dd-MMM-yy HH:mm:ss a");
		String date1= df.format(lineData.getStartDate());
		Date date2= df.parse(date1);
		java.sql.Date sqlStartDate = new java.sql.Date(date2.getTime());
		
		String date3= df.format(lineData.getEndDate());
		Date date4= df.parse(date3);
		java.sql.Date sqlEndDate = new java.sql.Date(date4.getTime());
		
		connectToDBServer();
		
		String sql= "INSERT INTO businesses(Business_id, PR_id, Name, Day_In_Week, Line_Start_Date, Line_End_Date, Min_Age, Description, Entrance_Fee, DJ, Opening_Hour) "
				+ "VALUES ('"+ lineData.getBusiness().getId()+ "','" + lineData.getPr().getId() +"','"+ lineData.getM_LineName() +"','" + lineData.getM_DayInWeek()+"','"+ sqlStartDate +"','"+ sqlEndDate +"','"+ lineData.getMinAge() +"','"+ lineData.getDescription()+"','"+ lineData.getEntranceFee() +"','"+ lineData.getOpeningHour() +"')";
		try {
			stmt.executeUpdate(sql);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			isSucceed = false;
			e.printStackTrace();
		}
		finally
		{
			disconnectFromDBServer();
		}		
		
		return isSucceed;
	}

	public static ArrayList<UserMessagesData> getJoinLineRequestData(String email) {
		email = "orelsharabi8@gmail.com";
		ArrayList<UserMessagesData> joinLineRequestList = new ArrayList<>();
		connectToDBServer();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * "
										   + "FROM messages M, users U "
										   + "WHERE M.To_User_id = U.id and "
										   + "M.Line_id is not null and  "
										   + "M.Auction_id is null and "
										   + "U.Email ='" + email + "'");
			while (rs.next())
			{				
				UserMessagesData messagesData = new UserMessagesData();
				messagesData.setId(rs.getInt("M.id"));
				messagesData.setFromUserId(rs.getInt("M.From_User_id"));
				messagesData.setToUserId(rs.getInt("M.To_User_id"));
				messagesData.setLineId(rs.getInt("M.Line_id"));
				messagesData.setCreatedOn(rs.getDate("M.Created_On"));
				
				joinLineRequestList.add(messagesData);
			}	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			disconnectFromDBServer();
		}
		
		
		return joinLineRequestList;
	}

	public static String getFirstName(String emailParam) {
		String firstName =""; 
		connectToDBServer();
		try {
			ResultSet rs = stmt.executeQuery("SELECT first_name "
										   + "FROM users U "
										   + "WHERE "  
										   + "U.Email ='" + emailParam + "'");
			while (rs.next())
			{				

				firstName = rs.getString("first_name");
			}	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			disconnectFromDBServer();
		}
		
		
		return firstName;
	}	
}
