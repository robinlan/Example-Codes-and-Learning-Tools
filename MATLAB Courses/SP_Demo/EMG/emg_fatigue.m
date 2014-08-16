% This program was written by Hsiao-Lung chan Ph.D. 2011
% The used data was provided by Ya-Ju Chang, Ph.D. 2011

clear all               % clears all active variables
close all

emg = load('Pre_fatigue.txt');
fs = 5000;           % sampling rate for emg

[slen,chNo] = size(emg);
t=(1:slen)/fs; 
maxv=max(emg(:));
minv=min(emg(:));

clf
set(gcf,'color','w')  % set white background
for n=1:3
  subplot(3,3,3*n-2)
  plot(t, emg(:,n))
  axis([min(t) max(t) minv*1.1 maxv*1.1])
  ylabel('Amplitude, uV');
  if n==1
    title('Rectus femoris')
  elseif n==2
    title('Vastus medialis')
  else   
    title('Vastus lateralis')
  end    
end 
xlabel('Time, s')

index=2*fs+1:10*fs;  
NFFT=2048;
for n=1:3
 subplot(3,3,3*n-1)
 F=0:fs/NFFT:500;
 [Y,F,T,P] = spectrogram(emg(index,n),NFFT,NFFT/2,F,fs);
 T=T+index(1)/fs;
 mesh(F,T,10*log10(abs(P')));
 view(30,45);
 axis tight
 ylabel('Time, s');
 xlabel('Frequency, Hz');
end

for n=1:3
 subplot(3,3,3*n)
 F=0:fs/NFFT:500;
 [Y,F,T,P] = spectrogram(emg(index,n),NFFT,NFFT/2,F,fs);
 T=T+index(1)/fs;
 surf(T,F,10*log10(abs(P)),'EdgeColor','none');
 axis xy; axis tight; colormap(jet); view(0,90);
 xlabel('Time, s');
 ylabel('Frequency, Hz');
end
