

create table book
(
   ISBN varchar(20) primary key,
   B_Name varchar(50),
   B_Author varchar(50),   
   B_Publishment varchar(80),
   B_BuyTime varchar(50)
);
insert into book values('9787302164289','3ds max 9三維建模','程鳳娟','清華大學出版社','2010-02-01');
insert into book values('9787121060953','photoshop cs3 圖像處理','卓越科技','電子工業出版社','2003-02-01');
insert into book values('9787121102462','Java程序員職場全攻略-從小工到專家','吳亞峰','電子工業出版社','2004-04-05');
insert into book values('9787115227508','Android 2.0遊戲開發實戰寶典','吳亞峰','人民郵電出版社','2003-06-07');
insert into book values('9787030236630','PowerBuilder_10.5實用教程','樊金生','科學出版社','2005-07-05');
insert into book values('9787121079528','PowerBuilder 實用教程（第3版）','鄭阿奇','電子工業出版社','2005-07-09');
insert into book values('9787302244158','大學計算機基礎（21世紀普通高校計算機公共課程規劃教材）','許薇，王東來 ','清華大學出版社','2005-0-09');
insert into book values('9787562324560','微型計算機原理及應用','吳榮光，呂鈞星','華南理工大學出版社','2005-07-09');
insert into book values('9787111187776','算法導論','（美）科曼（Cormen,T.H.） 等著，潘金貴 等譯','機械工業出版社','2005-07-09');



create table bdetailedinformation
(
   B_Num varchar(20) primary key,      
   ISBN varchar(20),     
   Borrowed varchar(50),
   Ordered varchar(50),
   Introduction varchar(1000)
);


insert into bdetailedinformation values('10001','9787302164289','是','否','本書通過通俗易懂的實例解析,從簡單建模到複雜動畫的製作,逐步將讀者引入動畫設計堂');

insert into bdetailedinformation values('10002','9787302164289','是','否','本書通過通俗易懂的實例解析,從簡單建模到複雜動畫的製作,逐步將讀者引入動畫設計堂');

insert into bdetailedinformation values('10003','9787302164289','否','否','本書通過通俗易懂的實例解析,從簡單建模到複雜動畫的製作,逐步將讀者引入動畫設計堂');

insert into bdetailedinformation values('10004','9787302164289','否','否','本書通過通俗易懂的實例解析,從簡單建模到複雜動畫的製作,逐步將讀者引入動畫設計堂');

insert into bdetailedinformation values('10005','9787121060953','否','否','本書內容新穎,版式清晰,語言淺顯易懂,操作舉例豐富,每章以知識講解+實戰演練+上機練習為講解構');

insert into bdetailedinformation values('10006','9787121060953','是','否','本書內容新穎,版式清晰,語言淺顯易懂,操作舉例豐富,每章以知識講解+實戰演練+上機練習為講解結構');

insert into bdetailedinformation values('10007','9787121102462','否','否','本書以包羅萬象的IT大江湖為背景,將Java職場中從入門前的學校菜鳥成長為技術大牛的過程展現給讀者,內容飽滿但又不失趣味性.在本書中既有入職前的行業探秘誤區排除,也有入職後的口訣傳授和江湖新銳兵器介紹');

insert into bdetailedinformation values('10008','9787121102462','是','否','本書以包羅萬象的IT大江湖為背景,將Java職場中從入門前的學校菜鳥成長為技術大牛的過程展現給讀者,內容飽滿但又不失趣味性.在本書中既有入職前的行業探秘、誤區排除,也有入職後的口訣傳授和江湖新銳兵器介紹');


insert into bdetailedinformation values('10009','9787115227508','是','否','本書以Android手機遊戲的開發為主題,結合真實的遊戲案例向讀者詳細介紹了Android平台下遊戲開發的整個流程,同時在遊戲開發的介紹過程中還分享了筆者多年積累的開發技巧與經驗');

insert into bdetailedinformation values('10010','9787115227508','是','否','本書以Android手機遊戲的開發為主題,結合真實的遊戲案例向讀者詳細介紹了Android平台下遊戲開發的整個流程,同時在遊戲開發的介紹過程中還分享了筆者多年積累的開發技巧與經驗');

