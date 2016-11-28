package survey;

import static spark.Spark.*;

import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import question.Question;
import question.Question.Response_Type;
import question.QuestionDAO;
import response.Response;
import response.ResponseDAO;
import survey.Survey.User_Response_Type;
import user.UserDAO;

public class SurveyAPI {

	public SurveyAPI(){
		setupEndpoints();
	}
	
	//object to convert question from when received from client
	class Question_JSON_wrapper_recieve{
		String text;
		int response_type;
	}
	//object to convert question from when received from client
	class Question_JSON_wrapper_recieve_for_edit{
		String question_text;
		int question_id;
	}
	//object to convert Question to when sent from server
	class Question_JSON_wrapper_send{
		int id;
		String text;
		int response_type;
		
		public Question_JSON_wrapper_send(int id, String text, int response_type) {
			this.id = id;
			this.text = text;
			this.response_type = response_type;
		}		
	}
	
	//object to convert Response to when sent to arraylist
	class Response_JSON_wrapper_send{
		int respondant;
		String answer;
	}
	//object to send to client
	class Question_for_veiw_JSON_wrapper_send{
		String text;
		ArrayList<Response_JSON_wrapper_send> responses;
	}
	

	private void setupEndpoints() {
		
		/**
		 * Create survey and all questions corresponding to survey
		 * Requires json arguments
		 *  @param user_id - user who created and now owns the survey
		 *  @param password - password hash of user
		 * 	@param survey_name
		 * 	@param response_type - int corresponding to User_Response_Type
		 * 	@param closing_date
		 * 	@param deleting_date
		 *  @param questions - array of input questions
		 * 	
		 * @return id of created survey
		 */
		post("/createSurvey", "application/json", (request, response) -> {
			try{
				JsonObject survey_obj = new JsonParser().parse(request.body()).getAsJsonObject();

				//verify user
				int user_id = survey_obj.get("user_id").getAsInt();
				String password = survey_obj.get("password").getAsString();
				if(!UserDAO.verify_user(user_id, password)){
					response.status(HttpURLConnection.HTTP_NOT_ACCEPTABLE);
					return -3;
				}					

				//create survey object
				String survey_name = survey_obj.get("survey_name").getAsString();
				User_Response_Type response_type = User_Response_Type.fromInt(survey_obj.get("user_response_type").getAsInt());
				DateFormat df = DateFormat.getDateInstance();
				Date closing = df.parse(survey_obj.get("closing_date").getAsString());
				Date deleting = df.parse(survey_obj.get("deleting_date").getAsString());
				//verification of dates
				if(closing.before(new Date(System.currentTimeMillis())) || deleting.before(new Date(System.currentTimeMillis())) || closing.after(deleting)){
					response.status(HttpURLConnection.HTTP_NOT_ACCEPTABLE);
					return -2;
				}

				Survey survey = SurveyDAO.create_survey(survey_name, response_type, closing, deleting, user_id);
				
				if(survey==null){
					response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
					return -1;
				}
				
				//create questions for survey
				Question_JSON_wrapper_recieve[] questions = new Gson().fromJson(survey_obj.get("questions"), Question_JSON_wrapper_recieve[].class);
				for(Question_JSON_wrapper_recieve question:questions){
					QuestionDAO.create_question(survey, question.text, Response_Type.fromInt(question.response_type));
				}
				
				response.status(HttpURLConnection.HTTP_CREATED);
				return survey.getId();
			}catch(Exception e){
				response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
				return e.getMessage();
			}
		});
		
		/**
		 * Get and return all info about the given survey
		 * Questions, name, etc
		 */
		post("/survey/:survey_id", "application/json", (request, response) -> {
			try{
				Survey survey = SurveyDAO.get_survey(Integer.valueOf(request.params("survey_id")));
				if(survey==null){
					response.status(HttpURLConnection.HTTP_NOT_FOUND);
					return -1;
				}
				
				JsonObject optional_args = new JsonParser().parse(request.body()).getAsJsonObject();
				
				JsonObject survey_json = new JsonObject();
				survey_json.addProperty("name", survey.getSurvey_name());
				survey_json.addProperty("closing_date", survey.getClosing().getTime());
				if(optional_args.has("user_id")){
					survey_json.addProperty("has_user_responded", ResponseDAO.has_responded(survey.getId(), optional_args.get("user_id").getAsInt()));
				}else{
					survey_json.addProperty("has_user_responded", false);
				}
				survey_json.addProperty("is_informal", survey.getUser_response_type()==User_Response_Type.Informal);
				
				ArrayList<Question> questions = QuestionDAO.get_questions(survey.getId());
				//convert question objects to JSON convertable objects
				ArrayList<Question_JSON_wrapper_send> questions_json = new ArrayList<Question_JSON_wrapper_send>(questions.size());
				for(Question q:questions){
					questions_json.add(new Question_JSON_wrapper_send(q.getQuestion_id(), q.getQuestion_text(), Response_Type.toInt(q.getResponse_type())));
				}
				//add json of questions to response
				survey_json.addProperty("questions",new Gson().toJson(questions_json));
				
				response.status(HttpURLConnection.HTTP_OK);
				return survey_json.toString();
			}catch(Exception e){
				response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
				return e.getMessage();
			}
		});
		
		//allow managing user to edit survey
		post("/survey/:survey_id/edit", "application/json", (request, response) -> {
			try{
				JsonObject survey_obj = new JsonParser().parse(request.body()).getAsJsonObject();

				//verify user
				int user_id = survey_obj.get("user_id").getAsInt();
				String password = survey_obj.get("password").getAsString();
				if(!UserDAO.verify_user(user_id, password)){
					response.status(HttpURLConnection.HTTP_NOT_ACCEPTABLE);
					return -3;
				}	
				//Get Survey
				Survey orig_survey = SurveyDAO.get_survey(Integer.valueOf(request.params("survey_id")));
				if(orig_survey==null){
					response.status(HttpURLConnection.HTTP_NOT_FOUND);
					return -1;
				}
				if(orig_survey.getManaging_user_id()!=user_id){
					response.status(HttpURLConnection.HTTP_NOT_FOUND);
					return -1;
				}

				//edit survey object
				orig_survey.setSurvey_name(survey_obj.get("survey_name").getAsString());
				orig_survey.setUser_response_type(User_Response_Type.fromInt(survey_obj.get("user_response_type").getAsInt()));
				DateFormat df = DateFormat.getDateInstance();
				orig_survey.setClosing(df.parse(survey_obj.get("closing_date").getAsString()));
				orig_survey.setDeleting(df.parse(survey_obj.get("deleting_date").getAsString()));

				//verification of dates
				if(orig_survey.getClosing().before(new Date(System.currentTimeMillis())) || orig_survey.getDeleting().before(new Date(System.currentTimeMillis())) || orig_survey.getClosing().after(orig_survey.getDeleting())){
					response.status(HttpURLConnection.HTTP_NOT_ACCEPTABLE);
					return -2;
				}

				SurveyDAO.update_survey(orig_survey);
				
				//edit questions for survey
				Question_JSON_wrapper_recieve_for_edit[] questions = new Gson().fromJson(survey_obj.get("questions"), Question_JSON_wrapper_recieve_for_edit[].class);
				for(Question_JSON_wrapper_recieve_for_edit question:questions){
					Question question_obj = QuestionDAO.get_question(question.question_id);
					question_obj.setQuestion_text(question.question_text);
					QuestionDAO.update_question(question_obj);
				}
				
				response.status(HttpURLConnection.HTTP_ACCEPTED);
				return 1;
			}catch(Exception e){
				response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
				return e.getMessage();
			}
		});
		
		//Get all responses for a survey
		post("/survey/:survey_id/view", "application/json", (request, response) -> {
			try{
				JsonObject survey_obj = new JsonParser().parse(request.body()).getAsJsonObject();

				//verify user
				int user_id = survey_obj.get("user_id").getAsInt();
				String password = survey_obj.get("password").getAsString();
				if(!UserDAO.verify_user(user_id, password)){
					response.status(HttpURLConnection.HTTP_NOT_ACCEPTABLE);
					return -3;
				}	
				//Get Survey
				Survey orig_survey = SurveyDAO.get_survey(Integer.valueOf(request.params("survey_id")));
				if(orig_survey==null){
					response.status(HttpURLConnection.HTTP_NOT_FOUND);
					return -1;
				}
				if(orig_survey.getManaging_user_id()!=user_id){
					response.status(HttpURLConnection.HTTP_NOT_FOUND);
					return -1;
				}
				
				JsonObject responses_json = new JsonObject();

				ArrayList<Response> responses = ResponseDAO.get_responses(Integer.valueOf(request.params("survey_id")));
				ArrayList<Question> questions = QuestionDAO.get_questions(Integer.valueOf(request.params("survey_id")));
				
				ArrayList<Question_for_veiw_JSON_wrapper_send> responses_json_array = new ArrayList<Question_for_veiw_JSON_wrapper_send>(questions.size());
				for(Question q:questions){
					Question_for_veiw_JSON_wrapper_send question_json = new Question_for_veiw_JSON_wrapper_send();
					question_json.text=q.getQuestion_text();
					for(Response r:responses){
						if(r.getResponse_to().getQuestion_id()==q.getQuestion_id()){
							Response_JSON_wrapper_send r_json = new Response_JSON_wrapper_send();
							r_json.answer=r.getAnswer();
							r_json.respondant=r.getRespondant();
							question_json.responses.add(r_json);
						}
					}
					responses_json_array.add(question_json);
				}
				
				responses_json.addProperty("questions_and_responses",new Gson().toJson(responses_json_array));

				response.status(HttpURLConnection.HTTP_OK);
				return responses_json.toString();

			}catch(Exception e){
				response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
				return e.getMessage();
			}
		});
	}
}
