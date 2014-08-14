package mahout.fansy.utils.fpg;

import org.apache.hadoop.io.Writable;

public  class AKVRegex implements IKVRegex{
	
	/**
	 * key 的解析方法
	 * @return key的解析字符串
	 */
	public  String keyRegex(Writable key){
		return key.toString();
	};
	
	/**
	 * value 的解析方法
	 * @return value的解析字符串
	 */
	public String valueRegex(Writable value) {
		return value.toString();
	}
}
