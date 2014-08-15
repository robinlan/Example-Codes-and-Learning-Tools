package com.bn.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import android.os.Environment;
import android.widget.Toast;

public class FileUtils{
	private String SDPATH;//用於存SD卡的文件的路徑
	private ReaderActivity ra;
	//獲取路徑
	public FileUtils(ReaderActivity ra) {
		this.ra=ra;
		SDPATH=Environment.getExternalStorageDirectory()+"/";//獲得當前外部存儲設備的目錄(根據版本不同路徑不同)
	}
	//創建文件
	public File createSDFile(String fileName) throws IOException{
		File file=new File(SDPATH+fileName);
		file.createNewFile();
		return file;
	}
	//創建文件目錄
	public File createSDDir(String dirName){
		File dir=new File(SDPATH+dirName);
		dir.mkdir();
		return dir;
	}
	//判斷文件是否存在
	public boolean isFileExist(String fileName){
		File file=new File(SDPATH+fileName);
		return file.exists();
	}
	// 將inputStream中文件寫入SD卡 
	//path 子目錄名  fileName 保存文件名 inputStream 通過URL獲取的輸入流
	public File writeToSDFromInput(String path,String fileName,InputStream inputStream){
		File file=null;
		OutputStream output=null;
		try {
			file=createSDFile(path+fileName);//創建目錄和文件
			output=new FileOutputStream(file);//創建輸出流 
			byte buffer[]=new byte[4*1024];//一次讀取文件的長度
			if((inputStream.read(buffer))!=-1){
				output.write(buffer);
			}else{
				Toast.makeText
				(
						ra, 
						"已經存在該文件無需下載"
						,Toast.LENGTH_SHORT 
				).show();
			}
			output.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				output.close();//關閉輸出流
				Toast.makeText
				(
						ra, 
						"下載完成"
						,Toast.LENGTH_SHORT 
				).show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
  
		return file;
	}
	public InputStream getInputStreamFromUrl(String urlStr) throws IOException{//獲取輸出流
		 URL url;
		 url=new URL(urlStr);
		 URLConnection urlCon=url.openConnection();
		 InputStream inputStream=urlCon.getInputStream();
		 return inputStream;
  }

}

