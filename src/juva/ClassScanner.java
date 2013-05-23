package juva;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ClassScanner {
   	
	private Juva _juva = null;
	
	public ClassScanner(Juva juva){
	   	this._juva = juva;
	}
	
	public void scanClasses(File path){
		try{
			if (path.isFile()){
				loadClass(path);
			}
			else{
				File[] files = path.listFiles();
				for(int i=0;i<files.length;++i){
					scanClasses(files[i]);
				}
			}
		}catch(Exception e){
			System.out.println(e.getStackTrace());
		}
	}
	
	private void loadClass(File classFile){
		try {
			String path = classFile.getAbsolutePath();
			JClassLoader loader = new JClassLoader(Controller.class.getClassLoader());
			Class controllerClass = loader.load(path);
			Controller controller = (Controller) controllerClass.newInstance();
			this._juva.routers.addRouter(controller.getUrlPattern(), controller);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class JClassLoader extends ClassLoader{
	    public JClassLoader(ClassLoader parent){  
	        super(parent);
	    }  

	    public Class load(String file){
	    	try{
	    		int temp;
	    		int loadedLength = 0;
	    		FileInputStream classFileStream = new FileInputStream(file);
	    		int classLength = classFileStream.available();
	    		byte[] buffer = new byte[classLength];
	    		while(loadedLength < classLength){
	    			temp = classFileStream.read(buffer, loadedLength, classLength - loadedLength);
	    			loadedLength += temp;
	    		}
	    		return super.defineClass(null, buffer, 0, classLength);
	    	}catch(Exception e){
	    		System.out.println(e.getStackTrace());
	    	}
	    	return null;
	    }
	}  

}