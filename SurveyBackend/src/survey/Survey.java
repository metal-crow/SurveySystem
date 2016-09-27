package survey;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import user.User;

/**The different types of responses allowed for a survey*/
enum User_Response_Type{
	Informal,//no user login required, anyone can response and respond more than 1 time
	Anonymous,//user login required and checked, but user id hashed on submission
	User//full user id saved
}

@Entity
@Table(name = "SURVEYS")
public class Survey {
	
	public static final int MAX_QUESTIONS = 30;//the maximum possible questions a survey can have
	
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto increment
    @Column(name = "id", nullable=false)
	private int id;

    @Column(name = "survey_name", nullable = false)
	private String survey_name;

    @Column(name = "user_response_type", nullable = false)
    private User_Response_Type user_response_type;
    
    @Column(name = "closing", nullable = false)
    private Date closing;

    @Column(name = "deleting", nullable = false)
    private Date deleting;
    
    @Column(name = "managing_user_id")
    private int managing_user_id;
        
    public Survey(String name, User_Response_Type user_response_type, Date closing, Date deleting, User managing_user) {
    	this.survey_name = name;
    	this.user_response_type = user_response_type;
    	this.closing = closing;
    	this.deleting = deleting;
    	this.managing_user_id = managing_user.getid();
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

	public int getManaging_user_id() {
		return managing_user_id;
	}

	public void setManaging_user_id(int managing_user_id) {
		this.managing_user_id = managing_user_id;
	}
}
