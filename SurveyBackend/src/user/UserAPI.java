package user;

import static spark.Spark.*;
import java.net.HttpURLConnection;
import org.hibernate.SessionFactory;

public class UserAPI {
	
    private UserDAO user_dao;

    public UserAPI(SessionFactory factory){
      this.user_dao = new UserDAO(factory);
      setupEndpoints();
    }
	
	private void setupEndpoints() {
		post("/createUser", "application/json", (request, response) -> {
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
	}
}
