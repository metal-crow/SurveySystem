package question;

import org.hibernate.SessionFactory;

public class QuestionDAO {

	private static SessionFactory factory;
	
	public QuestionDAO(SessionFactory factory) {
		QuestionDAO.factory=factory;
	}

	
}
	