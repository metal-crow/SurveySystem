package response;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import question.Question;
import survey.Survey;

@SuppressWarnings("serial")
//composite primary key class
class ResponsePK implements Serializable{
	@SuppressWarnings("unused")
	private int respondant;
	@SuppressWarnings("unused")
	private Survey survey;
	@SuppressWarnings("unused")
	private int respondant_id;
	
	public ResponsePK(int respondant, Survey survey, int respondant_id) {
		this.respondant = respondant;
		this.survey = survey;
		this.respondant_id = respondant_id;
	}
}

@Entity(name = "RESPONSES")
@Table(name = "RESPONSES", indexes = {
        @Index(columnList = "respondant", name = "respondant_hidx")
	   })
@IdClass(ResponsePK.class)
public class Response {
	@Id
	@Column(name = "respondant", nullable = true)
	private int respondant;//the responding user. Can be their user id, their hashed user id, or null if informal. An index.
    
	@Id
    @Column(name = "respondant_id", nullable = true)
    private int respondant_id;//if informal response, this is incremented for each new user response to survey.

	@Id
	@ManyToOne(targetEntity=Survey.class)
    private Survey survey;//survey being responded to. An index.
        
	@ManyToOne(targetEntity=Question.class)
    private Question response_to;//the question this is an answer to

    @Column(name = "answer", nullable = false)
    private String answer;
    
    public Response(){}
    
    public Response(int respondant, Survey survey, int respondant_id, Question response_to, String answer) {
		this.respondant = respondant;
		this.survey = survey;
		this.respondant_id = respondant_id;
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
