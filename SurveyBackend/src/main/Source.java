package main;
import java.util.Random;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import question.Question;
import question.QuestionDAO;
import response.Response;
import response.ResponseDAO;
import survey.Survey;
import survey.SurveyAPI;
import survey.SurveyDAO;
import user.User;
import user.UserAPI;
import user.UserDAO;

import static spark.Spark.*;

public class Source {
	
	private static SessionFactory factory;
	
	public static void main(String[] args) {
		//Initialize the db connection
		try{
			
			factory = new Configuration()
						//Add all classes here
						.addAnnotatedClass(User.class)
						.addAnnotatedClass(Survey.class)
						.addAnnotatedClass(Response.class)
						.addAnnotatedClass(Question.class)
						.configure()
						.buildSessionFactory();
		}catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex); 
		}

		//initalize DAO's access
		new UserDAO(factory);
		new SurveyDAO(factory);
		new ResponseDAO(factory);
		new QuestionDAO(factory);
		
		//run tests here
		UserDAO.testDAO();
		SurveyDAO.testDAO();
		ResponseDAO.testDAO();
		QuestionDAO.testDAO();
		
        get("/hello", (req, res) -> "Hello World");
        
		//start the APIs here, Spark will auto traverse them and start the endpoints
		new UserAPI();
		new SurveyAPI();
		
		factory.close();
	}
	
	/**
	 * Returns random alphanumberic string
	 * @param length
	 * @return
	 */
	public static String random_string(int length){
		StringBuilder str = new StringBuilder();
		Random r = new Random();
		for(int i=0;i<length;i++){
			int result = r.nextInt(26*2+10);
			if(result<26){
				str.append((char)(result+'a'));
			}else if(result>=26 && result<52){
				str.append((char)((result-26)+'A'));
			}else{
				str.append((char)((result-52)+'0'));
			}
		}
		
		return str.toString();
	}

}
