package juva;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Juva extends HttpServlet {
	
	protected String PROJECT_NAME;
	protected String URL_PREFIX;
	protected ClassScanner scanner;
	protected PrintWriter out;

	protected Router routers = new Router();
	
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
		routers.setPrefix(urlPrefix);
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
			controller.setSession(request.getSession());
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
			controller.setSession(request.getSession());
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
			controller.setSession(request.getSession());
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
			controller.setSession(request.getSession());
			controller.doPut(request, response);
		}else{
			response.sendError(404, "Page Not Found!");
		}

	}
	
	public void initPrinter(HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=utf-8");
		this.out = response.getWriter();
	}
	
	public Controller makeController(ArrayList uris){
		Controller controller;
		for (int i=0;i<uris.size(); ++i){
			String url = (String) uris.get(i);
			controller = makeController(url);
			if (controller != null){
				return controller;
			}
		}
		return null;
	}
	
	public Controller makeController(String uri){
		Controller controller = routers.getController(uri);
		if (controller != null){
			
			ServletContext context = this.getServletContext();
			String rootPath = context.getRealPath("/");
			controller.setContext(context);
			controller.setPath(rootPath);

			return controller;
		}
		return null;
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
