package juva.rbac;

public interface Role {
	
	public String getRoleName();
	
	public boolean authenticate(User user, Resource resource);
	
	public Role getParent();
	
}
