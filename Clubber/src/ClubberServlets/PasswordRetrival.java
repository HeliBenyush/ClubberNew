package ClubberServlets;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ClubberLogic.DAL;
import Utlis.Constants;
import Utlis.GoogleMail;
import Utlis.PasswordGenarator;

/**
 * Servlet implementation class SendPasswordRetrivalMail
 */
@WebServlet("/PasswordRetrival")
public class PasswordRetrival extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PasswordRetrival() {
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
        
		String recipientEmail = request.getParameter(Constants.EMAIL);
		String statusMessage = "";
		
		boolean isEmailExists =  DAL.isEmailExists(recipientEmail);
		
		if(isEmailExists == true)
		{
			String newPassword = PasswordGenarator.create(); 
			String title = "Clubber - Password Retrival";
			String message = "Hello your new password is : " + newPassword;
					
			try {
				GoogleMail.Send(Constants.CLUBBER_USER_NAME, Constants.CLUBBER_USER_PASSWORD, recipientEmail, title, message);
				
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				statusMessage = "����� ����� ����� �����";
				request.setAttribute(Constants.STATUS_MESSAGE, statusMessage);
				getServletContext().getRequestDispatcher("/PasswordRetrival.jsp").forward(request, response);
			}
			
			DAL.updateUserPassword(recipientEmail, newPassword);
			getServletContext().getRequestDispatcher("/Login.jsp").forward(request, response);
		}
		else
		{
			statusMessage = "����� ���� �������� ���� ����� ������";
			request.setAttribute(Constants.STATUS_MESSAGE, statusMessage);
			getServletContext().getRequestDispatcher("/PasswordRetrival.jsp").forward(request, response);			
		}
	}
}
