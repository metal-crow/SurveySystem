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

@SuppressWarnings("serial")
//composite primary key class
class ResponsePK implements Serializable{
	private int respondant;
	private int survey_id;
	private int respondant_id;
	
	public ResponsePK(){}
	public ResponsePK(int respondant, int survey_id, int respondant_id) {
		this.respondant = respondant;
		this.survey_id = survey_id;
		this.respondant_id = respondant_id;
	}
	
	@Override
	public String toString(){
		return "respondant:"+respondant+" survey:"+survey_id+" respondant_id:"+respondant_id;
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
	private int respondant;//the responding user. Can be their user id, their hashed user id, or null if informal. An index.
    
	@Id
    @Column(name = "respondant_id")
    private int respondant_id;//if informal response, this is incremented for each new user response to survey.

	@Id
    private int survey_id;//survey being responded to. An index.
        
	@ManyToOne(targetEntity=Question.class)
    private Question response_to;//the question this is an answer to

    @Column(name = "answer", nullable = false)
    private String answer;
    
    public Response(){}
    
    public Response(int respondant, int survey_id, int respondant_id, Question response_to, String answer) {
		this.respondant = respondant;
		this.survey_id = survey_id;
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

	public int getSurvey_id() {
		return survey_id;
	}

	public void setSurvey_id(int survey_id) {
		this.survey_id = survey_id;
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
