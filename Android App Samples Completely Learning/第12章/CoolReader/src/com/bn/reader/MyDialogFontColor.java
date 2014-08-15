package com.bn.reader;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class MyDialogFontColor extends Dialog 
{
	public MyDialogFontColor(Context context) {
        super(context,R.style.FullHeightDialog);
    }
	
	@Override
	public void onCreate (Bundle savedInstanceState) 
	{
		this.setContentView(R.layout.font_color);
	}
	
	@Override
	public String toString()
	{
		return "MyDialog";
	}
}
