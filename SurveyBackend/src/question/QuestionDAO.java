package question;

import java.util.ArrayList;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import question.Question.Response_Type;
import survey.Survey;
import survey.Survey.User_Response_Type;
import survey.SurveyDAO;

public class QuestionDAO {

	private static SessionFactory factory;
	
	public QuestionDAO(SessionFactory factory) {
		QuestionDAO.factory=factory;
	}

	public static void testDAO(){
		Survey survey = SurveyDAO.create_survey(
				"testResponse", 
				User_Response_Type.Informal, 
				new Date(System.currentTimeMillis()), 
				new Date(System.currentTimeMillis()), 
				-1);
		create_question(survey, "test question", Response_Type.S_String);
	}

	/**
	 * Create questions for given survey
	 * @param survey
	 * @param question_text
	 * @param response_type
	 * @return
	 */
	public static Question create_question(Survey survey, String question_text, Response_Type response_type){
		Session session = factory.openSession();
		Transaction tx = null;
		Question question = null;
		try{
			tx = session.beginTransaction();
			question = new Question(survey, question_text, response_type);
			session.save(question);
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close(); 
		}
		return question;
	}
	
	/**
	 * Delete all questions for a given survey
	 * @param survey_id
	 */
	public static void delete_questions(int survey_id) {
		Session session = factory.openSession();
		Transaction tx = null;
		try{
			tx = session.beginTransaction();
			
			@SuppressWarnings("rawtypes")
			Query response_query = session.createQuery("delete QUESTIONS where survey.id = :id");
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
	 * Get all questions for a given survey
	 * @param survey_id
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList<Question> get_questions(int survey_id){
		Session session = factory.openSession();
		Transaction tx = null;
		ArrayList<Question> questions=null;
		try{
			tx = session.beginTransaction();
			
			Query query = session.createQuery("from QUESTIONS where survey.id = :id ");
	        query.setParameter("id", survey_id);
	        questions = new ArrayList<Question>(query.getResultList());
			
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close(); 
		}
		return questions;
	}	
	
}
	