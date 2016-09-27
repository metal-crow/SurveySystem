package response;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Index;
import javax.persistence.Table;

@SuppressWarnings("serial")
//composite primary key class
class ResponsePK implements Serializable{
	@SuppressWarnings("unused")
	private int respondant;
	@SuppressWarnings("unused")
	private int survey;
	@SuppressWarnings("unused")
	private int respondant_id;
	
	public ResponsePK(int respondant, int survey, int respondant_id) {
		this.respondant = respondant;
		this.survey = survey;
		this.respondant_id = respondant_id;
	}
}

@Entity
@Table(name = "RESPONSES", indexes = {
        @Index(columnList = "respondant", name = "respondant_hidx"),
        @Index(columnList = "survey", name = "survey_hidx")
	   })
@IdClass(ResponsePK.class)
public class Response {
	@Id
	@Column(name = "respondant", nullable = true)
	private int respondant;//the responding user. Can be their user id, their hashed user id, or null if informal. An index.
    
	@Id
    @Column(name = "survey", nullable = false)
    private int survey;//survey being responded to. An index.
    
	@Id
    @Column(name = "respondant_id", nullable = true)
    private int respondant_id;//if informal response, this is incremented for each new user response to survey.
    
    @Column(name = "response_to", nullable = false)
    private int response_to;//the question this is an answer to

    @Column(name = "answer", nullable = false)
    private String answer;
    
    //TODO
    public Response(int respondant, int survey, int respondant_id, int response_to, String answer) {
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

	public int getSurvey() {
		return survey;
	}

	public void setSurvey(int survey) {
		this.survey = survey;
	}

	public int getRespondant_id() {
		return respondant_id;
	}

	public void setRespondant_id(int respondant_id) {
		this.respondant_id = respondant_id;
	}

	public int getResponse_to() {
		return response_to;
	}

	public void setResponse_to(int response_to) {
		this.response_to = response_to;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
