function s=oledbcnstr(type,sv,db,uid,pwd)
% s=oledbcnstr(type,[sv],[db],[uid],[pwd])
%
% Returns connection string for OLEDB connection
%
% Inputs:
%   type,   connection type - currently supported: 'Access','SQL','Oracle'
%   sv,     name of server - not required for Access connection
%   db,     name of database - not required for Oracle connection
%   uid,    user id - added to connection string if provided
%   pwd,    password - added to connection string if provided
%
% Output:
%   s,      connection string
%
% Usage:
%   s=oledbcnstr('access',[],'test.mdb')
%   s=oledbcnstr('sql','servername','databasename','userid','password')
%
% Tim Myers
% myers@metronaviation.com
% March 2005


if strcmpi('ACCESS',type)
    %Connect to Access database given by input db
    s=['PROVIDER=MSDASQL;'];
    s=[s 'DRIVER={Microsoft Access Driver (*.mdb)};'];
    s=[s 'DBQ=' db ';'];
    
elseif strcmpi('SQL',type)
    %Connect to SQL Database
    s=['PROVIDER=MSDASQL;DRIVER={SQL Server};'];
    s=[s 'SERVER=' sv ';DATABASE=' db ';'];

elseif strcmpi('ORACLE',type)
    %Connect to Oracle Database
    s=['PROVIDER=MSDASQL;'];
    s=[s 'DRIVER={Microsoft ODBC for Oracle};'];
    s=[s 'SERVER=' sv ';'];
else
    s='Unknown type';
end

%add uid and pwd if provided
if nargin>3 s=[s 'UID=' uid ';']; end
if nargin>4 s=[s 'PWD=' pwd ';']; end