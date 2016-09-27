package response;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "RESPONSES", indexes = {
        @Index(columnList = "respondant", name = "respondant_hidx"),
        @Index(columnList = "survey", name = "survey_hidx")
	   })
public class Response {

	/**
	 * The types of responses available for a survey question.
	 * Use functions for seralizing/deseralizing each when storing/retrieving from db.
	 */
	public enum Response_Type {
		S_String,
		S_Date,
		S_Int,
		S_Float,
		S_Bool,
	}
	
    @Column(name = "respondant", nullable = true)
	private int respondant;//the responding user. Can be their user id, their hashed user id, or null if informal. An index
    
    @Column(name = "survey", nullable = false)
    private int survey;//survey being responded to. An index
    
    @Column(name = "respondant_id", nullable = true)
    private int respondant_id;//if informal response, this is incremented for each new user response to survey
    
    
}
