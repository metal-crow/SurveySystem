package survey;

import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import survey.Survey.User_Response_Type;
import user.User;

public class SurveyDAO {
	
	private static SessionFactory factory;
	
	public SurveyDAO(SessionFactory factory) {
		SurveyDAO.factory=factory;
	}
	
	public static void testDAO(){
		int id = create_survey(
				"test_survey", 
				User_Response_Type.User, 
				new Date(System.currentTimeMillis()), 
				new Date(System.currentTimeMillis()), 
				null).getId();
		System.out.println(id);
		assert id>-1;
		
		Survey survey = get_survey(id);
		assert survey.getSurvey_name().equals("test_survey");
		assert survey.getUser_response_type()==User_Response_Type.User;
		
		int old_count=survey.getRespondant_id_count();
		assert got_informal_response(survey);
		assert old_count==survey.getRespondant_id_count();
		
		assert delete_survey(id);
	}

	/**
	 * Api function for creating a survey
	 * Returns the created survey, or null for database error
	 */
	public static Survey create_survey(String name, User_Response_Type user_response_type, Date closing, Date deleting, User managing_user){
		Session session = factory.openSession();
		Transaction tx = null;
		Survey survey = null;
		try{
			tx = session.beginTransaction();
			survey = new Survey(name, user_response_type, closing, deleting, managing_user);
			survey.setId((Integer) session.save(survey)); 
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
			survey = null;
		}finally {
			session.close(); 
		}
		return survey;
	}
	
	/**
	 * Delete the given survey, and all corrisponding questions and responses
	 * @param id the survey id
	 * @return
	 */
	public static boolean delete_survey(int id){
		Session session = factory.openSession();
		Transaction tx = null;
		boolean result = false;
		try{
			tx = session.beginTransaction();
			//delete survey
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery("delete SURVEYS where id = :id");
			query.setParameter("id", id);
			result = (query.executeUpdate()==1);
			//delete responses
			if(result){
				@SuppressWarnings("rawtypes")
				Query response_query = session.createQuery("delete RESPONSES where survey_id = :id");
				response_query.setParameter("id", id);
				response_query.executeUpdate();
			}
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close(); 
		}
		return result;
	}
	
	/**
	 * Increment the count for the number of informal users who've responded
	 * No effect if this is not a formal survey
	 * @param survey
	 * @return true on success, and the passed survey is altered
	 */
	public static boolean got_informal_response(Survey survey){
		Session session = factory.openSession();
		Transaction tx = null;
		boolean result = true;
		
		if(survey.getUser_response_type() == Survey.User_Response_Type.Informal){
			try{
				tx = session.beginTransaction();
				
				//increment count
				@SuppressWarnings("rawtypes")
				Query update_query = session.createQuery("update SURVEYS SET respondant_id_count = :count + 1 WHERE id = :id");
				update_query.setParameter("id", survey.getId());
				update_query.setParameter("count", survey.getRespondant_id_count());
				//save update to object (do even if db failure, to allow a potential retry later)
				survey.setRespondant_id_count(survey.getRespondant_id_count()+1);
				result = (update_query.executeUpdate()==1);
				
				tx.commit();
			}catch (HibernateException e) {
				if (tx!=null) tx.rollback();
				e.printStackTrace(); 
			}finally {
				session.close(); 
			}
		}
		
		return result;
	}

	/**
	 * Get survey by id
	 * @param id
	 * @return
	 */
	public static Survey get_survey(int id){
		Session session = factory.openSession();
		Transaction tx = null;
		Survey survey=null;
		try{
			tx = session.beginTransaction();
			
			survey = (Survey) session.get(Survey.class, id);
			
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close(); 
		}
		return survey;
	}
}
