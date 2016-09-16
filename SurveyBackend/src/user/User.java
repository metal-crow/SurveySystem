package user;
import javax.persistence.*;

@Entity
@Table(name = "USER")
public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	public int getid() {
		return id;
	}
	public void setid(int id) {
		this.id = id;
	}
	public String getemail() {
		return email;
	}
	public void setemail(String email) {
		this.email = email;
	}
	public String getpassword_hash() {
		return password_hash;
	}
	public void setpassword_hash(String password_hash) {
		this.password_hash = password_hash;
	}
	public String getfirst_name() {
		return first_name;
	}
	public void setfirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getlast_name() {
		return last_name;
	}
	public void setlast_name(String last_name) {
		this.last_name = last_name;
	}
}
