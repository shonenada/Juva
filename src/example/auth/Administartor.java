package example.auth;

import juva.rbac.Resource;
import juva.rbac.Role;
import juva.rbac.User;
import juva.rbac.roles.Everyone;

public class Administartor extends Everyone{

	@Override
	public boolean authenticate(User user, Resource resource) {
		if (user != null){
			return (getParent().authenticate(user, resource)
					 && user.getIdentity().equals("5"));
		}
		return false;
	}

	@Override
	public Role getParent() {
		return new LocalUser();
	}

	@Override
	public String getRoleName() {
		return "Administartor";
	}

}