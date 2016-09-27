import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import question.Question;
import response.Response;
import survey.Survey;
import user.User;
import user.UserAPI;

public class Source {
	
	private static SessionFactory factory;
	
	public static void main(String[] args) {
		//Initialize the db connection and DAOs
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

		UserAPI user_api = new UserAPI(factory);
		
		factory.close();
	}

}
