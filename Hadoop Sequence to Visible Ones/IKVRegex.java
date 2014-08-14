package mahout.fansy.utils.fpg;

import org.apache.hadoop.io.Writable;

/**
 * <key,value>解析方法
 * @author Administrator
 *
 */
public interface IKVRegex {
	
	/**
	 * key 的解析方法
	 * @return key的解析字符串
	 */
	public   String keyRegex(Writable key);
	
	/**
	 * value 的解析方法
	 * @return value的解析字符串
	 */
	public String valueRegex(Writable value);
}
