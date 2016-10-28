package user;

import static spark.Spark.post;

import java.net.HttpURLConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UserAPI {
	
    public UserAPI(){
      setupEndpoints();
    }
	
	private void setupEndpoints() {
		/**
		 * Creates a user
		 * Requires json arguments
		 * 	@param email
		 *  @param password - (the hash)
		 *  @param first_name
		 *  @param last_name
		 * @return id of created user
		 */
		post("/createUser", "application/json", (request, response) -> {
			try{
				JsonObject json_user_obj = new JsonParser().parse(request.body()).getAsJsonObject();
				
				//TODO add ability to check email domain and require it match a given config value
				
				User user = UserDAO.create_user(
						json_user_obj.get("email").getAsString(), 
						json_user_obj.get("password").getAsString(), 
						json_user_obj.get("first_name").getAsString(),
						json_user_obj.get("last_name").getAsString());
				
				if(user==null){
					response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
					return -1;
				}
				
				response.status(HttpURLConnection.HTTP_CREATED);
				return user.getid();
			}catch(Exception e){
				response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
				return e.getMessage();
			}
		});
		
		/**
		 * Verifies user login. 
		 * Requires json arguments
		 * 	@param id - user id
		 *  @param password - password hash
		 * @return HTTP_ACCEPTED on login, HTTP_NOT_ACCEPTABLE on failure
		 */
		post("/login", "application/json", (request, response) -> {
			try{
				JsonObject verification_obj = new JsonParser().parse(request.body()).getAsJsonObject();

				Integer id = verification_obj.get("id").getAsInt();
				String password_hash = verification_obj.get("password").getAsString();
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
