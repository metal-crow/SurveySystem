package response;

import static spark.Spark.post;

import java.net.HttpURLConnection;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import question.Question;
import question.QuestionDAO;
import survey.Survey;
import survey.Survey.User_Response_Type;
import survey.SurveyDAO;
import user.UserDAO;

public class ResponseAPI {
	
    public ResponseAPI(){
      setupEndpoints();
    }
    
    //object to convert response from when received from client
    class Response_JSON_wrapper_recieve{
    	String answer;
    	int question_id;
    }
	
	private void setupEndpoints() {
		
		//TODO allow user to edit if they have already responded (and not informal survey)
		post("/survey/:survey_id/respond", "application/json", (request, response) -> {
			try{
				JsonObject json = new JsonParser().parse(request.body()).getAsJsonObject();

				Survey survey = SurveyDAO.get_survey(Integer.valueOf(request.params("survey_id")));
				Integer user_id = json.get("user_id").getAsInt();

				if(survey.getUser_response_type()==User_Response_Type.User || survey.getUser_response_type()==User_Response_Type.Anonymous){
					String password_hash = json.get("password").getAsString();
					//verify user logged in if survey requires it
					if(!UserDAO.verify_user(user_id, password_hash)){
						response.status(HttpURLConnection.HTTP_NOT_ACCEPTABLE);
						return -3;
					}
					//also verify user has not responded to survey yet
					if(ResponseDAO.has_responded(survey.getId(), user_id)){
						response.status(HttpURLConnection.HTTP_FORBIDDEN);
						return -2;
					}
				}
				
				//verify survey not closed
				
				
				int user_response_id=-1;
				//fix this in the real world, use bcrypt 
				switch(survey.getUser_response_type()){
					case Anonymous:
						MessageDigest digest = MessageDigest.getInstance("SHA-256");
						user_response_id = ByteBuffer.wrap(digest.digest(user_id.toString().getBytes())).getInt();
						break;
					case Informal:
						user_response_id=-1;
						break;
					case User:
						user_response_id=user_id;
						break;
					default:
						break;					
				}
				
				//enter responses for the survey
				Response_JSON_wrapper_recieve[] responses = new Gson().fromJson(json.get("responses"), Response_JSON_wrapper_recieve[].class);
				for(Response_JSON_wrapper_recieve response_info:responses){
					Question question = QuestionDAO.get_question(response_info.question_id);
					//TODO serialize/deserialize
					ResponseDAO.create_response(user_response_id, survey, question, response_info.answer);
				}
				
				response.status(HttpURLConnection.HTTP_CREATED);
				return 1;
			}catch(Exception e){
				response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
				return e.getMessage();
			}
		});

	}
}
