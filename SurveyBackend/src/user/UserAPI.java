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
		 * @return success/failure
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
				}
				
				response.status(HttpURLConnection.HTTP_CREATED);
				return "";
			}catch(Exception e){
				response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
				return e.getMessage();
			}
		});
		
		/**
		 * Verifies user login. 
		 * Requires json arguments
		 * 	@param email - user email
		 *  @param password - password hash
		 * @return HTTP_ACCEPTED and User_Id on login, HTTP_NOT_ACCEPTABLE on failure.
		 */
		post("/login", "application/json", (request, response) -> {
			try{
				JsonObject verification_obj = new JsonParser().parse(request.body()).getAsJsonObject();

				String email = verification_obj.get("email").getAsString();
				String password_hash = verification_obj.get("password").getAsString();
				boolean valid = UserDAO.verify_user(email, password_hash);
				if(valid){
					response.status(HttpURLConnection.HTTP_ACCEPTED);
					return UserDAO.get_user(email).getid();
				}else{
					response.status(HttpURLConnection.HTTP_NOT_ACCEPTABLE);
					return -1;
				}
			}catch(Exception e){
				response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
				return e.getMessage();
			}
	    });
	}
}
