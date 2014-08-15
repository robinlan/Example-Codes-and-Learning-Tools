package com.bn.reader;
import java.io.Serializable;

public class BookMark implements Serializable
{
	private static final long serialVersionUID = -6494236495731556416L;
	String bmname;
	int page;
	
	BookMark(String bmname,int page)
	{
		this.bmname=bmname;
		this.page=page;
	}
}
