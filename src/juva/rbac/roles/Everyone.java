package juva.rbac.roles;

import juva.rbac.Resource;
import juva.rbac.Role;
import juva.rbac.User;

public class Everyone implements Role{

	@Override
	public boolean authenticate(User user, Resource resource) {
		return true;
	}

	@Override
	public Role getParent() {
		return this;
	}

	@Override
	public String getRoleName() {
		return "EveryOne";
	}
	
}
