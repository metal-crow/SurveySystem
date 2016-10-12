package question;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "QUESTIONS")
public class Question {
	/**
	 * The types of responses available for a survey question.
	 * Use functions for seralizing/deseralizing each when storing/retrieving from db.
	 */
	public enum Response_Type {
		S_String,
		S_Date,//year,month,day,hour,minute,second,millisecond
		S_Int,
		S_Float,
		S_Bool,
		S_Char,
	}
	
    @Column(name = "survey", nullable = false)
	private int survey;//survey this is a question for
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto increment
    @Column(name = "question_id", nullable=false)
    private int question_id;
    
    @Column(name = "question_text", nullable=false)
    private String question_text;
    
    @Column(name = "response_type", nullable=false)
    private Response_Type response_type;//what to deseralize the response to

	public Question(int survey, String question_text, Response_Type response_type) {
		this.survey = survey;
		this.question_text = question_text;
		this.response_type = response_type;
	}

	public int getSurvey() {
		return survey;
	}

	public void setSurvey(int survey) {
		this.survey = survey;
	}

	public int getQuestion_id() {
		return question_id;
	}

	public void setQuestion_id(int question_id) {
		this.question_id = question_id;
	}

	public String getQuestion_text() {
		return question_text;
	}

	public void setQuestion_text(String question_text) {
		this.question_text = question_text;
	}

	public Response_Type getResponse_type() {
		return response_type;
	}

	public void setResponse_type(Response_Type response_type) {
		this.response_type = response_type;
	}
}
