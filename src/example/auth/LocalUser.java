package example.auth;

import juva.rbac.*;
import juva.rbac.roles.*;


public class LocalUser extends Everyone{

    public boolean authenticate(User user, Resource resource) {
        if (user != null){
            return (getParent().authenticate(user, resource) &&
            	    user.getIdentity() != null);
        }
        return false;
    }

    public Role getParent() {
        return new Everyone();
    }

    public String getRoleName() {
        return "Local User";
    }

}