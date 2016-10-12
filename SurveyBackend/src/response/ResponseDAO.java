package response;

import java.util.ArrayList;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import question.Question;
import survey.Survey;


public class ResponseDAO {

	private static SessionFactory factory;
	
	public ResponseDAO(SessionFactory factory) {
		ResponseDAO.factory=factory;
	}
	
	public static void testDAO(){
		//int id = create_response(respondant, survey, respondant_id, response_to, answer);
	}

	/**
	 * Create response
	 * @param respondant hash of user id, or user id
	 * @param survey
	 * @param respondant_id the count of responses up to this point. i.e 10 for this being the 10th response.
	 * @param response_to the question
	 * @param answer string representing the serialized answer
	 * @return id
	 */
	public static int create_response(int respondant, Survey survey, int respondant_id, Question response_to, String answer){
		Session session = factory.openSession();
		Transaction tx = null;
		Integer response_id = null;
		try{
			tx = session.beginTransaction();
			Response response = new Response(respondant, survey, respondant_id, response_to, answer);
			response_id = (Integer) session.save(response); 
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close(); 
		}
		return response_id;
	}
	
	/**
	 * Delete a specific response
	 * @param id
	 * @return
	 */
	public static void delete_response(int id){
		Session session = factory.openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery("delete RESPONSES where id = :id");
			query.setParameter("id", id);
			query.executeUpdate();
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
	public static ArrayList<Response> get_responses(Survey survey){
		Session session = factory.openSession();
		Transaction tx = null;
		ArrayList<Response> responses=null;
		try{
			tx = session.beginTransaction();
			
			Query query = session.createQuery("from RESPONSES where survey = :id ");
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
