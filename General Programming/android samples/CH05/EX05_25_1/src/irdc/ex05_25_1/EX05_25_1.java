package irdc.ex05_25_1;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.gesture.GestureLibrary;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import java.util.ArrayList;
import java.io.File;

public class EX05_25_1 extends Activity
{
  private GestureLibrary lib; 
  private GesturesAdapter mAdapter;
  private TextView mEmpty;
  private File gesFile;
  private ArrayList<String> gesNames=new ArrayList<String>();
  private ArrayList<Bitmap> gesPics=new ArrayList<Bitmap>();
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    /* 載入main.xml Layout */
    setContentView(R.layout.main);
    
    /* 檢視SDCard是否存在 */ 
    if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) 
    { 
      finish(); 
    }
    /* 取得gesture file */
    gesFile=new File(Environment.getExternalStorageDirectory(),
    "gestures");
    /* 物件初始化 */
    mEmpty = (TextView) findViewById(R.id.empty);
    ListView lv=(ListView)findViewById(R.id.myList);   
    /* 讀取gesture */
    loadGestures();
    
    mAdapter = new GesturesAdapter(this,gesNames,gesPics);
    if(mAdapter.getCount()!=0)
    {
      lv.setAdapter(mAdapter);
      mEmpty.setVisibility(EditText.GONE);
    }
    else
    {
      mEmpty.setText(getString(R.string.list_empty));
      mEmpty.setVisibility(EditText.VISIBLE);
    }
  }

  /* 讀取gesture資訊的method */
  private void loadGestures()
  {
    gesNames=new ArrayList<String>();
    gesPics=new ArrayList<Bitmap>();

    try 
    {
      lib = GestureLibraries.fromFile(gesFile); 
       
      if(gesFile.exists())
      {
        /* 檔案存在時先讀取已儲存的Gesture */
        if (!lib.load()) 
        { 
          /* Library讀取失敗 */
          mEmpty.setText(getString(R.string.load_failed));
          mEmpty.setVisibility(EditText.VISIBLE);
        }
        else
        {
          /* 讀取gesture資訊 */
          Object[] en=lib.getGestureEntries().toArray();
          for(int i=0;i<en.length;i++)
          {
            ArrayList<Gesture> al=lib.getGestures(en[i].toString());
            for(int j=0;j<al.size();j++)
            {
              /* 手勢名稱 */
              gesNames.add(en[i].toString());
              Gesture gs=(Gesture)al.get(j);
              /* 將手勢轉成Bitmap圖檔 */
              gesPics.add(gs.toBitmap(64,64,12,Color.GREEN));
            }
          }
        }
      }
    }
    catch(Exception e) 
    { 
      e.printStackTrace(); 
    } 
  }

  /* 自定義GesturesAdapter繼承BaseAdapter */
  private class GesturesAdapter extends BaseAdapter
  {
    private final LayoutInflater mInflater;
    private ArrayList<String> _na=new ArrayList<String>();
    private ArrayList<Bitmap> _pi=new ArrayList<Bitmap>();
    private ViewHolder holder;
      
    public GesturesAdapter(Context context,ArrayList<String> na,
                           ArrayList<Bitmap> pi)
    {
      /* 參數初始化 */
      mInflater = LayoutInflater.from(context);
      _na=na;
      _pi=pi;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent)
    {
      if (convertView == null)
      {
        /* 使用自定義的gestures_item作為Layout */
        convertView = mInflater.inflate(R.layout.gestures_item, null);
        holder = new ViewHolder();
        holder.text = (TextView) convertView.findViewById(R.id.text);
        holder.icon = (ImageView) convertView.findViewById(R.id.icon);
              
        convertView.setTag(holder);
      }
      else
      {
        holder = (ViewHolder) convertView.getTag();
      }

      holder.text.setText(_na.get(position).toString());
      holder.icon.setImageBitmap(_pi.get(position));
      return convertView;
    }

    @Override
    public int getCount()
    {
      return _na.size();
    }

    @Override
    public Object getItem(int arg0)
    {
      return _na.get(arg0);
    }

    @Override
    public long getItemId(int arg0)
    {
      return arg0;
    }
  }
    
  /* class ViewHolder */
  private class ViewHolder
  {
    TextView text;
    ImageView icon;
  }
}
