package ClubberServlets;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Utlis.Constants;
import ClubberLogic.Client;
import ClubberLogic.DAL;

/**
 * Servlet implementation class UpdateClientDetails
 */
@WebServlet("/UpdateClientDetails")
public class UpdateClientDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateClientDetails() {
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
		// TODO Auto-generated method stub
		response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        
        String message = "";
        Client client = new Client();
        
        client.setFirstName(request.getParameter(Constants.FIRST_NAME));
        client.setLastName(request.getParameter(Constants.LAST_NAME));
        client.setGender(request.getParameter(Constants.GENDER));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        Date birthDate = new Date();
        try {
			birthDate = sdf.parse(request.getParameter(Constants.BIRTHDATE));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        client.setBirthDate(birthDate);
        
        client.setPhoneNumber(request.getParameter(Constants.PHONE_NUMBER));
        client.setEmail(request.getParameter(Constants.EMAIL));
        client.setPassword(request.getParameter(Constants.PASSWORD));
                
        boolean isSucceed = true;
        
		try {
			isSucceed = DAL.updateUserDetails(client);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			isSucceed = false;
		}
        
        if(isSucceed == true)
        {
        	message = "העדכון בוצע";
        }
        else
        {
        	message = "העדכון נכשל";
        }
        
        request.setAttribute(Constants.MESSAGE_TEXT, message);
        getServletContext().getRequestDispatcher("/ClientProfile.jsp").forward(request, response);
	}

}
