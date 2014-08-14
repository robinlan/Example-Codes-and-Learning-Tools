package com.kujirahand.jsWaffle.utils;

import com.kujirahand.jsWaffle.WaffleActivity;

import android.R;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

public class DialogHelper {
	public static Context getFocusView() {
		WaffleActivity wa = WaffleActivity.mainInstance;
		try {
			wa.getWindow().makeActive();
			View v = wa.getCurrentFocus();
			if (v == null) {
				return wa;
			}
			return v.getContext();
		} catch (Exception e) {
			wa.log_error("getFocusView: " + e.getMessage());
			return WaffleActivity.mainInstance;
		}
	}
	
	public static boolean inputDialog(String title, String message, String defaultValue, final JsPromptResult result) {
		final LinearLayout layout = new LinearLayout(getFocusView());
		final EditText edtInput = new EditText(getFocusView());
		final TextView txtView = new TextView(getFocusView());
		txtView.setPadding(10,10,10,10);
		
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(txtView);
		layout.addView(edtInput);
		
		txtView.setText(message);
		edtInput.setText(defaultValue);
		
		new AlertDialog.Builder(getFocusView())
            .setTitle("Prompt")
            .setView(layout)
            .setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            String value = edtInput.getText().toString();
                            result.confirm(value);
                        }
                    })
            .setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            result.cancel();
                        }
                    })
            .setOnCancelListener(
                    new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            result.cancel();
                        }
                    })
            .show();
        
        return true;
	}
	
	public static boolean dialogYesNo(String title, String message, String defaultValue, final JsPromptResult result) {
		new AlertDialog.Builder(getFocusView())
		.setIcon(android.R.drawable.ic_menu_help)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	result.confirm("true");
		    }
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	result.confirm("false");
		    }
		})
        .setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        result.cancel();
                    }
                })
        .setCancelable(true)
		.show();
		return true;
	}

	public static boolean alert(String title, String message, final JsResult result) {
		new AlertDialog.Builder(getFocusView())
		.setTitle("Information")
		.setMessage(message)
		.setPositiveButton(
			android.R.string.ok,
			new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface dialog, int which) {
					result.confirm();
				}
			})
		.setCancelable(true)
        .setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				result.cancel();
			}
		})
		.create()
		.show();
		return true;
	}
	
	public static boolean confirm(String title, String message, final JsResult result) {
	    new AlertDialog.Builder(getFocusView())
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(
        		android.R.string.ok,
        		new DialogInterface.OnClickListener()
        {
			@Override
            public void onClick(DialogInterface dialog, int which)
            {
                result.confirm();
            }
        })
        .setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener()
        {
			@Override
            public void onClick(DialogInterface dialog, int which)
            {
                result.cancel();
            }
        })
        .setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				result.cancel();
			}
		})
        .setCancelable(true)
        .create()
        .show();
        return true;			
	}

	public static boolean selectList(String title, String message, String items, final JsPromptResult result) {
		// items
		final String[] str_items = items.split(";;;");
		// show select list
		new AlertDialog.Builder(getFocusView())
		.setTitle(title)
		.setItems(str_items, new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which) {
				String ans = str_items[which];
				ans = ans.replaceAll("\"", "''");
				result.confirm(ans);
			}
		})
        .setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        result.cancel();
                    }
                })
        .setCancelable(true)
		.show();
		return true;
	}
	
	public static boolean checkboxList(String title, String message, String items, final JsPromptResult result) {
		// items split
		final String[] str_items = items.split(";;;");
		final boolean[] bool_items = new boolean[str_items.length];
		//
		new AlertDialog.Builder(getFocusView())
		.setTitle(title)
		.setMultiChoiceItems(str_items, bool_items, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				bool_items[which] = isChecked;
			}
		})
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String ans = "";
				for (int i = 0; i < bool_items.length; i++) {
					if (bool_items[i]) {
						ans += str_items[i] + ";;;";
					}
				}
				if (ans != "") {
					ans = ans.substring(0, ans.length() - 3);
				}
				ans = ans.replaceAll("\"", "''");
				result.confirm(ans);
			}
		})
		.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				result.confirm("");
			}
		})
        .setOnCancelListener(
            new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface dialog) {
                    result.confirm("");
                }
            })
        .setCancelable(true)
		.show();
		return true;
	}
	
	public static boolean datePickerDialog(String title, String message, String defValue, final JsPromptResult result) {
		String[] a = defValue.split(",");
		int defYear = 2011; // 適当
		int defMon  = 0;
		int defDate = 1;
		try {
			defYear = Integer.valueOf(a[0]);
			defMon  = Integer.valueOf(a[1]); // 1月が0に相当する月番号を指定
			defDate = Integer.valueOf(a[2]);
		}catch (Exception e) {
		}
		DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, 
					int y, 
					int m,
					int d) {
				String ans = y + "," + m + "," + d;
				result.confirm(ans);
			}
		};
		DatePickerDialog d = new DatePickerDialog(getFocusView(), mDateSetListener, 
				defYear, defMon, defDate);
		d.setCancelable(true);
		d.setOnCancelListener(
	            new DialogInterface.OnCancelListener() {
	                public void onCancel(DialogInterface dialog) {
	                    result.confirm("");
	                }
	            });
		d.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
                result.confirm("");
			}
		});
		d.show();
		return true;
	}
	
	public static boolean timePickerDialog(String title, String message, String defValue, final JsPromptResult result) {
		String[] a = defValue.split(":");
		int defHour = 0;
		int defMin  = 0;
		try {
			defHour = Integer.valueOf(a[0]);
			defMin  = Integer.valueOf(a[1]);
		} catch(Exception e) {
		}
		TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				result.confirm(String.format("%02d:%02d", hourOfDay, minute));
			}
		};
		TimePickerDialog d = new TimePickerDialog(getFocusView(), mTimeSetListener, defHour, defMin, true);
		d.setCancelable(true);
		d.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
                result.confirm("");
			}
		});
		d.setOnCancelListener(
            new DialogInterface.OnCancelListener() {
    			@Override
                public void onCancel(DialogInterface dialog) {
                    result.confirm("");
                }
            });
		d.show();
		return true;
	}
	
	public static boolean seekbarDialog(String title, String message, String defaultValue, final JsPromptResult result) {
		final LinearLayout layout = new LinearLayout(getFocusView());
		final SeekBar bar = new SeekBar(getFocusView());
		bar.setPadding(10, 10, 10, 10);
		
		String[] a = defaultValue.split(","); // min, max, default
		final int min = Integer.valueOf(a[0]);
		int max = Integer.valueOf(a[1]);
		int val = Integer.valueOf(a[2]);
		// min:max = 0:(max-min)
		bar.setMax(max-min);
		bar.setProgress(val-min);
		
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(bar);
		
		new AlertDialog.Builder(getFocusView())
            .setTitle(title)
            .setView(layout)
            .setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	int v = bar.getProgress() + min;
                            result.confirm("" + v);
                        }
                    })
            .setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            result.cancel();
                        }
                    })
            .setOnCancelListener(
                    new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            result.cancel();
                        }
                    })
            .show();
        
        return true;
	}
	
}
