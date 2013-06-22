package juva.rbac;

import java.util.ArrayList;

import juva.Controller;


public class Resource {

    private String resourceName;

    public Resource(String name){
        this.resourceName = name;
    }

    public String getUrl(){
        return this.resourceName;
    }

}
