package ClubberServlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Utlis.Constants;
import Utlis.SessionUtils;

import com.google.gson.Gson;

/**
 * Servlet implementation class AuctionOfferItemClicked
 */
@WebServlet("/AuctionOfferItemClicked")
public class AuctionOfferItemClicked extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuctionOfferItemClicked() {
        super();
        // TODO Auto-generated constructor stub
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
		
		String requestType= request.getParameter(Constants.CLICKED_ITEM_TYPE);
		String ID= request.getParameter(Constants.ITEM_ID);
		Integer itemID= Integer.parseInt(ID);
		
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		
        try 
        {
            if (requestType.equals(Constants.AUCTION_ITEM_CLICKED))
            {
            	SessionUtils.setCurrentAuctionToDisplay(request.getSession(), itemID);
            }
            else if (requestType.equals(Constants.OFFER_ITEM_CLICKED))
            {
            	SessionUtils.setCurrentOfferToDisplay(request.getSession(), itemID);
            }
            
            out.print(gson.toJson(true));
        }
        catch(Exception e)
        {
        	out.print(gson.toJson(false));
        }
        finally 
        {
            out.close();
        }
	}

}
