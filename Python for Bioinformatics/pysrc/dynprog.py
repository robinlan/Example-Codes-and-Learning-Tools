# dynprog.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008


from numpy import *

# Code 8-1
# Create a scoring matrix and arrow matrix
# mat: input substitution matrix
# abet: alphabet
# seq1, seq2: strings to be aligned
# gap: gap penalty
# returns: scoring matrix, arrow matrix
def ScoringMatrix( mat, abet, seq1, seq2, gap=-8 ):
    l1, l2 = len( seq1), len(seq2)
    scormat = zeros( (l1+1,l2+1), int )
    arrow = zeros( (l1+1,l2+1), int )
    # create first row and first column
    scormat[0] = arange(l2+1)* gap
    scormat[:,0] = arange( l1+1)* gap
    arrow[0] = ones(l2+1)
    for i in range( 1, l1+1 ):
        for j in range( 1, l2+1 ):
            f = zeros( 3 )
            f[0] = scormat[i-1,j] + gap
            f[1] = scormat[i,j-1] + gap
            n1 = abet.index( seq1[i-1] )
            n2 = abet.index( seq2[j-1] )
            f[2] = scormat[i-1,j-1] + mat[n1,n2]
            #if i==1 and j==1: print f
            scormat[i,j] = f.max()
            arrow[i,j] = f.argmax()
    return scormat, arrow

# Code 8-3
# backtrace to create alignments
# arrow: arrow matrix
# seq1, seq2: strings to be aligned
# returns aligned strings
def Backtrace( arrow, seq1, seq2 ):
    st1, st2 = '',''
    v,h = arrow.shape
    ok = 1
    v-=1
    h-=1
    while ok:
        if arrow[v,h] == 0:
            st1 += seq1[v-1]
            st2 += '-'
            v -= 1
        elif arrow[v,h] == 1:
            st1 += '-'
            st2 += seq2[h-1]
            h -= 1
        elif arrow[v,h] == 2:
            st1 += seq1[v-1]
            st2 += seq2[h-1]
            v -= 1
            h -= 1
        if v==0 and h==0:
            ok = 0
    # reverse the strings
    st1 = st1[::-1]
    st2 = st2[::-1]
    return st1, st2

# Code 
# create a fast substitution matrix
def FastSubMatrix( mat, abet, seq1, seq2 ):
    l1, l2 = len( seq1), len(seq2)
    LM = len( mat )
    # convert the sequences to numbers
    si1 = zeros( l1, int )
    si2 = zeros( l2, int )
    for i in range( l1 ):
        si1[i] = abet.index( seq1[i] )
    for i in range( l2 ):
        si2[i] = abet.index( seq2[i] )
    # create a matrix that contains the blosum values
    br = ravel( mat )
    bvndx = add.outer( LM*si1, si2 )
    bv = take( br, ravel( bvndx ))
    bv = reshape( bv, (l1,l2) )
    bvt = zeros( (l1+1,l2+1), int )
    bvt[1:,1:] = bv + 0
    bv = ravel( bvt )
    return bv

# Code 8-8
# Faster version of the substitution matrix
def FastSubValues( mat, abet, seq1, seq2 ):
    l1, l2 = len( seq1 ), len(seq2)
    subvals = zeros( (l1+1,l2+1), int )
    # convert the sequences to numbers
    si1 = zeros( l1, int )
    si2 = zeros( l2, int )
    for i in range( l1 ):
        si1[i] = abet.index( seq1[i] )
    for i in range( l2 ):
        si2[i] = abet.index( seq2[i] )
    for i in range( 1, l1+1 ):
        subvals[i,1:] = mat[ [si1[i-1]]*l2,si2]
    return subvals

# Code 8-6    
def CreateIlist( l1, l2 ):
    ilist = []
    for i in range( l1 + l2 -1 ):
        st1 = min( i+1, l1 )
        sp1 = max( 1, i-l2+2 )
        st2 = max( 1, i-l1+2 )
        sp2 = min( i+1, l2 )
        #print st1, sp1, st2, sp2
        ilist.append( (arange(st1,sp1-1,-1),arange(st2,sp2+1)))
    return ilist

# Code 8-9
# Fast version of Needleman-Wunsch
def FastNW( subvals, seq1, seq2, gap=-8 ) :
    l1, l2 = len( seq1), len(seq2)
    scormat = zeros( (l1+1,l2+1), int )
    arrow = zeros( (l1+1,l2+1), int )
    # create first row and first column
    scormat[0] = arange(l2+1)* gap
    scormat[:,0] = arange( l1+1)* gap
    arrow[0] = ones( l2+1 )
    # compute the ilist
    ilist = CreateIlist( l1, l2 )
    # fill in the matrix
    for i in ilist:
        LI = len( i[0] )
        f = zeros( (3,LI), float )
        x,y = i[0]-1, i[1]+0
        f[0] = scormat[x,y] + gap
        x,y = i[0]+0,i[1]-1
        f[1] = scormat[x,y] + gap
        x,y = i[0]-1,i[1]-1
        f[2] = scormat[x,y] + subvals[i]
        f += 0.1 * sign(f) * random.ranf( f.shape ) # to randomly select from a tie
        mx = (f.max(0)).astype(int)   # best values
        maxpos = f.argmax( 0 )
        scormat[i] = mx + 0
        arrow[i] = maxpos + 0
    return scormat, arrow

# Code 8-11
# Fast version of Smith-Waterman (see textbook)
def FastSW( subvals, seq1, seq2, gap=-8 ) :
    l1, l2 = len( seq1), len(seq2)
    scormat = zeros( (l1+1,l2+1), int )
    arrow = zeros( (l1+1,l2+1), int )
    # create first row and first column
    arrow[0] = ones( l2+1 )
    # compute the ilist
    ilist = CreateIlist( l1, l2 )
    # fill in the matrix
    for i in ilist:
        LI = len( i[0] )
        f = zeros( (4,LI), float )
        x,y = i[0]-1, i[1]+0
        f[0] = scormat[x,y] + gap
        x,y = i[0]+0,i[1]-1
        f[1] = scormat[x,y] + gap
        x,y = i[0]-1,i[1]-1
        f[2] = scormat[x,y] + subvals[i]
        f += 0.1 * sign(f) * random.ranf( f.shape ) # to randomly select from a tie
        mx = (f.max(0)).astype(int)   # best values
        maxpos = f.argmax( 0 )
        scormat[i] = mx + 0
        arrow[i] = maxpos + 0
    return scormat, arrow

# Code 8-13
# Backtrace for Smith-Waterman (see textbook)
def SWBacktrace( scormat, arrow, seq1, seq2 ):
    st1, st2 = '',''
    v,h = arrow.shape
    ok = 1
    #arrow[0] = ones( h )
    #mx = amax(amax(scormat ))
    v,h = divmod( scormat.argmax(), len(seq2)+1 )
    while ok:
        print v,h,arrow[v,h], scormat[v,h],'    ',
        if arrow[v,h] == 0:
            st1 += seq1[v-1]
            st2 += '-'
            v -= 1
        elif arrow[v,h] == 1:
            st1 += '-'
            st2 += seq2[h-1]
            h -= 1
        elif arrow[v,h] == 2:
            st1 += seq1[v-1]
            st2 += seq2[h-1]
            v -= 1
            h -= 1
        elif arrow[v,h] == 3:
            ok = 0
        if (v==0 and h==0) or scormat[v,h]==0:
            ok = 0
    # reverse the strings
    st1 = st1[::-1]
    st2 = st2[::-1]
    return st1, st2
