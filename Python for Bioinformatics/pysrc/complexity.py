# complexity.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See chapter 16 for explanation of codes

from numpy import zeros, array
import miner, suffixtree

# Code 16-1
# complexity of written text
def EngComplexity( fname ):
    txt = file( fname ).read()
    txt = miner.Hoover( txt )
    stxt = txt.split()
    uniq = set( stxt )
    ratio = len( uniq)/float(len(stxt ))
    return ratio

# Code 16-2
# sliding window written text
def SlideEng( fname, wind ):
    # wind = window size
    txt = file( fname ).read()
    txt = miner.Hoover( txt )
    stxt = txt.split()
    L = len( stxt )-wind
    vec = zeros( L, float )
    for i in range( L ):
        uniq = set( stxt[i:i+wind] )
        vec[i] = len( uniq)/float(len(stxt[i:i+wind] ))
    return vec

# Code 16-3
def SlidePiChuting( fname='c26/pi.txt', wind=50 ):
    txt = file( fname ).read().split()[1]
    L = len( txt )-wind
    vec = zeros( L, float )
    for i in range( L ):
        a = list( str( txt[i:i+wind]))
        uniq = set( a )
        vec[i] = len( uniq)/float(len(a ))
    return vec

# Code 16-4
def DNAcomplexity( seq, abet, wordsize, wind ):
    labet = len( abet )
    L = len( seq ) - wind
    vec = zeros( L, float )
    mx = float(labet**wordsize)
    for i in range( L ):
        # gather words
        words = []
        for j in range( i, i+wind+1):
            words.append( seq[j:j+wordsize] )
        ct = len( set( words ))
        vec[i] = ct/mx
    return vec

# Code 16-7
# compare suffix tree complexity with gene start,
def ComplexityAtStarts( glocs, dna, wind ):
    # glocs from genbank.GeneLocs
    hits = []
    for i in glocs:
        print i, i[1]
        if i[1] == False:
            # considering only non-complements
            start = i[0][0][0]
            seq = dna[start-500:start+500]
            vec = []
            for j in range( len(seq)-wind):
                if j%100==0: print j,
                t = suffixtree.SuffixTree( seq[j:j+wind])
                vec.append(len(t))
            vec = array( vec )
            hits.append( vec )
    return hits
            
