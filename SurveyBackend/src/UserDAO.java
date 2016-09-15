import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class UserDAO {
	
	private static SessionFactory factory;
	
	public static void main(String[] args) {
		try{
			
			factory = new Configuration()
						//Add all classes here
						.addAnnotatedClass(User.class)
						.configure()
						.buildSessionFactory();
		}catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex); 
		}
		
		UserDAO dao = new UserDAO();
		dao.create_user("test@aol.com", "1d1d2m1098c1c1c1c2", "Bob","Lastname");
		factory.close();
	}
	/**
	 * This opens the database to the Users table, or creates it if it doesnt exist
	 */
	public static void open_db_connection(){
		
	}
	
	/**
	 * API request
	 * Verify email is valid
	 * Verify non duplicate email
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
	public void verify_user(){
		
	}
	
}
