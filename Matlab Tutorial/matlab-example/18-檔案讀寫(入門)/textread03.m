contents = textread('textread03.m','%s','delimiter','\n','whitespace','');
class(contents)		% 印出 contents 的資料類別
contents{1}		% 列出 contents 第一列
contents{2}		% 列出 contents 第二列