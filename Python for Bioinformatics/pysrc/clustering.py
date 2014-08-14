# clustering.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

from numpy import random, zeros

# Code 17-1
def CData( N, L, scale = 1, K=-1 ):
    # N = number of data vectors
    # L = length of data vectors
    # create a random seeds
    if K==-1:
        K = int( random.rand()*N/20 + N/20)
    seeds = random.ranf( (K,L) )
    # create random data based on deviations from seeds
    data = zeros( (N,L), float )
    for i in range( N ):
        pick = int( random.ranf() * K )
        data[i] = seeds[pick] +scale*(0.5*random.ranf(L)-0.25)
    return data

# Code 17-2
# compare all of the vectors to a target: abs-subtraction
def CompareVecs( target, vecs ):
    N = len( vecs ) # number of vectors
    diffs = abs( target - vecs )
    scores = diffs.sum( 1 )
    return scores

# Code 17-4
# greedy clustering algorithm
def CheapClustering( vecs, gamma ):
    # vecs: array of data vectors
    # gamma: threshold.  Below this is a match
    clusters = [ ]  # collect the clusters here.
    ok = 1
    work = list(vecs) # copy of data that can be destroyed
    while ok:
        target = work.pop( 0 )
        # score for all remaining vecs in work
        scores = CompareVecs(target, work )
        # threshold vectors
        nz = nonzero( less( scores, gamma) )[0][::-1]
        group = []
        group.append( target )
        # add vectors to group
        for i in nz:
            group.append( work.pop( i ) )
        clusters.append( group )
        if len( work )==1:
            clusters.append( [work.pop(0)])
        if len(work)==0:
            ok = 0
    return clusters

