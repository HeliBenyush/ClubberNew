package ClubberServlets;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		response.setContentType("text/html;charset=UTF-8");
		
        String emailParam = request.getParameter(Constants.EMAIL);
        String passwordParam = request.getParameter(Constants.PASSWORD);
        UserType userType = UserType.Client;
        
        boolean isSucceed = true; 
        String message = "";
        
        if(DAL.isEmailExists(emailParam) == false)
        {
        	isSucceed = false;
        	message = "מייל לא קיים במערכת";
        }

        // in case of time stamp exist (user lock) check if passed 3 hours 
        long timeStamp = DAL.getLoginAttemptTimeStamp(emailParam);
        Date currentDate = new Date();
        long date = currentDate.getTime();
        
        if(timeStamp != 0 && timeStamp > currentDate.getTime())
        {
        	isSucceed = false;
        	message = "���� ���� ������ ������ ������ ��� �����, ������ ����.";        		
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
            	message = "הסיסמא שגויה, בלאט!";
            	
            	DAL.increaseLoginAttemptsDB(emailParam);

            	if (DAL.getUserLoginAttempts(emailParam) > 5)
            	{
					DAL.updateLoginAttemptTimeStamp(emailParam);
            	}
        	}
        	else
        	{
        		userType = DAL.getUserType(emailParam);
        	}
        }
        
        if(isSucceed == true)
        {
        	request.getSession(true).setAttribute(Constants.EMAIL, emailParam);
        	request.getSession(true).setAttribute(Constants.WHO_AM_I, userType);
        	getServletContext().getRequestDispatcher("/WelcomePage.jsp").forward(request, response);
        }
        else
        {
        	request.setAttribute(Constants.LOGIN_FAILED, message);
        	getServletContext().getRequestDispatcher("/Login.jsp").forward(request, response);        	
        }
	}

}
