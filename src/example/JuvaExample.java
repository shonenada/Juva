package example;

import juva.Juva;

public class JuvaExample extends Juva {

	public JuvaExample(){
		super(settings.PROJECT_NAME);
		this.setUrlPrefix(settings.URL_PREFIX);
	}

}
