import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "USER")
public class User {
    @Id 
    @GenericGenerator(name="generator", strategy="increment")
    @GeneratedValue(generator="generator")
    @Column(name = "id", nullable=false)
	private int id;
    
    @Column(name = "email", nullable=false, unique=true)
	private String email;
    
    @Column(name = "password_hash", nullable=false)
	private String password_hash;
    
    @Column(name = "first_name")
	private String first_name;
    
    @Column(name = "last_name")
	private String last_name;
	
	
	public User() {}
	public User(String email, String password_hash, String first_name, String last_name) {
		this.email = email;
		this.password_hash = password_hash;
		this.first_name = first_name;
		this.last_name = last_name;
	}
	
	@Id
	public int get_id() {
		return id;
	}
	@Id
	public void set_id(int id) {
		this.id = id;
	}
	public String get_email() {
		return email;
	}
	public void set_email(String email) {
		this.email = email;
	}
	public String get_password_hash() {
		return password_hash;
	}
	public void set_password_hash(String password_hash) {
		this.password_hash = password_hash;
	}
	public String get_first_name() {
		return first_name;
	}
	public void set_first_name(String first_name) {
		this.first_name = first_name;
	}
	public String get_last_name() {
		return last_name;
	}
	public void set_last_name(String last_name) {
		this.last_name = last_name;
	}
}
