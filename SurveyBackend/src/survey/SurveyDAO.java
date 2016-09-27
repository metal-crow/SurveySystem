package survey;

import java.sql.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import user.User;

public class SurveyDAO {
	
	private static SessionFactory factory;
	
	public SurveyDAO(SessionFactory factory) {
		SurveyDAO.factory=factory;
	}

	/**
	 * Api function for creating a survey
	 * Returns the id of the created survey 
	 */
	public int create_survey(String name, UserResponse_Type userresponse_type, Date closing, Date deleting, User managing_user){
		Session session = factory.openSession();
		Transaction tx = null;
		Integer survey_id = null;
		try{
			tx = session.beginTransaction();
			Survey survey = new Survey(name, userresponse_type, closing, deleting, managing_user);
			survey_id = (Integer) session.save(survey); 
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close(); 
		}
		return survey_id;
	}
}
