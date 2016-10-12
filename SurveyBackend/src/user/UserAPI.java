package user;

import static spark.Spark.post;

import java.net.HttpURLConnection;

public class UserAPI {
	
    public UserAPI(){
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
				
				int id = UserDAO.create_user(email, password_hash, first_name, last_name);
				
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
				boolean valid = UserDAO.verify_user(id, password_hash);
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
