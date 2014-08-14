package mahout.fansy.utils.fpg;

import org.apache.hadoop.io.Writable;
import org.apache.mahout.fpm.pfpgrowth.convertors.string.TopKStringPatterns;
/**
 * 用于解析fpg算法输出的频繁项目集
 * @author Administrator
 *
 */
public class ImplAKVRegex implements IKVRegex{
	
	/**
	 * value 的解析方法
	 * @return value的解析字符串
	 */
	public String valueRegex(Writable value) {
		TopKStringPatterns val=(TopKStringPatterns) value;
		String temp=val.toString();
		if(val.getPatterns().size()==1){
			return "";
		}else{
			int b=temp.indexOf(")");
			return temp.substring(b+2,temp.length());
		}
	}

	@Override
	public String keyRegex(Writable key) {
		// TODO Auto-generated method stub
		return key.toString();
	}

}
