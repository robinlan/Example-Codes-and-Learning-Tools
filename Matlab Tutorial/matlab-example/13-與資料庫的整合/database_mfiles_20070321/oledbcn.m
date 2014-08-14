function cn=oledbcn(cnstr,cto,rst)
% cn = oledb(cnstr,[cto],[rst])
%
% Connects to OLEDB using the Microsoft ActiveX Data Source Control
%
% Inputs:
%   cnstr,  str containing information for connecting to data source
%   cto,    CommandTimeout in seconds (default=60 seconds if unspecified)
%   rst,    RecordsetType: 1=dscSnapshot (default), 2=dscUpdatableSnapshot
%
% Output:
%   cn,     connection
%
% Notes: Refer to demo_oledb.m and oledbcnstr.m for example connection 
% strings for Oracle, SQL, and MS Access databases
%
% Tim Myers
% myers@metronaviation.com
% March 2005
% Updated January 2006
% Updated August 2006

try
    %create activeX control
    try
        cn=COM.OWC11_DataSourceControl_11;
    catch
        cn=COM.OWC10_DataSourceControl_10;
    end
    
    %Open connection
    cn.ConnectionString=cnstr;

    %Specify connection timeout if provided
    if nargin>1 
        cn.Connection.CommandTimeout=cto; 
    else
        cn.Connection.CommandTimeout=60;    %default
    end

    %Specify connection RecordsetType
    if nargin>2
        cn.RecordsetType=rst; 
    else
        cn.RecordsetType=1;     %dscSnapshot default
    end
catch
    disp('***** OLEDBCN TROUBLESHOOTING *****')
    disp('Could not create connection.')
    disp('Verify your system has access to one of the ')
    disp('following ActiveX controls: ');
    disp('	 COM.OWC10_DataSourceControl_10');
    disp('	 COM.OWC11_DataSourceControl_11');
    disp('To view your available ActiveX controls enter: ');
    disp('   [h, info] = actxcontrolselect');
    disp('Look for the current version of "Microsoft Office Data Source Control" ');
    disp('and verify the "Program ID" matches the one used on line 26 of oledbcn.m');
    disp('If that does not solve the problem, verify the connection string is valid ');
    disp('by searching "universal data link" or "DSN-less connection strings" in Google');
    error('Could not create connection. See troubleshooting above.')
end


