package user;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class UserDAO {
	
	private static SessionFactory factory;

	public UserDAO(SessionFactory factory) {
		UserDAO.factory=factory;
	}
	
	/**
	 * API request to create user
	 */
	public int create_user(String email, String password_hash, String first_name, String last_name){
		Session session = factory.openSession();
		Transaction tx = null;
		Integer user_id = null;
		try{
			tx = session.beginTransaction();
			User user = new User(email, password_hash, first_name, last_name);
			user_id = (Integer) session.save(user); 
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close(); 
		}
		return user_id;
	}
	
	/**
	 * API Request (currently double for login and verify user logged in). Fix in future.
	 * given passed user email and hash, return if this is accurate
	 */
	public boolean verify_user(int id, String password_hash){
		return true;
	}
	
}
