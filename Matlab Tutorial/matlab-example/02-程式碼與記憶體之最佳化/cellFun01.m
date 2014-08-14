file='english.wpa';
[word, pa]=textread(file, '%s %s');

tic;
len1=zeros(length(word),1);
for i=1:length(word)
	len1(i)=length(word{i});
end
time1=toc;

tic;
len2=cellfun(@length, word);
time2=toc;

fprintf('time1=%g sec, time2=%g sec, time1/time2=%g\n', time1, time2, time1/time2);