package example.auth;

import juva.rbac.Resource;
import juva.rbac.Role;
import juva.rbac.User;
import juva.rbac.roles.Everyone;


public class Administrator extends Everyone{

    public boolean authenticate(User user, Resource resource) {
        if (user != null){
            return (getParent().authenticate(user, resource) &&
                    user.getIdentity().equals("5"));
        }
        return false;
    }

    public Role getParent() {
        return new LocalUser();
    }

    public String getRoleName() {
        return "Administartor";
    }

}