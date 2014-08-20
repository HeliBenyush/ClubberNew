package ClubberServlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ClubberLogic.BusinessData;
import ClubberLogic.DAL;
import ClubberLogic.LineData;
import Utlis.Constants;
import Utlis.IdWithName;

/**
 * Servlet implementation class AddNewLine
 */
@WebServlet("/AddNewLine")
public class AddNewLine extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddNewLine() {
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
        LineData lineData = new LineData();
        boolean isSucceed = true;

        lineData.setM_LineName(request.getParameter(Constants.LINE_NAME));
        Integer businnesId = Integer.parseInt(request.getParameter(Constants.LINE_BUSINEES_ID));
        String 	businessName = 	request.getParameter(Constants.LINE_BUSINEES_NAME);
        lineData.setBusiness(new IdWithName(businnesId, businessName));
        lineData.setM_DayInWeek(Integer.parseInt(request.getParameter(Constants.LINE_DAY_IN_WEEK)));

		DateFormat df = new SimpleDateFormat("dd/mm/yyyy");
		Date startDate = new Date();
		Date endDate = new Date();

		try {
			String startDateStr = request.getParameter(Constants.LINE_START_DATE);
			startDate = df.parse(startDateStr);
			String endDateStr = request.getParameter(Constants.LINE_START_DATE);
			endDate = df.parse(endDateStr);			
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

        lineData.setStartDate(startDate);
        lineData.setEndDate(endDate);
        lineData.setMinAge(Integer.parseInt(request.getParameter(Constants.LINE_MIN_AGE)));
        lineData.setDescription(request.getParameter(Constants.LINE_DESCRIPTION));
        lineData.setEntranceFee(request.getParameter(Constants.LINE_ETRANCEFEE));
        lineData.setDj(request.getParameter(Constants.LINE_DJ));
        
        /*
         * very important needs to save the pr details here take it from session
         */
        //set pr id and name
        //lineData.setPr(pr);
        
        try {
			isSucceed = DAL.addNewLine(lineData);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        if(isSucceed == true)
        {
        	message = "הליין התווסף בהצלחה";
        }
        else
        {
        	message = "הוספת הליין למערכת נכשלה";
        }
        
        request.setAttribute(Constants.MESSAGE_TEXT, message);
        getServletContext().getRequestDispatcher("/AddNewLine.jsp").forward(request, response);
		
		
	}
}
