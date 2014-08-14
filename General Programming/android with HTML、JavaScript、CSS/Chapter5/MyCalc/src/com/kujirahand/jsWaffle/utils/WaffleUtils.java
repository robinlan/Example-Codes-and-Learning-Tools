package com.kujirahand.jsWaffle.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import com.kujirahand.jsWaffle.WaffleActivity;

import android.app.Activity;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;

public class WaffleUtils {
	
	private static int BUFFSIZE = 1024 * 8;
	public static int http_timeout = 5000;
	
	/**
	 * copy assets to external file 
	 * @param app
	 * @param assetsName
	 * @param savepath
	 * @return
	 */
	public static boolean copyAssetsFile(Activity app, String assetsName, String savepath) {
		FileOutputStream fo;
		InputStream inp;
		try {
			File f = new File(savepath);
			File parent = f.getParentFile();
			if (parent != null) { parent.mkdirs(); }
			fo = new FileOutputStream(f);
		} catch (IOException e) {
			Log.e(WaffleActivity.LOG_TAG, "copyAssetsFile() savepath could not open:" + e.getMessage() + ",file=" + savepath);
			return false;
		}
		AssetManager am = app.getResources().getAssets();
		try {
			byte[] buf = new byte[BUFFSIZE];
			inp = am.open(assetsName);
			while (true) {
				int sz = inp.read(buf, 0, BUFFSIZE);
				if (sz <= 0) break;
				fo.write(buf, 0, sz);
			}
			fo.close();
			return true;
		}catch(IOException e) {
			Log.e(WaffleActivity.LOG_TAG, e.getMessage());
			return false;
		}
	}
	
	public static boolean mergeSeparatedAssetsFile(Activity app, String assetsName, String savepath) {
		/*
		 * In assets dir,
		 *   input  : name.1 name.2 name.3
		 *   output : savepath
		 */
		
		FileOutputStream fo;
		InputStream inp;
		try {
			File f = new File(savepath);
			File parent = f.getParentFile();
			if (parent != null) { 
				parent.mkdirs();
			}
			fo = new FileOutputStream(f);
		} catch (IOException e) {
			Log.e(WaffleActivity.LOG_TAG, "mergeSeparatedAssetsFile() savepath could not open:" + e.getMessage() + ",file=" + savepath);
			return false;
		}
		AssetManager am = app.getResources().getAssets();
		try {
			byte[] buf = new byte[BUFFSIZE];
			
			for (int no = 1; no < 999; no++) {
				String fname = assetsName + "." + no;
				try {
					inp = am.open(fname);
					if (inp == null) break;
				} catch (IOException e) {
					break;
				}
				while (true) {
					int sz = inp.read(buf, 0, BUFFSIZE);
					if (sz <= 0) break;
					fo.write(buf, 0, sz);
				}
			}
			
			fo.close();
			return true;
		}catch(IOException e) {
			Log.e(WaffleActivity.LOG_TAG, e.getMessage());
			return false;
		}
	}
	
	
	public static Uri checkFileUri(String filename) {
		Uri uri = Uri.parse(filename);
		String scheme = uri.getScheme();
		String path = uri.getPath();
		if (path.startsWith("/sdcard/")||path.startsWith("/data/")) {
			uri = Uri.parse("file://" + filename);
		}
		if ((scheme == null)&&(path.startsWith("www/") || path.startsWith("/www/"))) {
			if (path.startsWith("/www")) {
				path = path.substring(1);
			}
			uri = Uri.parse("file:///android_asset/" + path);
		}
		/*
		if (path.startsWith("/android_asset/")) {
			// android_assets は特殊なので別途処理すべき
		}
		*/
		return uri;
	}
	
	public static File detectFile(String filename, Activity app) {
		File result = null;
		try {
			Uri uri = checkFileUri(filename);
			String scheme = uri.getScheme();
			String path = uri.getPath();
			
			if (scheme == null) {
				result = app.getFileStreamPath(path);
			}
			else if (scheme.equals("file")) {
				result = new File(uri.getPath());
			}
			else {
				WaffleActivity.mainInstance.log_warn("Unknown scheme : " + filename);
				// error
			}
		} catch (Exception e) {
		}
		return result;
	}
	
	public static FileOutputStream getOutputStream(String filename, Activity activity) {
		FileOutputStream output = null;
		try {
			File f = detectFile(filename, activity);
			if (f == null) {
				WaffleActivity.mainInstance.log_warn("[FileNotFound]" + filename);
				return null;
			}
			output = new FileOutputStream(f);
		} catch (Exception e) {
		}
		return output;
	}
	
	public static InputStream getInputStream(String filename, Activity activity) {
		FileInputStream input = null;
		try {
			Uri uri = checkFileUri(filename);
			String path = uri.getPath();
			String scheme = uri.getScheme();
			// check assets
			if (path.startsWith("/android_asset/")) {
				path = path.substring(15);
				return activity.getAssets().open(path);
			}
			// check Contents Provider
			if (scheme != null && scheme.equals("content")) {
				return activity.getContentResolver().openInputStream(uri);
			}
			// cehck File Path
			File f = detectFile(filename, activity);
			if (f == null) {
				WaffleActivity.mainInstance.log_warn("[FileNotFound]" + filename);
				return null;
			}
			input = new FileInputStream(f);
		} catch (Exception e) {
		}
		return input;
	}
	
