# numseq.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See chapter 23 for explanation of codes

from numpy import zeros, nonzero, greater_equal, array, log2,\
     arange, sqrt, random, log
import akando

# Code 23-3
# convert a sequence to GC content
def GCContent( seq, win ):
    # win is the window size
    N = len( seq )
    D = N - win # number of windows
    vec = zeros( D, float )
    for i in range( D ):
        a = seq[i:i+win].count( 'A' )
        c = seq[i:i+win].count( 'C' )
        g = seq[i:i+win].count( 'G' )
        t = seq[i:i+win].count( 'T' )
        vec[i] = float(c+g)/(a+c+g+t)
    return vec

# Code 23-4
def FastGCContent( seq, win ):
    # win is the window size
    N = len( seq )
    D = N - win # number of windows
    vec = zeros( D, float )
    # first window
    a = seq[:win].count( 'A' )
    c = seq[:win].count( 'C' )
    g = seq[:win].count( 'G' )
    t = seq[:win].count( 'T' )
    nmr = float( c+g)
    vec[0] = nmr/win
    w1 = win-1
    # subsequent windows
    for i in range( 1, D ):
        if seq[i-1]=='C' or seq[i-1]=='G':
            nmr -=1
        if seq[i+w1]=='C' or seq[i+w1]=='G':
            nmr += 1
        vec[i] = nmr/win
    return vec

# Code 23-5
# Chang's method
def Chang( seq ):
    a = [1+1j, 1-1j, -1-1j, -1+1j]
    abet = 'ATCG'
    N = len( seq )
    vec = zeros( N-2, complex )
    for i in range( N-2):
        n1, n2, n3 = abet.find(seq[i+2]), abet.find(seq[i+1]), abet.find(seq[i])
        vec[i] = a[n1] + 0.5*a[n2] + 0.25*a[n3]
    return vec

# Code 23-6
# finding partial matches
def PartMatch( targ, seq, nhits ):
    # targ = target sequence
    # seq = data sequence
    # nhits = minimum number of matches needed.
    D = len( seq )
    query = zeros( (4,D) )
    data = zeros( (4,D) )
    # convert targ
    a = array( list( targ ))
    lt = len( targ )
    query[0,:lt] = (a=='A').astype(int)
    query[1,:lt] = (a=='C').astype(int)
    query[2,:lt] = (a=='G').astype(int)
    query[3,:lt] = (a=='T').astype(int)
    # convert sequence
    a = array( list( seq ))
    data[0] = (a=='A').astype(int)
    data[1] = (a=='C').astype(int)
    data[2] = (a=='G').astype(int)
    data[3] = (a=='T').astype(int)
    # correlate
    corr = zeros( (4,D) )
    for i in range( 4 ):
        a = akando.Correlate( data[i], query[i] )
        corr[i] = akando.Swap(a.real)
    corr = corr.sum(0)
    # find the peaks
    hits = greater_equal( corr, nhits*0.9999 ).astype(int)
    nz = nonzero( hits )[0]
    if D%2==0: return nz
    else: return nz+1


#############  Hurst
# Code 23-8
def TwoEncode( seq ):
    N = len( seq )
    vec = zeros( N, int )
    a = (-2, -1, 1, 2 )
    b = 'ACGT'
    for i in range( N ):
        ndx = b.find( seq[i] )
        vec[i] = a[ndx]
    return vec

# Code 23-9
def Hurst( seq ):
    # n- averages
    N = len( seq )
    x = TwoEncode( seq )
    R = zeros( N, float )
    S = zeros( N, float )
    for n in range( 1,N ):
        avg = x[:n].mean()
        X = zeros( n, float )
        for t in range( 1, n ):
            X[t] = X[t-1] + (x[t-1] - avg )
        R[n] = X.max() - X.min()
        S[n] = sqrt( 1./n * ((x[:n]-avg)**2).sum())
    Z = R[3:]/S[3:]
    M = zeros( (N-3,2), float )
    M[:,0] = log(arange( 3,N))
    M[:,1] = log(Z)
    H = akando.linearRegression( M[:,0], M[:,1] )
    return M, H[0]
