clear

figure(1)
load eeg1.mat;
fs=500;
t=0:1/fs:1;
NFFT=512;
x = eegs(1,:);
y = eegs(2,:);


[Cxy,f] = mscohere(x,y,hanning(NFFT),NFFT/2,NFFT); 
f=f/pi*fs/2;
subplot(1,1,1)
plot(f,Cxy)
axis([min(f) max(f) 0 1]);

indexDelta=2:5;
indexTheta=6:8;
indexAlpha=9:13;
indexBeta=14:31;
CohDelta=sum(Cxy(indexDelta))/length(indexDelta);
CohTheta=sum(Cxy(indexTheta))/length(indexDelta);
CohAlpha=sum(Cxy(indexAlpha))/length(indexDelta);
CohBeta=sum(Cxy(indexBeta))/length(indexDelta);
