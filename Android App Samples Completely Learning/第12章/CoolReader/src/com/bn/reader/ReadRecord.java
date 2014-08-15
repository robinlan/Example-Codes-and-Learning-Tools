package com.bn.reader;

import java.io.Serializable;

public class ReadRecord implements Serializable
{
	private static final long serialVersionUID = 1629550672724754997L;
	int leftStart;
	int rightStart;
	int pageNo;//从0开始数
	boolean isLeft=true;
   
	public ReadRecord(int LeftStart,int rightStart,int pageNo)
	{
		this.leftStart=LeftStart;
		this.rightStart=rightStart;
		this.pageNo=pageNo;
	}
}
