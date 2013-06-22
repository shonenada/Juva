package juva.rbac.roles;

import juva.rbac.Resource;
import juva.rbac.Role;
import juva.rbac.User;


public class Everyone implements Role{

    public boolean authenticate(User user, Resource resource) {
        return true;
    }

    public Role getParent() {
        return this;
    }

    public String getRoleName() {
        return "EveryOne";
    }
}
