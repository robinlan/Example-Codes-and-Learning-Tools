MATLAB OLEDB Connection and Query Functions

Tim Myers
myers@metronaviation.com
March 8, 2005	- initial version
August 23, 2006	- added help text in catch section of oledbcn.m when database connection cannot be made
March 21, 2007	- modified oledbquery to support SQL insert and update methods

These Matlab functions open a connection to a variety of data sources (SQL server, Oracle, or MS Access) without reference 
to the available data sources in the ODBC Data Source Administrator and without linking tables through MS Access.  In other words,
you can connect directly to a datasource without modifying your ODBC Data Source Administrator.  
Google "dsn-less connection strings" for help constructing your own connection strings.

The connection is created using the COM.OWC11_DataSourceControl_11 Microsoft Office Data Source Control 11.0 ActiveX Control.
Users have reported this ActiveX Control can be obtained from Microsoft by downloading owc10.exe. 


CONTENTS:

oledbcnstr.m 	builds a connection string based on the database type, server, and other inputs

oledbcn.m	opens the connection using the connection string

oledbquery.m	executes a sql statement against the data source and returns the result set in a cell array

demo_oledb.m	demonstrates how to connect, query, and close a connection




