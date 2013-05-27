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
	private PrintWriter out;

	protected Router routers = new Router(URL_PREFIX);
	
	public Juva(String project) {
		super();
		this.PROJECT_NAME = project;
	}

	public void destroy() {
		super.destroy();
	}

	public void init() throws ServletException {
		scanInit();
	}
	
	public void setUrlPrefix(String urlPrefix){
		this.URL_PREFIX = urlPrefix;
	}
	
	public void setProjectName(String project){
		this.PROJECT_NAME = project;
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		initPrinter(response);
		String uri = request.getRequestURI();
		Controller controller = makeController(uri);

		if (controller != null){
			controller.doGet(request, response);
		}else{
			response.sendError(404, "Page Not Found!");
		}
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		initPrinter(response);
		String uri = request.getRequestURI();
		Controller controller = makeController(uri);
		
		if (controller != null){
			controller.doPost(request, response);
		}else{
			response.sendError(404, "Page Not Found!");
		}
		
	}

	public void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		initPrinter(response);
		String uri = request.getRequestURI();
		Controller controller = makeController(uri);
		
		if (controller != null){
			controller.doDelete(request, response);
		}else{
			response.sendError(404, "Page Not Found!");
		}
		
	}

	public void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		initPrinter(response);
		String uri = request.getRequestURI();
		Controller controller = makeController(uri);
		
		if (controller != null){
			controller.doPut(request, response);
		}else{
			response.sendError(404, "Page Not Found!");
		}

	}
	
	public void initPrinter(HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=utf-8");
		this.out = response.getWriter();
	}
	
	public Controller makeController(String uri){
		
		Controller controller = routers.getController(uri);
		ServletContext context = this.getServletContext();
		String rootPath = context.getRealPath("/");
		controller.setContext(context);
		controller.setPath(rootPath);
		
		return controller;
	}
	
	public void scanInit(){
		scanner = new ClassScanner(this);
		String controllerPath = "/WEB-INF/classes/" + this.PROJECT_NAME + "/controller";
		ServletContext context = this.getServletContext();
		String controllerRealPath = context.getRealPath(controllerPath);
		File controllerClassFiles = new File(controllerRealPath);
		try {
			scanner.scanClasses(controllerClassFiles);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
