package juva.rbac;

import javax.servlet.http.HttpServletRequest;

public interface User {
	
	public String getIdentity();
	
	public Role getRole();
	
	public User getCurrentUser(HttpServletRequest request) throws Throwable;
	
}
