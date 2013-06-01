package juva;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
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

import juva.Exceptions.TemplateNoVariableKey;

public class Controller extends HttpServlet {

	protected ArrayList _urlPatterns = new ArrayList();

	protected HttpServletRequest _request;
	protected HttpServletResponse _response;
	protected PrintWriter out;
	protected ServletOutputStream outputStream;
	protected ServletContext context;
	protected String rootPath;
	protected Map<String, Object> variables = new HashMap<String, Object>(); 

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
	
	public ArrayList getUrlPatterns(){
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
		this._response.sendError(405, "Method not allow");
	}

	public void post() throws Throwable{
		this._response.sendError(405, "Method not allow");
	}

	public void put() throws Throwable{
		this._response.sendError(405, "Method not allow");
	}
	
	public void delete() throws Throwable{
		this._response.sendError(405, "Method not allow");
	}

	public void doGet(HttpServletRequest request,
			           HttpServletResponse response)
			throws ServletException, IOException {
		initActinon(request, response);
		try {
			this.get();
		} catch (Throwable e) {
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
			this.post();
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
			this.put();
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
			this.delete();
		} catch (Throwable e) {
			// TODO log this message.
			response.sendError(500);
			e.printStackTrace();
		}
	}
	
	public void initActinon(HttpServletRequest request,
			                 HttpServletResponse response)
            throws IOException{
		this._request = request;
		this._response = response;
		this._response.setContentType("text/html;charset=utf-8");
		this.out = this._response.getWriter();
	}

	protected void putVar(String key, Object value){
		variables.put(key, value);
	}
	
	protected void putRs(String key, ResultSet rs){
		ResultSet[] listOfSet = {rs};
		putVar("rs", listOfSet);
	}
	
	protected void putTrueVar(String key){
		variables.put(key, "True");
	}
	
	protected void putFalseVar(String key){
		variables.put(key, "False");
	}
}
