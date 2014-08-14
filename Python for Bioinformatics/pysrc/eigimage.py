# eigimage.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See chapter 19 for explanation of codes

import Image
from numpy import zeros, linalg
import akando

# Code 19-20
def LoadImages( dr, mglist ):
    pics = []
    for i in mglist:
        mg = Image.open( dr +'/' + i )
        pics.append(  akando.i2a( mg )/256. )
    return pics

# Code 19-21
def SubAvg( data ):
    N = len( data )
    avg = zeros( data[0].shape, float )
    for i in data:
        avg += i
    avg /= N
    #
    for i in range( N):
        data[i] -= avg
    return avg, data

# Code 19-22
def Lcov( data ):
    N = len( data ) # number of images
    L = zeros( (N,N), float )
    for i in range( N ):
        L[i,i] = ( data[i]*data[i]).sum()
        for j in range( i ):
            L[i,j] = L[j,i] = ( data[i]*data[j]).sum()
    return L

# Code 19-23
def Eimage( L, data ):
    N = len( data )
    evls, evcs = linalg.eig( L )
    emg = []
    V,H = data[0].shape
    for j in range( N ):
        temp = zeros( (V,H), float )
        for i in range( N ):
            temp +=  evcs[i,j] * data[i]
        emg.append( temp )
    return emg, evls

# Code 19-24
def Coeffs( pca, data ):
    D = len( pca )
    N = len( data )
    cffs = zeros( (N,D), float )
    for i in range( N ):
        for j in range( D ):
            cffs[i,j] = ( pca[j] * data[i]).sum()
    return cffs

def FindClose( cffs, pt ):
    N = len( cffs )
    d = zeros( N , float )
    for i in range( N ):
        d[i] = akando.Distance( cffs[i], array( pt ) )
    ag = argsort( d )
    return ag

def ListBox( cffs, box ):
    w, n, e, s = box
    lb = []
    for i in range(len(cffs)):
        if n>=cffs[i][1]>=s and w<=cffs[i][0]<=e:
            lb.append( i )
    return lb
