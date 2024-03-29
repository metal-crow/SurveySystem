package question;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import survey.Survey;

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
		S_Char;
		
		public static Response_Type fromInt(int in){
			switch (in) {
				case 0:
					return S_String;
				case 1:
					return S_Date;
				case 2:
					return S_Int;
				case 3:
					return S_Float;
				case 4:
					return S_Bool;
				case 5:
					return S_Char;
				default:
					return null;
			}
		}
		
		public static int toInt(Response_Type in){
			switch (in) {
				case S_String:
					return 0;
				case S_Date:
					return 1;
				case S_Int:
					return 2;
				case S_Float:
					return 3;
				case S_Bool:
					return 4;
				case S_Char:
					return 5;
				default:
					return -1;
			}
		}
	}
	
	@ManyToOne(targetEntity=Survey.class, optional=false, cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)
	private Survey survey;//survey this is a question for
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto increment
    @Column(name = "question_id", nullable=false)
    private int question_id;
    
    @Column(name = "question_text", nullable=false)
    private String question_text;
    
    @Column(name = "response_type", nullable=false)
    private Response_Type response_type;//what to deseralize the response to

    public Question() {}
    
	public Question(Survey survey, String question_text, Response_Type response_type) {
		this.survey = survey;
		this.question_text = question_text;
		this.response_type = response_type;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
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
