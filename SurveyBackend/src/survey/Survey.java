package survey;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import user.User;

@Entity(name = "SURVEYS")
public class Survey {
	/**The different types of responses allowed for a survey*/
	public enum User_Response_Type{
		Informal,//no user login required, anyone can response and respond more than 1 time
		Anonymous,//user login required and checked, but user id hashed on submission
		User//full user id saved
	}
	
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto increment
    @Column(name = "id", nullable=false)
	private int id;

    @Column(name = "survey_name", nullable = false)
	private String survey_name;

    @Column(name = "user_response_type", nullable = false)
    private User_Response_Type user_response_type;//enum of how the user response is recorded
    
    @Column(name = "respondant_id_count", nullable = true)
    private int respondant_id_count = 0;//count for the number of users responded, if informal response used
    
    @Column(name = "closing", nullable = false)
    private Date closing;//date and time survey closes

    @Column(name = "deleting", nullable = false)
    private Date deleting;//date and time survey is deleted
    
    @ManyToOne(targetEntity=User.class)
    private User managing_user;
        
    public Survey(){}
    
    public Survey(String name, User_Response_Type user_response_type, Date closing, Date deleting, User managing_user) {
    	this.survey_name = name;
    	this.user_response_type = user_response_type;
    	this.closing = closing;
    	this.deleting = deleting;
    	this.managing_user = managing_user;
	}

	public int getRespondant_id_count() {
		return respondant_id_count;
	}

	public void setRespondant_id_count(int respondant_id_count) {
		this.respondant_id_count = respondant_id_count;
	}

	public User getManaging_user() {
		return managing_user;
	}

	public void setManaging_user(User managing_user) {
		this.managing_user = managing_user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSurvey_name() {
		return survey_name;
	}

	public void setSurvey_name(String survey_name) {
		this.survey_name = survey_name;
	}

	public User_Response_Type getUser_response_type() {
		return user_response_type;
	}

	public void setUser_response_type(User_Response_Type user_response_type) {
		this.user_response_type = user_response_type;
	}

	public Date getClosing() {
		return closing;
	}

	public void setClosing(Date closing) {
		this.closing = closing;
	}

	public Date getDeleting() {
		return deleting;
	}

	public void setDeleting(Date deleting) {
		this.deleting = deleting;
	}
}
