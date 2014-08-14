to = {'email1@aaa.bbb.ccc', 'email2@aaa.bbb.ccc'};
to = {'jang@cs.nthu.edu.tw'};
subject = 'Test email from a MATLAB program';
message = ['This is a test email sent via', 10, ' sendmail.'];
attachment = {'c:\windows\matlab.ini'};
sendmail(to, subject, message, attachment);