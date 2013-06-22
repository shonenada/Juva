package juva.rbac;


public interface User {
    public String getIdentity();
    public Role getRole();
}