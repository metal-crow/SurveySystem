package response;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import question.Question;
import survey.Survey;
import survey.Survey.User_Response_Type;
import survey.SurveyDAO;


public class ResponseDAO {

	private static SessionFactory factory;
	
	public ResponseDAO(SessionFactory factory) {
		ResponseDAO.factory=factory;
	}
	
	public static void testDAO(){
		Survey survey = SurveyDAO.create_survey(
				"testResponse", 
				User_Response_Type.Informal, 
				new Date(System.currentTimeMillis()), 
				new Date(System.currentTimeMillis()), 
				null);
		create_response(123, survey, null, "this is an answer");
		get_responses(survey).get(0);
	}

	/**
	 * Create response
	 * @param respondant hash of user id, or user id
	 * @param survey
	 * @param response_to the question
	 * @param answer string representing the serialized answer
	 * @return response
	 */
	public static Response create_response(int respondant, Survey survey, Question response_to, String answer){
		Session session = factory.openSession();
		Transaction tx = null;
		Response response = null;
		try{
			tx = session.beginTransaction();
			response = new Response(respondant, survey.getId(), survey.getRespondant_id_count(), response_to, answer);
			session.save(response);
			SurveyDAO.got_informal_response(survey);
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close(); 
		}
		return response;
	}
	
	/**
	 * Get list of responses by survey. Does not deserialize by the questions
	 * @param survey
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList<Response> get_responses(Survey survey){
		Session session = factory.openSession();
		Transaction tx = null;
		ArrayList<Response> responses=null;
		try{
			tx = session.beginTransaction();
			
			Query query = session.createQuery("from RESPONSES where survey_id = :id ");
	        query.setParameter("id", survey.getId());
	        responses = new ArrayList<Response>(query.getResultList());
			
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close(); 
		}
		return responses;
	}	
}
