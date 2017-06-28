package com.wua.mc.webuntisapp.presenter;

public class Filter {
// Ray: modification
	/*
	added some parameters in the construction to enable the creation of a filter .
	 */
private String id;
	private String name;
	private String longName;

	public Filter(String id, String name, String longName) {
		this.setId(id);
		this.setName(name);
		this.setLongName(longName);

	}



	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLongName(String longName) {
		this.longName = longName;
	}


	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getLongName() {
		return longName;
	}
	@Override
	public String toString(){
		return ""+this.getLongName()+"-"+this.getName();
	}



}