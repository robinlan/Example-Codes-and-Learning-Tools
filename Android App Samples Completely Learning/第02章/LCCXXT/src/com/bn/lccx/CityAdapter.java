package com.bn.lccx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
public class CityAdapter<T> extends BaseAdapter implements Filterable
{
private List<T> mObjects;//城市名稱  漢字數組
private List<T> mObjects2;//城市名稱  拼音數組
private final Object mLock = new Object();
private int mResource;//展示數組適配器內容的View Id
private int mDropDownResource;//下拉框中內容的Id
private int mFieldId = 0;//下拉框選項ID
private boolean mNotifyOnChange = true;
private Context mContext;//當前上下文對像 - Activity
private ArrayList<T> mOriginalValues;//原始數組列表
private ArrayFilter mFilter;
private LayoutInflater mInflater;
public CityAdapter(Context context, int textViewResourceId, T[] objects,T[] objects2) 
{
    init(context, textViewResourceId, 0, Arrays.asList(objects),Arrays.asList(objects2));
}
public void add(T object) 
{
    if (mOriginalValues != null) 
    {
        synchronized (mLock)
        {
            mOriginalValues.add(object);
            if (mNotifyOnChange) notifyDataSetChanged();
        }
    }
    else 
    {
        mObjects.add(object);
        if (mNotifyOnChange) notifyDataSetChanged();
    }
}
public void insert(T object, int index) 
{
    if (mOriginalValues != null)
    {
        synchronized (mLock) 
        {
            mOriginalValues.add(index, object);
            if (mNotifyOnChange) notifyDataSetChanged();
        }
    }
    else
    {
        mObjects.add(index, object);
        if (mNotifyOnChange) notifyDataSetChanged();
    }
}
public void remove(T object)
{
    if (mOriginalValues != null) 
    {
        synchronized (mLock) 
        {
            mOriginalValues.remove(object);
        }
    }
    else
    {
        mObjects.remove(object);
    }
    if (mNotifyOnChange) notifyDataSetChanged();
}
public void clear() //從列表中刪除所有的信息
{
    if (mOriginalValues != null) 
    {
        synchronized (mLock) 
        {
            mOriginalValues.clear();
        }
    } 
    else
    {
        mObjects.clear();
    }
    if (mNotifyOnChange) notifyDataSetChanged();
}
public void sort(Comparator<? super T> comparator)//根據指定的比較器對適配器中的內容進行排序
{
    Collections.sort(mObjects, comparator);
    if (mNotifyOnChange) notifyDataSetChanged();        
}
@Override
public void notifyDataSetChanged()
{
    super.notifyDataSetChanged();
    mNotifyOnChange = true;
}
//設置自動修改
public void setNotifyOnChange(boolean notifyOnChange)
{
    mNotifyOnChange = notifyOnChange;
}
//構造器  -- 初始化所有信息
private void init(Context context, int resource, int textViewResourceId, List<T> objects ,List<T> objects2) 
{
    mContext = context;
    mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mResource = mDropDownResource = resource;
    mObjects = objects;
    mObjects2 = objects2;
    mFieldId = textViewResourceId;
}
//返回數組適配器相關聯的上下文對像
public Context getContext()
{
    return mContext;
}
public int getCount() //返回  城市名稱漢字  列表的大小
{
    return mObjects.size();
}

public T getItem(int position)//返回城市名稱漢字列表中指定位置的字符串的值
{
    return mObjects.get(position);
}
public int getPosition(T item)//返回 城市名稱漢字 列表中指定的 字符串值 的索引
{
    return mObjects.indexOf(item);
}
public long getItemId(int position)//將int型整數以長整型返回
{
    return position;
}
public View getView(int position, View convertView, ViewGroup parent)//創建View
{
    return createViewFromResource(position, convertView, parent, mResource);
}
private View createViewFromResource(int position, View convertView, ViewGroup parent,//創建View
        int resource)
{
    View view;
    TextView text;
    if (convertView == null) //如果當前為空
    {
        view = mInflater.inflate(resource, parent, false);
    } 
    else //如果不為空
    {
        view = convertView;
    }

    try {
        if (mFieldId == 0) //如果當前域為空,假定所有的資源就是一個TextView
        {
            text = (TextView) view;
        }
        else//否則,在界面中找到TextView
        {
            text = (TextView) view.findViewById(mFieldId);
        }
    } 
    catch (ClassCastException e) //異常處理
    {
        throw new IllegalStateException
        (
           "ArrayAdapter requires the resource ID to be a TextView", e
        );
    }
    text.setText(getItem(position).toString());//為Text設值  -返回當前城市名稱漢字列表中選中的值 
    return view;
}
public void setDropDownViewResource(int resource) //創建下拉視圖
{
    this.mDropDownResource = resource;
}
@Override
public View getDropDownView(int position, View convertView, ViewGroup parent) 
{
    return createViewFromResource(position, convertView, parent, mDropDownResource);
}
public static ArrayAdapter<CharSequence> createFromResource(Context context,//從外部資源中創建新的數組適配器
        int textArrayResId, int textViewResId) 
{
    CharSequence[] strings = context.getResources().getTextArray(textArrayResId);//創建字符創序列
    return new ArrayAdapter<CharSequence>(context, textViewResId, strings);//返回數組適配器
}
public Filter getFilter() //得到過濾器
{
    if (mFilter == null)//如果為空,創建數組過濾器
    {
        mFilter = new ArrayFilter();
    }
    return mFilter;
}
//數組過濾器限制數組適配器以指定的前綴開頭,如果跟提供的前綴不匹配,則將其從中刪除
private class ArrayFilter extends Filter 
{
    @Override
    protected FilterResults performFiltering(CharSequence prefix)//執行過濾
    {
        FilterResults results = new FilterResults();//創建FilterResults對像
        if (mOriginalValues == null) //如果為空
        {
            synchronized (mLock)
            {
                mOriginalValues = new ArrayList<T>(mObjects);
            }
        }
        if (prefix == null || prefix.length() == 0) 
        {
            synchronized (mLock) 
            {
                ArrayList<T> list = new ArrayList<T>(mOriginalValues);
                results.values = list;
                results.count = list.size();
            }
        } 
        else 
        {
            String prefixString = prefix.toString().toLowerCase();//轉換成小寫
            final ArrayList<T> values = mOriginalValues;
            final int count = values.size();
            final ArrayList<T> newValues = new ArrayList<T>(count);
            for (int i = 0; i < count; i++)
            {
                final T value = values.get(i);
                final String valueText = value.toString().toLowerCase();

                final T value2 = mObjects2.get(i);
                final String valueText2 = value2.toString().toLowerCase();
                
                //查找拼音 
                if(valueText2.startsWith(prefixString))
                {
                        newValues.add(value);
                }//查找漢字       
                else if(valueText.startsWith(prefixString))
                {
                        newValues.add(value);
                }
                else
                {      //添加漢字關聯
                        final String[] words = valueText.split(" ");
                        final int wordCount = words.length;
                        for (int k = 0; k < wordCount; k++) 
                        {
	                        if (words[k].startsWith(prefixString)) 
	                        {
	                            newValues.add(value);
	                            break;
	                        }
                        }
                        //添加拼音關聯漢字
                        final String[] words2 = valueText2.split(" ");
                        final int wordCount2 = words2.length;

                    for (int k = 0; k < wordCount2; k++) {
                        if (words2[k].startsWith(prefixString))
                        {
                            newValues.add(value);
                            break;
                        }
                    }  
                }
            }
            results.values = newValues;
            results.count = newValues.size();
        }
        return results;
    }
    @SuppressWarnings("unchecked")
	protected void publishResults(CharSequence constraint, FilterResults results) 
    {    
        mObjects = (List<T>) results.values;
        if (results.count > 0)
        {
            notifyDataSetChanged();
        } else
        {
            notifyDataSetInvalidated();
        }
    }
}
}
