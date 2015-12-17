package googleServlets;

public class User {
	private Integer userId;
	private String sessionId;
	private Boolean isLoggedIn = false;
	
	public User(int id) {
		this.userId = id;
	}
	
	public Integer getUserId() {
		return this.userId;
	}
	
	public String getSessionId() {
		return this.sessionId;
	}
	
	public Boolean getIsLoggedIn() {
		return this.isLoggedIn;
	}
}
