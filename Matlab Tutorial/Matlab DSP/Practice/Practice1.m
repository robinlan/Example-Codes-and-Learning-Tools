## Copyright (C) 2014 younder
## 
## This program is free software; you can redistribute it and/or modify
## it under the terms of the GNU General Public License as published by
## the Free Software Foundation; either version 3 of the License, or
## (at your option) any later version.
## 
## This program is distributed in the hope that it will be useful,
## but WITHOUT ANY WARRANTY; without even the implied warranty of
## MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
## GNU General Public License for more details.
## 
## You should have received a copy of the GNU General Public License
## along with Octave; see the file COPYING.  If not, see
## <http://www.gnu.org/licenses/>.

## Practice1

## Author: younder <younder@YOUNDER-PC>
## Created: 2014-02-20

%例1.5 產生系列序列，并繪出离散圖。
%（1）x1(n)=3δ(n–2) – δ(n+4) –5?n?5
%（2）x2(n)=n[u(n)–u(n–5)]+10e–0.2(n–5)[u(n–10)–u(n–20)] 0?n?20
%（3）x3(n)=cos(0.04πn)+0.2w(n) 0?n?50 ,其中w(n)是均值為0，方差為1 的白噪聲序列。
%（4）x4(n)= x(n) = [x1, x1, x1, x1] ,其中x1=[1 0 1 2 3]

function [ ret ] = Practice1 ()
n=[-5:5];x1=3?impseq(2,-5,5)-impseq(-4,-5,5);
subplot(2,2,1);stem(n,x1);xlabel('n'); ylabel('x1(n)');axis([-5,5,-2,4]);
n=[0:20]; x21=n.?(stepseq(0,0,20)-stepseq(10,0,20));
x22=10?exp(-0.2?(n-5)).? (stepseq(10,0,20)-stepseq(20,0,20));x2=x21+x22;
subplot(2,2,2);stem(n,x2);xlabel('n'); ylabel('x2(n)');axis([0,20,-1,11])
n=[0:50];x3=cos(0.04?pi?n)+0.2?randn(size(n));
subplot(2,2,3);stem(n,x3);xlabel('n'); ylabel('x3(n)');axis([0,50,-2,2])
n=[-10:9];x4=[1,0,1,2,3];xTilde=x4'?ones(1,4);xtilde=(xtilde(:))';
subplot(2,2,4);stem(n,xtilde); xlabel('n');ylabel('x4(n)');axis([-10,9,
-1,4])
endfunction
