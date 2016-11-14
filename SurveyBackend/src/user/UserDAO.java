package user;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import main.Source;

public class UserDAO {
	
	private static SessionFactory factory;

	public UserDAO(SessionFactory factory) {
		UserDAO.factory=factory;
	}
	
	/**
	 * Testing dao functions
	 */
	public static void testDAO() {		
		String email = Source.random_string(7)+'@'+Source.random_string(4)+".com";
		String pass_hash = Source.random_string(50);
		String first = Source.random_string(7);
		String last = Source.random_string(10);
		
		User user = create_user(email, pass_hash, first, last);
		System.out.println(user.getid());
		assert user.getid()>-1;
		
		boolean result = verify_user(user.getid(), "!");
		assert result:false;
		
		result = verify_user(user.getid(), pass_hash);
		assert result:true;
		
		User user2 = get_user(user.getid());
		assert user2.getfirst_name().equals(first);
		assert user2.getemail().equals(email);
		assert user2.getpassword_hash().equals(pass_hash);
		assert user2.getlast_name().equals(last);
		
		assert delete_user(user.getid());
	}
	
	/**
	 * API request to create user
	 */
	public static User create_user(String email, String password_hash, String first_name, String last_name){
		Session session = factory.openSession();
		Transaction tx = null;
		User user = null;
		try{
			tx = session.beginTransaction();
			user = new User(email, password_hash, first_name, last_name);
			user.setid((Integer) session.save(user)); 
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close(); 
		}
		
		return user;
	}
	
	/**
	 * Delete given user
	 * @param id
	 */
	public static boolean delete_user(int id){
		Session session = factory.openSession();
		Transaction tx = null;
		boolean result = false;
		try{
			tx = session.beginTransaction();
			@SuppressWarnings("rawtypes")
			Query query = session.createQuery("delete USERS where id = :id");
			query.setParameter("id", id);
			result = (query.executeUpdate()==1);
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close(); 
		}
		return result;
	}
	
	/**
	 * API Request (currently double for login and verify user logged in). 
	 * TODO Fix in future.
	 * given passed user id and hash, return if this is accurate
	 */
	public static boolean verify_user(int id, String password_hash){
		Session session = factory.openSession();
		Transaction tx = null;
		boolean password_match=false;
		try{
			tx = session.beginTransaction();
			
			@SuppressWarnings("unchecked")
			Query<Integer> query = session.createQuery("select count(1) from USERS where id = :id and password_hash = :password_hash");
			query.setParameter("id", id);
			query.setParameter("password_hash", password_hash);
			password_match = query.getFirstResult()==1;
			
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close(); 
		}
		return password_match;
	}
	
	/**
	 * Get user object by id
	 * @param id
	 * @return
	 */
	public static User get_user(int id){
		Session session = factory.openSession();
		Transaction tx = null;
		User user=null;
		try{
			tx = session.beginTransaction();
			
            user = (User) session.get(User.class, id);
			
			tx.commit();
		}catch (HibernateException e) {
			if (tx!=null) tx.rollback();
			e.printStackTrace(); 
		}finally {
			session.close(); 
		}
		return user;
	}
}
