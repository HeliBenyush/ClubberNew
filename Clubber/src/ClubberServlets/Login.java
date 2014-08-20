package ClubberServlets;

import java.io.IOException;
import java.util.Date;

import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ClubberLogic.DAL;
import ClubberLogic.UserType;
import Utlis.Constants;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
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
		
		String firstName = request.getParameter(Constants.FIRST_NAME);
        String emailParam = request.getParameter(Constants.EMAIL);
        String passwordParam = request.getParameter(Constants.PASSWORD);
        UserType userType = UserType.Client;
        HttpSession session = request.getSession(true);
        
        boolean isSucceed = true; 
        String message = "";
        
        if(DAL.isEmailExists(emailParam) == false)
        {
        	isSucceed = false;
        	message = "המייל שהוזן אינו קיים במערכת.";
        }

        // in case of time stamp exist (user lock) check if passed 3 hours 
        long timeStamp = DAL.getLoginAttemptTimeStamp(emailParam);
        Date currentDate = new Date();
        long date = currentDate.getTime();
        
        if(timeStamp != 0 && timeStamp > currentDate.getTime())
        {
        	isSucceed = false;
        	message = "מאחר והיו ניסיות מרובים למערכת ללא הצלחה, המשתמש ננעל.";        		
        }
        else if(timeStamp != 0 && timeStamp <= date)
        {
        	DAL.unlockUser(emailParam);
        }
        
        if(isSucceed == true)
        {
        	if(DAL.isPasswordMatcheEmail(emailParam, passwordParam) == false)
        	{
            	isSucceed = false;
            	message = "הסיסמה אינה נכונה";
            	
            	DAL.increaseLoginAttemptsDB(emailParam);

            	if (DAL.getUserLoginAttempts(emailParam) > 5)
            	{
					DAL.updateLoginAttemptTimeStamp(emailParam);
            	}
        	}
        	else
        	{
        		userType = DAL.getUserType(emailParam);
        		firstName = DAL.getFirstName(emailParam);
        	}
        }
        
        if(isSucceed == true)
        {
        	session.setAttribute(Constants.EMAIL, emailParam);
        	session.setAttribute(Constants.FIRST_NAME, firstName);
        	session.setAttribute(Constants.WHO_AM_I, userType);
        	getServletContext().getRequestDispatcher("/ClientProfile.jsp").forward(request, response);
        }
        else
        {
        	request.setAttribute(Constants.LOGIN_FAILED, message);
        	getServletContext().getRequestDispatcher("/Login.jsp").forward(request, response);        	
        }
	}

}
