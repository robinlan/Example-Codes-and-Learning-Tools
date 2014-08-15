package com.bn.lccx;
public class CreatTable {
	public static void creattable(){		
		try{
			String sqll[]=new String[]{					
					
					"create table if not exists train " +//建立train表
					"(Tid integer primary key,Tname char(20)," +
					"Tstartstation char(20),Tterminus char(20),Ttype char(20))",					
					"create  table if not exists station(Sid integer primary key," +
					"Sname char(20),Spy char(10))",//建立station表
					"create table if not exists relation" +
					"(Rid integer primary key,Tid integer,Sid integer,Rarrivetime " +
					"char(20),Rstarttime char(20))",//建立relation表

					//插入一些初始化數據
					"insert into train values(11006,'4419','北京','唐山','空調特快')",
					"insert into train values(10000,'1712','包頭','瀋陽','空調普快')",
					"insert into train values(10001,'K39','北京','齊齊哈爾','空調快速')",		
					
					"insert into station values(1,'包頭','bt')",
					"insert into station values(2,'包頭東','btd')",
					"insert into station values(3,'察素齊','csq')",
					"insert into station values(4,'呼和浩特','hhht')",
					"insert into station values(5,'呼和浩特東','hhhtd')",
					"insert into station values(6,'旗下營','qxy')",
					"insert into station values(7,'集寧南','jnn')",
					"insert into station values(8,'土貴烏拉','tgwl')",
					"insert into station values(9,'豐鎮','fz')",
					"insert into station values(10,'大同','dt')",
					"insert into station values(11,'張家口南','zjkn')",
					"insert into station values(12,'沙城','sc')",					
					"insert into station values(13,'北京','bj')",					
					"insert into station values(14,'玉田縣','ytx')",					
					"insert into station values(15,'唐山','tsb')",					
					"insert into station values(16,'灤縣','lx')",
					"insert into station values(17,'昌黎','cm')",
					"insert into station values(18,'高牌店','gpd')",
					"insert into station values(19,'龍家營','ljy')",
					"insert into station values(20,'山海關','shg')",
					"insert into station values(21,'綏中','sz')",
					"insert into station values(22,'葫蘆島','hld')",
					"insert into station values(23,'錦州','hg')",
					"insert into station values(24,'溝幫子','gbz')",
					"insert into station values(25,'大虎山','dfs')",
					"insert into station values(26,'瀋陽','sy')",
					"insert into station values(27,'天津','tj')",
					"insert into station values(28,'齊齊哈爾','qqh')",
				
					"insert into relation values(1,10000,1,'','15:31')",
					"insert into relation values(2,10000,2,'15:49','15:53')",
					"insert into relation values(3,10000,3,'16:58','17:00')",
					"insert into relation values(4,10000,4,'17:42','17:52')",
					"insert into relation values(5,10000,5,'18:02','18:06')",
					"insert into relation values(6,10000,6,'18:41','18:43')",
					"insert into relation values(7,10000,7,'20:19','20:25')",
					"insert into relation values(8,10000,8,'20:53','20:55')",
					"insert into relation values(9,10000,9,'21:38','21:40')",
					"insert into relation values(10,10000,10,'22:26','22:34')",
					"insert into relation values(11,10000,11,'00:57','01:05')",
					"insert into relation values(12,10000,12,'02:08','02:10')",
					"insert into relation values(13,10000,13,'04:25','04:47')",
					"insert into relation values(14,10000,14,'05:58','06:10')",
					"insert into relation values(15,10000,15,'06:36','06:43')",
					"insert into relation values(16,10000,16,'07:18','07:27')",
					"insert into relation values(17,10000,17,'07:53','07:56')",
					"insert into relation values(18,10000,18,'08:44','08:49')",
					"insert into relation values(19,10000,19,'09:01','09:09')",
					"insert into relation values(20,10000,20,'09:51','09:54')",
					"insert into relation values(21,10000,21,'10:23','10:27')",
					"insert into relation values(22,10000,22,'10:44','10:48')",
					"insert into relation values(23,10000,23,'11:30','11:35')",
					"insert into relation values(24,10000,24,'12:14','12:17')",
					"insert into relation values(25,10000,25,'12:44','12:47')",
					"insert into relation values(26,10000,26,'15:17','')",					
					"insert into relation values(29,10001,13,'','23:00')",
					"insert into relation values(233,10001,15,'01:58','02:00')",
					"insert into relation values(3001,11006,13,'','11:01')",					
					"insert into relation values(3002,11006,15,'14:56','')",
					"insert into relation values(3003,11006,27,'13:12','13:18')",					
			};			
			for(String o:sqll){//循環所有SQL語句，進行建表和初始化一些數據操作
				LoadUtil.createTable(o);
			}		
		}catch(Exception e){		
			e.printStackTrace();			
		}}}

