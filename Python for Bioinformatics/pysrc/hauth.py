# hauth.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# Explanation of these routines are found in Chapter 9


from numpy import array, zeros

# Code 9-1
# extract all words of a specific size
def WordExtract( seq, wsize ):
    words = []
    L = len( seq )
    for i in range( L-wsize+1 ):
        words.append( seq[i:i+wsize] )
    words = set( words )
    return list(words)

# Code 9-2
def FindOccurs( seq, word ):
    cnt = seq.count( word )
    locs = []
    k = 0
    for i in range( cnt ):
        k = seq.find( word, k )
        locs.append( k )
        k +=1
    return locs

# Code 9-3
def AllDists( locs ):
    N = len( locs )
    D = []
    for i in range( N ):
        for j in range( i ):
            D.append( locs[i]-locs[j] )
    D = array( D )
    return D

# Code 9-4
def Histogram( upper, D ):
    Nbins = upper + 1
    hist = zeros( Nbins, int )
    for i in range( len( D )):
        k = D[i]
        if 0< k < Nbins:  hist[k] +=1
    return hist

# Code 9-6
def FindRepeats( hist, word, gamma = 10 ):
    mx = hist.max()
    here = hist.argmax()
    if mx < gamma:
        ok = 0
        return ()
    else:
        return (mx,word, here)

# Code 9-7
def MultipleWords( words, seq ):
    NW = len( words )
    hist = zeros( 21, int )
    for i in range( NW ):
        locs = FindOccurs( seq, words[i] )
        D = AllDists( locs )
        h = Histogram( 20, D )
        hist += h
    return hist
