package ClubberServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ClubberLogic.BusinessData;
import ClubberLogic.DAL;
import Utlis.Constants;
import Utlis.IdWithName;

/**
 * Servlet implementation class AddNewBusiness
 */
@WebServlet("/AddNewBusiness")
public class AddNewBusiness extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddNewBusiness() {
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
        BusinessData businessData = new BusinessData();
        
        businessData.setM_Name(request.getParameter(Constants.BUSINESS_NAME));

        Integer typeId = new Integer(Integer.parseInt(request.getParameter(Constants.BUSINESS_TYPE_ID)));
        IdWithName type = new IdWithName(typeId, request.getParameter(Constants.BUSINESS_TYPE_NAME));
        businessData.setM_BusinessTypeId(type);
        
        Integer areaId = new Integer(Integer.parseInt(request.getParameter(Constants.BUSINESS_AREA_ID)));
        IdWithName area = new IdWithName(areaId, request.getParameter(Constants.BUSINESS_AREA_NAME));
        businessData.setM_AreaId(area);
        
        Integer cityId = new Integer(Integer.parseInt(request.getParameter(Constants.BUSINESS_CITY_ID)));
        IdWithName city = new IdWithName(cityId, request.getParameter(Constants.BUSINESS_CITY_NAME));
        businessData.setM_CityId(city);

        Integer streetId = 0;
        IdWithName street = new IdWithName(streetId, request.getParameter(Constants.BUSINESS_STREET_NAME));
        businessData.setM_StreetId(street);
        
        businessData.setM_HouseNumber(Integer.parseInt(request.getParameter(Constants.BUSINESS_HOME_NUMBER)));
        businessData.setM_PhoneNumber(request.getParameter(Constants.BUSINESS_PHONE_NUMBER));
        businessData.setM_Description(request.getParameter(Constants.BUSINESS_DESCRIPTION));
        //businessData.setM_Photo(request.getParameter(Constants.BUSINESS_PHOTO));
                        
        boolean isSucceed = true;
        
		isSucceed = DAL.addNewBusiness(businessData);
        
        if(isSucceed == true)
        {
        	message = "המקום התווסף בהצלחה";
        }
        else
        {
        	message = "הוספת המקום למערכת נכשלה";
        }
        
        request.setAttribute(Constants.MESSAGE_TEXT, message);
        getServletContext().getRequestDispatcher("/AddNewBusiness.jsp").forward(request, response);
		
		
	}

}
