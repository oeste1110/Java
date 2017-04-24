package com.potevio.dao.bean;

public class User {
	private int Id;
	private String Name;
	private String Password;
	
	public void setId(int id)
	{
		this.Id = id;
	}
	
	public int getId()
	{
		return Id;
	}
	
	public void setName(String name)
	{
		this.Name = name;
	}
	
	public String getName()
	{
		return Name;
	}
	
	public void setPassword(String password)
	{
		this.Password = password;
	}
	
	public String getPassword()
	{
		return Password;
	}
	
}
