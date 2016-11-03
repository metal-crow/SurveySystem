package survey;

import static spark.Spark.post;

import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import question.Question.Response_Type;
import question.QuestionDAO;
import survey.Survey.User_Response_Type;

public class SurveyAPI {

	public SurveyAPI(){
		setupEndpoints();
	}
	
	//the object eqch question must be passed ad
	class Question_JSON_wrapper{
		String text;
		int response_type;
	}

	private void setupEndpoints() {
		
		/**
		 * Create survey and all questions corresponding to survey
		 * Requires json arguments
		 * 	@param survey_name
		 * 	@param response_type - int corresponding to User_Response_Type
		 * 	@param closing_date
		 * 	@param deleting_date
		 * 	@param user_id - user who created and now owns the survey
		 * 	
		 * @return id of created survey
		 */
		post("/createSurvey", "application/json", (request, response) -> {
			try{
				JsonObject survey_obj = new JsonParser().parse(request.body()).getAsJsonObject();

				//create survey object
				String survey_name = survey_obj.get("survey_name").getAsString();
				User_Response_Type response_type = User_Response_Type.fromInt(survey_obj.get("response_type").getAsInt());
				DateFormat df = DateFormat.getDateInstance();
				Date closing = df.parse(survey_obj.get("closing_date").getAsString());
				Date deleting = df.parse(survey_obj.get("deleting_date").getAsString());
				int user_id = survey_obj.get("user_id").getAsInt();

				Survey survey = SurveyDAO.create_survey(survey_name, response_type, closing, deleting, user_id);
				
				if(survey==null){
					response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
					return -1;
				}
				
				//create questions for survey
				Question_JSON_wrapper[] questions = new Gson().fromJson(survey_obj.get("questions"), Question_JSON_wrapper[].class);
				for(Question_JSON_wrapper question:questions){
					QuestionDAO.create_question(survey, question.text, Response_Type.fromInt(question.response_type));
				}
				
				response.status(HttpURLConnection.HTTP_CREATED);
				return survey.getId();
			}catch(Exception e){
				response.status(HttpURLConnection.HTTP_INTERNAL_ERROR);
				return e.getMessage();
			}
		});
	}
}
