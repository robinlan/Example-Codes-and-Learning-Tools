package com.bn.reader;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class MyDialog extends Dialog 
{
	public MyDialog(Context context) {
        super(context,R.style.FullHeightDialog);
    }
	
	@Override
	public void onCreate (Bundle savedInstanceState) 
	{
		this.setContentView(R.layout.bookmark_input);
	}
	
	@Override
	public String toString()
	{
		return "MyDialog";
	}
}
