package juva;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Juva extends HttpServlet {
	
	private String PROJECT_NAME = "example";

	private String URL_PREFIX = "/Juva";

	private ClassScanner scanner;

	protected Router routers = new Router(URL_PREFIX);
	
	public Juva(String project) {
		super();
		this.PROJECT_NAME = project;
		scanner = new ClassScanner(this);
	}

	public void destroy() {
		super.destroy();
	}

	public void init() throws ServletException {
		String controllerPath = "/WEB-INF/classes/" + this.PROJECT_NAME + "/controller";
		ServletContext context = this.getServletContext();
		String controllerRealPath = context.getRealPath(controllerPath);
		File controllerClassFiles = new File(controllerRealPath);
		scanner.scanClasses(controllerClassFiles);
	}
	
	public void setUrlPrefix(String urlPrefix){
		this.URL_PREFIX = urlPrefix;
	}
	
	public void setProjectName(String project){
		this.PROJECT_NAME = project;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String uri = request.getRequestURI();
		Controller controller = routers.getController(uri);
		if (controller != null){
			controller.doGet(request, response);
		}else{
			response.sendError(404, "Page Not Found!");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		Controller controller = routers.getController(uri);
		if (controller != null){
			controller.doPost(request, response);
		}else{
			response.sendError(404, "Page Not Found!");
		}
	}

	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		Controller controller = routers.getController(uri);
		if (controller != null){
			controller.doDelete(request, response);
		}else{
			response.sendError(404, "Page Not Found!");
		}
	}

	public void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String uri = request.getRequestURI();
		Controller controller = routers.getController(uri);
		if (controller != null){
			controller.doPut(request, response);
		}else{
			response.sendError(404, "Page Not Found!");
		}
	}

}
