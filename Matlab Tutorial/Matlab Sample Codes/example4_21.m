clear
load mtlb
specgram(mtlb,512,Fs,kaiser(500,5),475)
title('Spectrogram')
