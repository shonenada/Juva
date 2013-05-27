package juva;

import java.io.IOException;
import java.io.PrintWriter;
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

public class Controller extends HttpServlet {

	protected String _urlPattern = "/";

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
		this._urlPattern = urlPattern;
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
	
	public String getUrlPattern(){
		return this._urlPattern;
	}
	
	public void setUrlPattern(String urlPattern){
		this._urlPattern = urlPattern;
	}
	
	public void render(String path) throws IOException{
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

	public void get() throws IOException{
		this._response.setContentType("text/html");
		this._response.sendError(405, "Method not allow");
	}

	public void post() throws IOException{
		this._response.setContentType("text/html");
		this._response.sendError(405, "Method not allow");
	}

	public void put() throws IOException{
		this._response.setContentType("text/html");
		this._response.sendError(405, "Method not allow");
	}
	
	public void delete() throws IOException{
		this._response.setContentType("text/html");
		this._response.sendError(405, "Method not allow");
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		initActinon(request, response);
		this.get();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		initActinon(request, response);
		this.post();
	}

	public void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		initActinon(request, response);
		this.put();
	}

	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		initActinon(request, response);
		this.delete();
	}
	
	public void initActinon(HttpServletRequest request,
			                HttpServletResponse response)
            throws IOException{
		this._request = request;
		this._response = response;
		this.out = this._response.getWriter();
	}

}
