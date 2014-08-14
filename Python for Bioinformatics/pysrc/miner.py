# miner.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See chapter 15 for explanation of details

import os
from numpy import zeros, greater_equal, nonzero, take, logical_and, argsort, \
     sqrt, greater, dot

# get list of filenames
def GetList( direct ):
    names = os.listdir( direct )
    # get just those that end with txt
    nms = []
    for i in names:
        if '.txt' in i:
            nms.append( i )
    return nms

# Code 15-1
# read and clean
def Hoover( txt ):
    # clean it up
    work = txt.lower()
    work = work.replace( '\n',' ')
    valid = [32] + range( 97,123)
    for i in range( 256 ):
        if i not in valid:
            work = work.replace( chr(i), '' )
    return work

# Code 15-2
# make a dictionary
def AllWordDict( txt ):
    dct = { }
    work = txt.split()
    for i in range( len( work )):
        wd = work[i]
        if wd in dct:
            dct[wd].append( i )
        else:
            dct[wd] = [i]
    return dct

# Code 15-4
# build a dictionary with 5 lettter words
def FiveLetterDict( txt ):
    dct = { }
    work = txt.split()
    for i in range( len( work )):
        wd = work[i]
        if len(wd)>=5:
            wd5 = wd[:5]
            if wd5 in dct:
                dct[wd5].append( i )
            else:
                dct[wd5] = [i]
    return dct

# Code 15-6
def GoodWords( dcts ):
    ND = len( dcts )
    gw = []
    for i in range( ND ):
        gw = gw + dcts[i].keys()
    gw = set( gw )
    gw = list( gw )
    return gw

# Code 15-9
# build a word count matrix
def WordCountMat(dcts ):
    # dcts is a list of dct
    ND = len( dcts )  # number of dicts
    # find all words in all dicts
    goodwords = GoodWords( dcts )
    LW = len( goodwords )
    wcmat = zeros( (ND,LW), int )
    for i in range( ND ):
        for j in range( LW ):
            if goodwords[j] in dcts[i]:
                wcmat[i,j] = len( dcts[i][goodwords[j]] )
    return wcmat

# Code 15-10
# probabilities of words occuring in docs
def WordFreqMat( wcmat ):
    V = len( wcmat )
    pmat = zeros( wcmat.shape, float )
    for i in range( V ):
        pmat[i] = wcmat[i]/float( wcmat[i].sum() )
    return pmat

# Code 15-11
# probablitiy of seeing a word in ANY doc
def WordProb( wcmat ):
    vsum = wcmat.sum( 0 )  # number of times each word is seen
    tot = vsum.sum() # total number of words
    pvec = vsum/float(tot)
    return pvec

# Code 15-13
def IndicWords( wcm, voracity, pdoc, ndoc, mincount=5 ):
    # mask for words with minimun number of occurrences
    vsum = wcm.sum(0)
    mask = greater( vsum, mincount ).astype(int)
    # allocate
    ND, NW = wcm.shape
    paccum = zeros( NW, float )
    for i in pdoc:
        m = greater( voracity[i], 1 ).astype(int)
        paccum += m * voracity[i]
    naccum = zeros( NW, float )
    for i in ndoc:
        naccum += voracity[i]
    paccum /= len( pdoc )
    naccum /= len( ndoc )
    scores = mask * paccum/(naccum+1)
    ag = argsort( scores )[::-1]
    return ag, scores
        
