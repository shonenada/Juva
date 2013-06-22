package juva;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class ClassScanner {

    private Juva _juva = null;

    public ClassScanner(Juva juva){
        this._juva = juva;
    }

    public void scanClasses(File path) throws Exception{
        if (path.isFile()){
            loadClass(path);
        }
        else{
            File[] files = path.listFiles();
            for(int i=0;i<files.length;++i){
                scanClasses(files[i]);
            }
        }
    }

    /**
     * Load controller.
     */
    private void loadClass(File classFile) throws Exception{
        String path = classFile.getAbsolutePath();
        ClassLoader parentLoader = Controller.class.getClassLoader();
        JClassLoader loader = new JClassLoader(parentLoader);
        Class controllerClass = loader.load(path);
        Controller controller = (Controller) controllerClass.newInstance();
        ArrayList urls = controller.getUrlPatterns();
        for(int i=0;i<urls.size();++i){
            String url = (String) urls.get(i);
            this._juva.routers.addRouter(url, controller);
        }
    }

    class JClassLoader extends ClassLoader{

        public JClassLoader(ClassLoader parent){
            super(parent);
        }

        public Class load(String file) throws Exception{
            int temp;
            int loadedLength = 0;
            FileInputStream classFileStream = new FileInputStream(file);
            int classLength = classFileStream.available();
            byte[] buffer = new byte[classLength];
            while(loadedLength < classLength){
                temp = classFileStream.read(buffer, loadedLength,
                	                        classLength - loadedLength);
                loadedLength += temp;
            }
            return super.defineClass(null, buffer, 0, classLength);
        }
    }  

}