package juva;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import juva.Exceptions.AuthenticateFailedException;
import juva.database.Model;
import juva.rbac.PermissionTable;
import juva.rbac.Role;
import juva.rbac.User;
import juva.rbac.roles.Everyone;

public class Controller extends HttpServlet {

	protected ArrayList _urlPatterns = new ArrayList();

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected PrintWriter out;
	protected ServletOutputStream outputStream;
	protected ServletContext context;
	protected String rootPath;
	protected HttpSession session;
	protected Map<String, Object> variables = new HashMap<String, Object>();
	protected User currentUser;

	protected PermissionTable permissionTable; 
	
	public Controller() {
		super();
	}
	
	public Controller(String urlPattern) {
		super();
		this.addUrlPattern(urlPattern);
		variables.clear();
	}
	
	public Controller(String[] urlPatterns) {
		super();
		for(int i=0;i<urlPatterns.length; ++i){
			this.addUrlPattern(urlPatterns[i]);
		}
		variables.clear();
	}

	public void destroy() {
		super.destroy();
	}

	public void setContext(ServletContext context){
		this.context = context;
	}
	
	public void setPath(String path){
		this.rootPath = path;
	}
	
	public ArrayList<String> getUrlPatterns(){
		return this._urlPatterns;
	}
	
	public void addUrlPattern(String urlPattern){
		this._urlPatterns.add(urlPattern);
	}
	
	public void render(String path) throws IOException, SQLException{
		Template template = new Template(rootPath + "/templates/" + path);
		Set<String> keys = variables.keySet();
		Iterator iterator = keys.iterator();
		while (iterator.hasNext()){
			String key = (String) iterator.next();
			Object value = variables.get(key);
			template.putVariables(key, value);
		}
		String html = template.render();
		out.print(html);
	}

	public void get() throws Throwable{
		this.response.sendError(405, "Method not allow");
	}

	public void post() throws Throwable{
		this.response.sendError(405, "Method not allow");
	}

	public void put() throws Throwable{
		this.response.sendError(405, "Method not allow");
	}
	
	public void delete() throws Throwable{
		this.response.sendError(405, "Method not allow");
	}

	public void doGet(HttpServletRequest request,
			           HttpServletResponse response)
			throws ServletException, IOException {
		initActinon(request, response);
		try {
			this.authenticate(PermissionTable.METHODS.GET);
			this.get();
		}
		catch (AuthenticateFailedException e) {
			response.sendError(405, "Method Not Allow");
		}
		catch (Throwable e) {
			// TODO log this message.
			response.sendError(500);
			e.printStackTrace();
		}
		
	}

	public void doPost(HttpServletRequest request,
			            HttpServletResponse response)
			throws ServletException, IOException {
		initActinon(request, response);
		try {
			this.authenticate(PermissionTable.METHODS.POST);
			this.get();
		}
		catch (AuthenticateFailedException e) {
			response.sendError(405, "Method Not Allow");
		} catch (Throwable e) {
			// TODO log this message.
			response.sendError(500);
			e.printStackTrace();
		}
	}

	public void doPut(HttpServletRequest request,
			           HttpServletResponse response)
			throws ServletException, IOException {
		initActinon(request, response);
		try {
			this.authenticate(PermissionTable.METHODS.PUT);
			this.get();
		}
		catch (AuthenticateFailedException e) {
			response.sendError(405, "Method Not Allow");
		} catch (Throwable e) {
			// TODO log this message.
			response.sendError(500);
			e.printStackTrace();
		}
	}

	public void doDelete(HttpServletRequest request,
			              HttpServletResponse response)
			throws ServletException, IOException {
		initActinon(request, response);
		try {
			this.authenticate(PermissionTable.METHODS.DELETE);
			this.get();
		}
		catch (AuthenticateFailedException e) {
			response.sendError(405, "Method Not Allow");
		} catch (Throwable e) {
			// TODO log this message.
			response.sendError(500);
			e.printStackTrace();
		}
	}

	public void initActinon(HttpServletRequest request,
			                 HttpServletResponse response)
            throws IOException{
		this.request = request;
		this.response = response;
		this.response.setContentType("text/html;charset=utf-8");
		this.setContext(context);
		session = request.getSession(true);
		this.out = this.response.getWriter();
		Utils.Json.registerPrinter(out);
		currentUser.beCurrentUser(request);
	}

	public void authenticate(PermissionTable.METHODS method)
	        throws IOException, AuthenticateFailedException{
		Role currentRole;
		
		if (this.currentUser == null){
			currentRole = new Everyone();
		}else{
			currentRole = this.currentUser.getRole();
		}
		
		boolean allow = this.permissionTable.accessiable(currentRole, method);
		if (!allow){
			throw new AuthenticateFailedException();
		}
		
	}
	
	protected void putVar(String key, Object value){
		variables.put(key, value);
	}
	
	protected void putModel(String key, Model model){
		variables.put(key, model);
	}
	
	protected void putTrueVar(String key){
		variables.put(key, "True");
	}
	
	protected void putFalseVar(String key){
		variables.put(key, "False");
	}
}
