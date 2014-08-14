%demo_oledb
% Demonstrates the OLEDB connection functions
%
% Requires these files:
%   oledbcnstr.m
%   oledbcn.m
%   oledbquery.m
%   test.mdb
%
% Usage:
% Only 4 lines to connect, query, and disconnect:
% 
% s=oledbcnstr(type,[sv],[db],[uid],[pwd]);
% cn = oledb(cnstr,[cto],[rst]);
% [x]=oledbquery(cn,sql);
% invoke(cn,'release')
%
% Tim Myers
% myers@metronaviation.com
% March 2005

disp('Make sure test.mdb is in the current directory')
%Build connection string
%Google "dsn-less connection strings" for help constructing 
%your own connection strings
s=oledbcnstr('Access',[],[cd '\test.mdb']);
%Open connection
cn=oledbcn(s);
%Sample query to execute
sql='select * from TestTable order by lastname, firstname';
%Run query and return results
x=oledbquery(cn,sql)
%Parse result set
lastname=char(x(:,1))
firstname=char(x(:,2))
profession=char(x(:,3))
office=double(cell2mat(x(:,4)))
%Close connection
invoke(cn,'release')
