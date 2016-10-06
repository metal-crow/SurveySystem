package response;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import question.Question;
import survey.Survey;


public class ResponseDAO {

	private static SessionFactory factory;
	
	public ResponseDAO(SessionFactory factory) {
		ResponseDAO.factory=factory;
	}
	
	public void testDAO(){
		//int id = create_response(respondant, survey, respondant_id, response_to, answer);
	}

	/**
	 * Create response
	 * @param respondant
	 * @param survey
	 * @param respondant_id
	 * @param response_to
	 * @param answer
	 * @return id
	 */
	public int create_response(int respondant, Survey survey, int respondant_id, Question response_to, String answer){
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
}
