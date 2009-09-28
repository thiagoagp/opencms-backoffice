package com.mashfrog.backoffice.project.beans.rowvalue;

public class TitledRowValueBean implements RowValueBean {
    protected String title;
    protected String type;
    protected boolean enabled;

    public TitledRowValueBean(){
    	this(null);
    }

    public TitledRowValueBean(String title){
    	setTitle(title);
    }

    public String getTitle(){
        return title;
    }

    public String getType(){
        return type;
    }

    public boolean getEnabled(){
        return enabled;
    }

    public void setTitle(String title){
    	this.title = title;
    }

    public void setType(String type){
    	this.type = type;
    }

    public void setEnabled(boolean enabled){
    	this.enabled = enabled;
    }

}