insert into bdetailedinformation values('10011','9787030236630','是','否','本書系統描述了PowerBuilder 10.5版的組成及其語言,事件和函數,對構成PowerBuilder的各種對像(應用,窗口,菜單,控件,數據窗口,用戶對像)以及它們的創建和使用方法等內容做了詳細講解');

insert into bdetailedinformation values('10012','9787030236630','否','否','本書系統描述了PowerBuilder 10.5版的組成及其語言,事件和函數,對構成PowerBuilder的各種對像(應用,窗口,菜單,控件,數據窗口,用戶對像)以及它們的創建和使用方法等內容做了詳細講解');


insert into bdetailedinformation values('10013','9787030236630','否','否','本書系統描述了PowerBuilder 10.5版的組成及其語言,事件和函數,對構成PowerBuilder的各種對像(應用,窗口,菜單,控件,數據窗口,用戶對像)以及它們的創建和使用方法等內容做了詳細講解');
insert into bdetailedinformation values('10014','9787030236630','否','否','本書系統描述了PowerBuilder 10.5版的組成及其語言,事件和函數,對構成PowerBuilder的各種對像(應用,窗口,菜單,控件,數據窗口,用戶對像)以及它們的創建和使用方法等內容做了詳細講解');




insert into bdetailedinformation values('10015','9787121079528','否','否','本書包含實用教程,習題,上機操作指導和綜合應用實習等4部分.實用教程比較系統地介紹了PowerBuilder 9.0開發環境,PowerScript語言,窗口及窗口控件,創建數據庫,數據窗口對象及控件,用戶自定義對象及事件函數和結構,選單,SQL語句,游標,數據管道,PBL庫管理器等');



insert into bdetailedinformation values('10016','9787121079528','否','否','本書包含實用教程,習題,上機操作指導和綜合應用實習等4部分.實用教程比較系統地介紹了PowerBuilder 9.0開發環境,PowerScript語言,窗口及窗口控件,創建數據庫,數據窗口對象及控件,用戶自定義對象及事件函數和結構,選單,SQL語句,游標,數據管道,PBL庫管理器等');




insert into bdetailedinformation values('10017','9787121079528','否','否','本書包含實用教程,習題,上機操作指導和綜合應用實習等4部分.實用教程比較系統地介紹了PowerBuilder 9.0開發環境,PowerScript語言,窗口及窗口控件,創建數據庫,數據窗口對象及控件,用戶自定義對象及事件函數和結構,選單,SQL語句,游標,數據管道,PBL庫管理器等');



insert into bdetailedinformation values('10018','9787121079528','否','否','本書包含實用教程,習題,上機操作指導和綜合應用實習等4部分.實用教程比較系統地介紹了PowerBuilder 9.0開發環境,PowerScript語言,窗口及窗口控件,創建數據庫,數據窗口對象及控件,用戶自定義對象及事件函數和結構,選單,SQL語句,游標,數據管道,PBL庫管理器等');



insert into bdetailedinformation values('10019','9787302244158','否','否','本書既注重基本原理和方法的闡述,又注重實踐能力的培養,以理論與實踐相結合的方式培養學生的應用能力.');


insert into bdetailedinformation values('10020','9787302244158','是','否','本書既注重基本原理和方法的闡述,又注重實踐能力的培養,以理論與實踐相結合的方式培養學生的應用能力.');


insert into bdetailedinformation values('10021','9787302244158','是','否','本書既注重基本原理和方法的闡述,又注重實踐能力的培養,以理論與實踐相結合的方式培養學生的應用能力.');

insert into bdetailedinformation values('10022','9787562324560','否','否','本書全面深入地介紹了微型計算機的基本組成,工作原理和實際應用.全書共10章,循序漸進地介紹了微型計算機的基本知識,從8086到19entium 4微處理器的內部結構和特點,尋址方式,指令系統及彙編語言程序設計,半導體存儲器,8086中斷系統,輸入與輸出接口技術,MSC-51單片機的功能及其擴展方法');


insert into bdetailedinformation values('10023','9787562324560','否','否','本書全面深入地介紹了微型計算機的基本組成,工作原理和實際應用.全書共10章,循序漸進地介紹了微型計算機的基本知識,從8086到19entium 4微處理器的內部結構和特點,尋址方式,指令系統及彙編語言程序設計,半導體存儲器,8086中斷系統,輸入與輸出接口技術,MSC-51單片機的功能及其擴展方法');


