    function x=oledbquery(cn,sql)
% [x]=oledbquery(cn,sql)
%
% oledbquery    Executes the sql statement against the connection cn
%
% Inputs:
%   cn,     open connection to OLEDB ActiveX Data Source Control
%   sql,    SQL statement to be executed
%
% Output
%   x,      cell array of query results
%
% Notes: Convert cells to strings using char. Convert cells to numeric
% data using cell2mat() for ints or double(cell2mat()) for floats
%
% Tim Myers
% myers@metronaviation.com
% March 2005

%open recordset and run query
r = cn.connection.invoke('execute', sql);

%retrieve data from recordset
if r.state && r.recordcount>0
    x=invoke(r,'getrows');
    x=x';
else
    x=[];
end

%release recordset
invoke(r,'release');