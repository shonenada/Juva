package juva.rbac;


public class Resource {

    private String resourceName;

    public Resource(String name){
        this.resourceName = name;
    }

    public String getUrl(){
        return this.resourceName;
    }

}
