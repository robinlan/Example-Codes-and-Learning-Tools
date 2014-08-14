# upgma.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008


from numpy import zeros
import akando, copy

# Code 14-5
# Create a binary tree using the UPGMA algorithm
def UPGMA( indata ):
    data = copy.deepcopy( indata )
    # data is a list of vectors
    N = len( data )  # number of data vectors
    N2 = 2*N-1
    BIG = 999999
    scmat = zeros( (N2,N2), float ) + BIG
    # initial pairwise comparisons
    for i in range( N ):
        for j in range( i ):
            scmat[i,j] = (abs( data[i]-data[j] )).sum()
    #
    tree, used = {}, []
    for i in range( N-1 ):
        v,h = divmod( scmat.argmin(), N2 )
        tree[N+i] = (v, h, scmat.min() )
        used.append( v )
        used.append( h )
        avg = ( data[v] + data[h])/2.
        data.append( avg )
        for j in range( N+i ):
            if j not in used:
                scmat[N+i,j] = (abs( avg-data[j] )).sum()
        scmat[v] = zeros( N2 ) +BIG
        scmat[h] = zeros( N2 ) +BIG
        scmat[:,v] = zeros( N2 ) +BIG
        scmat[:,h] = zeros( N2 ) +BIG
    return tree

        
        
