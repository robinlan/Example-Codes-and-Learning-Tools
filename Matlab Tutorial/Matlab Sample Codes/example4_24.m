clear
I = imread('f1.jpg');
BW = rgb2gray(I);
J = imnoise(BW,'salt & pepper',0.02);
K = medfilt2(J);
subplot(121)
imshow(J)
subplot(122)
imshow(K)