	/**
	 * copy file
	 * @param src
	 * @param des
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void copyFileFromName(String src, String des, Activity app)
		throws FileNotFoundException, IOException {
		InputStream srcFile = getInputStream(src, app);
		if (srcFile == null) {
			throw new FileNotFoundException(src);
		}
		OutputStream desFile = getOutputStream(des, app);
		copyFileStream(srcFile, desFile);
	}
	public static void copyFile(File srcFile, File desFile)
		throws FileNotFoundException, IOException {
		try {
			File parent = desFile.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
		} catch (Exception e) {
		}
		//
		InputStream input = new FileInputStream(srcFile);
		OutputStream output = new FileOutputStream(desFile);
		copyFileStream(input, output);
	}
	public static void copyFileStream(InputStream input, OutputStream output)
		throws IOException {
		byte[] buffer = new byte[BUFFSIZE];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		input.close();
		output.close();
	}
	
	/**
	 * get data from url
	 * @param url
	 * @return get data
	 * @see http://se-suganuma.blogspot.com/2010/02/androidhttpclienthttpgetjson.html
	 */
	public static String httpGet(String url) {
		HttpClient objHttp = new DefaultHttpClient();
		HttpParams params = objHttp.getParams();
		HttpConnectionParams.setConnectionTimeout(params, http_timeout); //接続のタイムアウト
		HttpConnectionParams.setSoTimeout(params, http_timeout); //データ取得のタイムアウト
		String sReturn = "";
	    try {
	    	HttpGet objGet   = new HttpGet(url);
	        HttpResponse objResponse = objHttp.execute(objGet);
	        if (objResponse.getStatusLine().getStatusCode() < 400){
	            InputStream objStream = objResponse.getEntity().getContent();
	            InputStreamReader objReader = new InputStreamReader(objStream);
	            BufferedReader objBuf = new BufferedReader(objReader);
	            StringBuilder objJson = new StringBuilder();
	            String sLine;
	            while((sLine = objBuf.readLine()) != null){
	            	objJson.append(sLine);
	            }
	            sReturn = objJson.toString();
	            objStream.close();
	        }
	    } catch (IOException e) {
	    	httpLastError = e.getMessage();
	    	return null;
	    }	
	    return sReturn;
	}
	public static String httpLastError = null;
	
	/**
	 * http download
	 * @param url
	 * @param filename
	 * @return
	 */
	public static boolean httpDownloadToFile(String url, String filename, Activity app) {
		// 保存先をセット
		FileOutputStream outStream = getOutputStream(filename, app);
		if (outStream == null) return false;
		// ダウンロード
		HttpClient objHttp = new DefaultHttpClient();
		HttpParams params = objHttp.getParams();
		HttpConnectionParams.setConnectionTimeout(params, http_timeout); //接続のタイムアウト
		HttpConnectionParams.setSoTimeout(params, http_timeout); //データ取得のタイムアウト
	    try {
	    	byte[] buf = new byte[BUFFSIZE];
	    	HttpGet objGet   = new HttpGet(url);
	        HttpResponse objResponse = objHttp.execute(objGet);
	        if (objResponse.getStatusLine().getStatusCode() < 400){
	            InputStream objStream = objResponse.getEntity().getContent();
	            int retCount = 0;
	            while ((retCount = objStream.read(buf)) > 0) {
	            	outStream.write(buf, 0, retCount);
	            }
	            objStream.close();
		        outStream.close();
	        }
	        return true;
	    } catch (IOException e) {
	    	Log.e(WaffleActivity.LOG_TAG, "httpDownload.failed:" + e.getMessage());
	    	return false;
	    }
	}
	
	/**
	 * post data to url
	 * @param sUrl 送信先URL
	 * @param sJson 文字列に変換したJSONデータ
	 * @return
	 */
	public static String httpPostJSON(String sUrl, String sJson) {
		HttpClient objHttp = new DefaultHttpClient();
		String sReturn = "";
	    try {
	    	HttpPost objPost   = new HttpPost(sUrl);
	    	try {
	    		JSONObject j = new JSONObject(sJson);
	    		List<NameValuePair> pairs = new ArrayList <NameValuePair>();
	    		
	    		@SuppressWarnings("unchecked")
	    		Iterator<String> i = j.keys();
	    		while(i.hasNext()) {
	    			String pname = i.next();
	    			BasicNameValuePair nv = new BasicNameValuePair(pname, j.getString(pname));
	    			pairs.add(nv);
	    		}
	    		objPost.setEntity(new UrlEncodedFormEntity(pairs, "UTF-8"));
	    	} catch (Exception e) {
	    	}
	        HttpResponse objResponse = objHttp.execute(objPost);
	        if (objResponse.getStatusLine().getStatusCode() < 400){
	            InputStream objStream = objResponse.getEntity().getContent();
	            InputStreamReader objReader = new InputStreamReader(objStream);
	            BufferedReader objBuf = new BufferedReader(objReader);
	            StringBuilder objJson = new StringBuilder();
	            String sLine;
	            while((sLine = objBuf.readLine()) != null){
	            	objJson.append(sLine);
	            }
	            sReturn = objJson.toString();
	            objStream.close();
	        }
	    } catch (IOException e) {
	    	return null;
	    }	
	    return sReturn;
	}
}
