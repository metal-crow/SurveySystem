import static spark.Spark.*;

import java.net.HttpURLConnection;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import user.User;
import user.UserDAO;

public class Source {
	
	private static SessionFactory factory;
	
	public static void main(String[] args) {
		//Initialize the db connection and DAOs
		try{
			
			factory = new Configuration()
						//Add all classes here
						.addAnnotatedClass(User.class)
						.configure()
						.buildSessionFactory();
		}catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex); 
		}
		
		UserDAO user_dao = new UserDAO(factory);
		
		post("/createUser", (request, response) -> {
			try{
				String email = request.queryParams("email");
				//TODO add ability to check email domain and require it match a given config value
				String password_hash = request.queryParams("password");
				String first_name = request.queryParams("first_name");
				String last_name = request.queryParams("last_name");
				
				int id = user_dao.create_user(email, password_hash, first_name, last_name);
				//user_dao.create_user("test@aol.com", "1d1d2m1098c1c1c1c2", "Bob","Lastname");
				
				response.status(HttpURLConnection.HTTP_CREATED);
				return id;
			}catch(Exception e){
				response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
				return e.getMessage();
			}
		});
		
		post("/login", (request, response) -> {
			try{
				Integer id = Integer.valueOf(request.queryParams("id"));
				String password_hash = request.queryParams("password");
				boolean valid = user_dao.verify_user(id, password_hash);
				if(valid){
					response.status(HttpURLConnection.HTTP_ACCEPTED);
					return "Login Successful";
				}else{
					response.status(HttpURLConnection.HTTP_NOT_ACCEPTABLE);
					return "Invalid Login";
				}
			}catch(Exception e){
				response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
				return e.getMessage();
			}
        });

		factory.close();
	}

}