insert into bdetailedinformation values('10024','9787111187776','是','否','本書深入討論各類算法,並著力使這些算法的設計和分析能為各個層次的讀者接受.各章自成體系,可以作為獨立的學習單元。算法以英語和偽代碼的形式描述,具備初步程序設計經驗的人就能看懂.說明和解釋力求淺顯易懂,不失深度和數學嚴謹性');


insert into bdetailedinformation values('10025','9787111187776','是','否','本書深入討論各類算法,並著力使這些算法的設計和分析能為各個層次的讀者接受.各章自成體系,可以作為獨立的學習單元。算法以英語和偽代碼的形式描述,具備初步程序設計經驗的人就能看懂.說明和解釋力求淺顯易懂,不失深度和數學嚴謹性');

insert into bdetailedinformation values('10026','9787111187776','是','否','本書深入討論各類算法,並著力使這些算法的設計和分析能為各個層次的讀者接受.各章自成體系,可以作為獨立的學習單元。算法以英語和偽代碼的形式描述,具備初步程序設計經驗的人就能看懂.說明和解釋力求淺顯易懂,不失深度和數學嚴謹性');



create table student
(
  S_Num varchar(20) primary key,
  S_Name varchar(50),
  S_Age varchar(10),
  S_Sex varchar(50),
  S_Class varchar(50),
  S_Department varchar(50),
  S_Phone varchar(11),
  S_Permitted varchar(50),
  S_Pwd varchar(20)
);

insert into student values('10001','李亞','20','女','計算機1班','計算機系','15176536034','是','001');
insert into student values('10002','王飛',21,'女','計算機1班','計算機系','13730220123','是','002');
insert into student values('10003','孫好',20,'男','計算機1班','計算機系','13633654578','是','003');
insert into student values('10004','何光',22,'男','計算機1班','計算機系','2578975','是','004');
insert into student values('10005','唐心',21,'女','計算機1班','計算機系','13936968956','是','005');
insert into student values('10006','宋理光',20,'男','計算機2班','計算機系','1234667','是','006');



create table record
(
   B_Num varchar(50) primary key,
   S_Num varchar(20),
   BorrowTime varchar(50),
   ReturnTime varchar(50),
   Borrowed varchar(50),
   Ordered varchar(50)
);

insert into record values('10001','10001','2010-1-2','2010-3-2','是','否');
insert into record values('10002','10001','2010-1-23','2010-3-23','是','否');
insert into record values('10006','10001','2010-1-2','2010-3-2','是','否');
insert into record values('10008','10002','2010-1-2','2010-3-2','是','否');
insert into record values('10009','10001','2010-1-2','2010-3-2','是','否');

insert into record values('10010','10002','2010-1-2','2010-3-2','是','否');
insert into record values('10011','10001','2010-2-2','2010-4-2','是','否');
insert into record values('10020','10002','2010-1-2','2010-3-2','是','否');
insert into record values('10021','10001','2010-2-2','2010-4-2','是','否');
insert into record values('10025','10002','2010-1-2','2010-3-2','是','否');
insert into record values('10024','10002','2010-1-2','2010-3-2','是','否');
insert into record values('10026','10002','2010-1-2','2010-3-2','是','否');




create table orderbook
(
   B_Num varchar(50) primary key,
   S_Name varchar(50),
   S_Class varchar(50),
   B_Name varchar(50),
   S_Num varchar(20),
   B_Author varchar(50)
);

create table losebook
(
   GSBH int primary key,
   B_Num varchar(50), 
   B_Name varchar(50),
   S_Num varchar(20)
);



create table manager
(
   M_Num varchar(20) primary key,
   M_Permitted varchar(50),
   M_Pwd varchar(50)
);
insert into manager values('456','高級','123');



create table overtime
(
   S_Num varchar(20),
   B_Num varchar(20),
   B_Name varchar(50),
   overtime int(20),
   primary key(S_Num,B_Num)
);


insert into overtime values('10002','10020','大學計算機基礎（21世紀普通高校計算機公共課程規劃教材）',null);
insert into overtime values('10002','10024','算法導論',null);
insert into overtime values('10002','10025','算法導論',null);





