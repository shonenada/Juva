package juva;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller extends HttpServlet {

	protected String _urlPattern = "/";

	protected HttpServletRequest _request;
	protected HttpServletResponse _response;

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
	
	public String getUrlPattern(){
		return this._urlPattern;
	}
	
	public void setUrlPattern(String urlPattern){
		this._urlPattern = urlPattern;
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
		this._request = request;
		this._response = response;
		this.get();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this._request = request;
		this._response = response;
		this.post();
	}

	public void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this._request = request;
		this._response = response;
		this.put();
	}

	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this._request = request;
		this._response = response;
		this.delete();
	}

	public void init() throws ServletException {
		
	}

}
