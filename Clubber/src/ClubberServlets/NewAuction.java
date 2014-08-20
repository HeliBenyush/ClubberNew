package ClubberServlets;

import Utlis.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import ClubberLogic.AuctionData;
import ClubberLogic.DAL;

/**
 * Servlet implementation class NewAuction
 */
@WebServlet("/NewAuction")
public class NewAuction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @throws SQLException 
     * @see HttpServlet#HttpServlet()
     */
    public NewAuction() throws SQLException {
        super();
        // TODO Auto-generated constructor stub
        //DAL.DoLogin("sad", "password");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
		PrintWriter out = response.getWriter();
		AuctionData auction= new AuctionData();
		try
		{
			auction.setEventType(new IdWithName(Integer.parseInt(request.getParameter(Constants.EVENT_TYPE)),null));
			addAllMusicStyles(request, auction);
			addAllSittsTypes(request, auction);
			addAllBusinessTypes(request, auction);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			Date date= df.parse(request.getParameter(Constants.DATEPICKER));
			auction.setEventDate(date);
			auction.setDateFlexible(Boolean.parseBoolean(request.getParameter(Constants.FLEXIBLE_DATE)));
			auction.setGuestesQuantiny(Integer.parseInt(request.getParameter(Constants.GUESTS_QUANTITY)));
			auction.setExceptionsDescription(request.getParameter(Constants.EXCEPTION_DESCRIPTION));
			auction.setMinAge(Integer.parseInt(request.getParameter(Constants.MIN_AGE)));
			auction.setArea(new IdWithName(Integer.parseInt(request.getParameter(Constants.AREA)),null));
			
			if ((request.getParameter(Constants.CERTAIN_BUSINESS) != null) && (request.getParameter(Constants.CERTAIN_BUSINESS) !=""))
			{
				auction.setCertainBusiness(new IdWithName(Integer.parseInt(request.getParameter(Constants.CERTAIN_BUSINESS)),null));
			}
			
			auction.setSmoking(Boolean.parseBoolean(request.getParameter(Constants.SMOKING)));
			auction.setDescription(request.getParameter(Constants.GENERAL_DESCRIPTION));
			
			DAL.addNewAuction(auction);
            out.flush();
		}
        catch (Exception e) {
    		e.printStackTrace();
    	}
        finally 
        {
            out.close();
            response.sendRedirect("www.google.com");
        }
	}
	
	public void addAllMusicStyles(HttpServletRequest request, AuctionData auction)
	{
		String musicStyles[]= request.getParameter(Constants.MUSIC_STYLE_LIST).split("&");
		for (String item : musicStyles) {
			auction.getMusicStyle().add(new IdWithName(Integer.parseInt(item), null));
		}
	}
	
	public void addAllSittsTypes(HttpServletRequest request, AuctionData auction)
	{
		String sittsTypes[]= request.getParameter(Constants.SITS_TYPE_LIST).split("&");
		for (String item : sittsTypes) {
			auction.getSittsType().add(new IdWithName(Integer.parseInt(item), null));
		}
	}
	
	public void addAllBusinessTypes(HttpServletRequest request, AuctionData auction)
	{
		String businessTypes[]= request.getParameter(Constants.BUSINESS_TYPE_LIST).split("&");
		for (String item : businessTypes) {
			auction.getBusinessType().add(new IdWithName(Integer.parseInt(item), null));
		}
	}

}
