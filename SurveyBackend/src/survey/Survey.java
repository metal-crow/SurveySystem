package survey;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "SURVEYS")
public class Survey {
	/**The different types of responses allowed for a survey*/
	public enum User_Response_Type{
		Informal,//no user login required, anyone can response and respond more than 1 time
		Anonymous,//user login required and checked, but user id hashed on submission
		User;//full user id saved
		
		public static User_Response_Type fromInt(int in){
			switch (in) {
				case 0:
					return User_Response_Type.Informal;
				case 1:
					return User_Response_Type.Anonymous;
				case 2:
					return User_Response_Type.User;
				default:
					return null;
			}
		}
	}
	
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto increment
    @Column(name = "id", nullable=false)
	private int id;

    @Column(name = "survey_name", nullable = false)
	private String survey_name;

    @Column(name = "user_response_type", nullable = false)
    private User_Response_Type user_response_type;//enum of how the user response is recorded
    
    @Column(name = "respondant_id_count")
    private int respondant_id_count = 0;//count for the number of users responded, if informal response used
    
    @Column(name = "closing", nullable = false)
    private Date closing;//date and time survey closes

    @Column(name = "deleting", nullable = false)
    private Date deleting;//date and time survey is deleted
    
    @Column(name = "managing_user_id")
    private int managing_user_id;//Don't bother making this a stored class since we don't have it when creating survey
        
    public Survey(){}
    
    public Survey(String name, User_Response_Type user_response_type, Date closing, Date deleting, int managing_user_id) {
    	this.survey_name = name;
    	this.user_response_type = user_response_type;
    	this.closing = closing;
    	this.deleting = deleting;
    	this.managing_user_id = managing_user_id;
	}

	public int getRespondant_id_count() {
		return respondant_id_count;
	}

	public void setRespondant_id_count(int respondant_id_count) {
		this.respondant_id_count = respondant_id_count;
	}

	public int getManaging_user_id() {
		return managing_user_id;
	}

	public void setManaging_user_id(int managing_user_id) {
		this.managing_user_id = managing_user_id;
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
