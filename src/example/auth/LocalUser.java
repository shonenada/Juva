package example.auth;

import juva.rbac.*;
import juva.rbac.roles.*;


public class LocalUser extends Everyone{

	@Override
	public boolean authenticate(User user, Resource resource) {
		if (user != null){
			return (getParent().authenticate(user, resource)
					 && user.getIdentity() != null);
		}
		return false;
	}

	@Override
	public Role getParent() {
		return new Everyone();
	}

	@Override
	public String getRoleName() {
		return "Local User";
	}

}