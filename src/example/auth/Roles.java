package example.auth;

import juva.rbac.Role;


public class Roles extends juva.rbac.Roles{

	public static Role LocalUser = new LocalUser();
	
	public static Role Administartor = new Administartor();
	
}
