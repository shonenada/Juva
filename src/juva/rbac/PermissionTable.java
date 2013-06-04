package juva.rbac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import juva.rbac.roles.Everyone;

public class PermissionTable {
	
	public enum METHODS{ GET, POST, PUT, DELETE }

	private Resource resource;
	private Map<Role, ArrayList<METHODS> > allowMethod =
		new HashMap<Role, ArrayList<METHODS> >();
	private Map<Role, ArrayList<METHODS> > denyMethod =
		new HashMap<Role, ArrayList<METHODS> >();
	
	public PermissionTable(Resource resource){
		this.resource = resource;
	}
	
	public void allow(Role role, METHODS method){
		ArrayList<METHODS> allowList = this.allowMethod.get(role);
		if (allowList == null){
			allowList = new ArrayList<METHODS>();
			this.allowMethod.put(role, allowList);
		}
		allowList.add(method);
	}
	
	public void deny(Role role, METHODS method){
		ArrayList<METHODS> denyList = this.denyMethod.get(role);
		if (denyList == null){
			denyList = new ArrayList<METHODS>();
			this.denyMethod.put(role, denyList);
		}
		denyList.add(method);
	}
	
	public boolean accessiable(Role role, METHODS method){
	
		if (method == null){
			return false;
		}
		
		if (role == null){
			role = new Everyone();
		}

		ArrayList<METHODS> allowList = this.allowMethod.get(role);
		ArrayList<METHODS> denyList = this.allowMethod.get(role);
		
		int allowIndex = allowList.indexOf(method);
		int denyIndex = denyList.indexOf(method);
		
		return (allowIndex != -1 && denyIndex == -1);
	}
	
}
