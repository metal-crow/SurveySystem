package response;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import question.Question;
import question.QuestionDAO;
import question.Question.Response_Type;
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
				-1);
		Question question = QuestionDAO.create_question(survey, "test question", Response_Type.S_String);
		create_response(123, survey, question, "this is an answer");
		ArrayList<Response> responses = get_responses(survey.getId());
		
		assert responses.size()==1;
		assert responses.get(0).getRespondant()==123;
		assert responses.get(0).getSurvey().getId()==survey.getId();
		assert responses.get(0).getResponse_to().getQuestion_id()==question.getQuestion_id();
		assert responses.get(0).getAnswer().equals("this is an answer");
		assert has_responded(survey.getId(), 123)==true;
		assert has_responded(survey.getId(), 999)==false;
		
		assert SurveyDAO.delete_survey(survey.getId());
	}

	/**
	 * Create response
	 * @param respondant hash of user id, or user id, or null if informal
	 * @param survey
	 * @param response_to the question
	 * @param answer string representing the serialized answer
	 * @return response
	 */
	public static Response create_response(Integer respondant, Survey survey, Question response_to, String answer){
		Session session = factory.openSession();
		Transaction tx = null;
		Response response = null;
		try{
			tx = session.beginTransaction();
			response = new Response(respondant, survey, response_to, answer);
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
	 * Delete all responses to a given survey
	 * @param survey_id
	 */
	public static void delete_responses(int survey_id){
		Session session = factory.openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			
			@SuppressWarnings("rawtypes")
			Query response_query = session.createQuery("delete RESPONSES where survey.id = :id");
			response_query.setParameter("id", survey_id);
			response_query.executeUpdate();
			
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close(); 
		}
	}
	
	/**
	 * Get list of responses by survey. Does not deserialize by the questions
	 * @param survey
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList<Response> get_responses(int survey_id){
		Session session = factory.openSession();
		Transaction tx = null;
		ArrayList<Response> responses=null;
		try{
			tx = session.beginTransaction();
			
			Query query = session.createQuery("from RESPONSES where survey_id = :id ");
	        query.setParameter("id", survey_id);
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

	/**
	 * Get if user has already responded to this survey
	 */
	@SuppressWarnings("rawtypes")
	public static boolean has_responded(int survey_id, int user_id) {
		Session session = factory.openSession();
		Transaction tx = null;
		boolean has_responded=true;
		try{
			tx = session.beginTransaction();
			
			Query query = session.createQuery("select count(1) from RESPONSES where survey_id = :id and respondant = :user_id");
	        query.setParameter("id", survey_id);
	        query.setParameter("user_id", user_id);
	        has_responded = query.uniqueResultOptional().equals(1);
			
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close(); 
		}
		return has_responded;
	}	
}
