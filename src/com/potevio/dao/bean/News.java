package com.potevio.dao.bean;

public class News {
	private int Id;
	private String Title;
	private String Content;
	
	public void setId(int id)
	{
		this.Id = id;
	}
	
	public int getId()
	{
		return Id;
	}
	
	public void setTitle(String title)
	{
		this.Title = title;
	}
	
	public String getTitle()
	{
		return Title;
	}
	
	public void setContent(String content)
	{
		this.Content = content;
	}
	
	public String getContent()
	{
		return Content;
	}
	
}
