# easyalign.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008


from numpy import equal, not_equal, zeros

# Code 7-1
# scoring an alignment.  +1 for a match. -1 for mismatch
# -2 for gaps
def SimpleScore( s1, s2 ):
    a1 = map( ord, list(s1) )
    a2 = map( ord, list(s2) )
    # count matches
    score = ( equal( a1, a2 )).astype(int).sum()
    # count mismatches
    score = score - ( not_equal( a1,a2) ).astype(int).sum()
    # gaps
    ngaps = s1.count( '-' ) + s2.count('-')
    score = score - ngaps
    return score

# Code 7-5
# scoring an alignment using the blosum matrix or equivalent
def BlosumScore( mat, abet, s1, s2, gap=-8 ):
    sc = 0
    n = min( [len(s1), len(s2)] )
    for i in range( n ):
        if s1[i] == '-' or s2[i] == '-' and s1[i] != s2[i]:
            sc += gap
        elif s1[i] == '.' or s2[i] == '.':
            pass
        else:
            n1 = abet.index( s1[i] )
            n2 = abet.index( s2[i] )
            sc += mat[n1,n2]
    return sc

# Code 7-6
# align two sequences using a brute force slide and the blosum matrix
def BruteForceSlide( mat, abet, seq1, seq2 ):
    # length of strings
    l1, l2  = len( seq1 ), len( seq2 )  #####
    # make new string with leader
    t1 = len(seq2) * '.' + seq1
    lt = len( t1 )
    answ = zeros( lt, int )
    for i in range( lt ):
        #print i, t1[i:]
        answ[i] = BlosumScore( mat, abet, t1[i:], seq2 )
    return answ
