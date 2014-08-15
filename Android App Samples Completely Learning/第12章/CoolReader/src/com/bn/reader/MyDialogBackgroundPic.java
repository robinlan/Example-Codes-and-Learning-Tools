package com.bn.reader;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class MyDialogBackgroundPic extends Dialog 
{
	public MyDialogBackgroundPic(Context context) {
        super(context,R.style.FullHeightDialog);
    }
	
	@Override
	public void onCreate (Bundle savedInstanceState) 
	{
		this.setContentView(R.layout.background_picture);
	}
	
	@Override
	public String toString()
	{
		return "MyDialog";
	}
}
