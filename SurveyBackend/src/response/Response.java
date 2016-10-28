package response;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import question.Question;
import survey.Survey;
import survey.Survey.User_Response_Type;

@SuppressWarnings("serial")
//composite primary key class
class ResponsePK implements Serializable{
	@SuppressWarnings("unused")
	private int respondant;
	@SuppressWarnings("unused")
	private Survey survey;
	@SuppressWarnings("unused")
	private int respondant_id;
	@SuppressWarnings("unused")
	private Question response_to;
	
	public ResponsePK(){}
	public ResponsePK(int respondant, Survey survey, int respondant_id, Question response_to) {
		this.respondant = respondant;
		this.survey = survey;
		this.respondant_id = respondant_id;
		this.response_to = response_to;
	}
}

@Entity(name = "RESPONSES")
@Table(name = "RESPONSES", indexes = {
        @Index(columnList = "respondant", name = "respondant_hidx")
	   })
@IdClass(ResponsePK.class)
public class Response {
	@Id
	@Column(name = "respondant")
	private int respondant;//the responding user. Can be their user id, their hashed user id, or -1 if informal. An index.
    
	@Id
    @Column(name = "respondant_id")
    private int respondant_id;//if informal response, this is incremented for each new user response to survey.

	@Id
	@ManyToOne(targetEntity=Survey.class, optional=false, cascade=CascadeType.PERSIST, fetch=FetchType.LAZY)//not going to need survey that much with response
    private Survey survey;//survey being responded to. An index.
        
	@Id
	@ManyToOne(targetEntity=Question.class,optional=false, cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)//need question to deserialize, so need nearly always
    private Question response_to;//the question this is an answer to. An index

    @Column(name = "answer", nullable = false)
    private String answer;
    
    public Response(){}
    
    public Response(Integer respondant, Survey survey, Question response_to, String answer) {
    	if(survey.getUser_response_type()==User_Response_Type.Informal){
    		this.respondant = -1;
    	}else{
    		this.respondant = respondant;
    	}
		this.survey = survey;
		this.respondant_id = survey.getRespondant_id_count();
		this.response_to = response_to;
		this.answer = answer;
	}

	public int getRespondant() {
		return respondant;
	}

	public void setRespondant(int respondant) {
		this.respondant = respondant;
	}

	public int getRespondant_id() {
		return respondant_id;
	}

	public void setRespondant_id(int respondant_id) {
		this.respondant_id = respondant_id;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public Question getResponse_to() {
		return response_to;
	}

	public void setResponse_to(Question response_to) {
		this.response_to = response_to;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
