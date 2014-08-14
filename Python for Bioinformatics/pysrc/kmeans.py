# kmeans.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See chapter 17 for explanation of codes

from numpy import random, take, zeros, sqrt, ravel

# Code 17-5
# Measure the variance of a cluster
def ClusterVar( vecs ):
    a = vecs.std( 0 )
    a = sum( a**2 )/len(vecs[0])
    return a

# Code 17-6
# initialize with random vectors
def Init1( K, L ):
    # K is the # of clusters
    # L is the length of the vectors
    """K=Number of clusters, L=Length of vectors"""
    clusts = random.ranf( (K,L) )
    return clusts

# Code 17-6
# initialize with random data vectors
def Init2( K, data ):
    """K=Number of clusters"""
    r = range( len(data) )
    random.shuffle( r )
    clusts = data.take( r[:K],0 )
    return clusts

# Code 17-7
# Decide which cluster each vector belongs to
def AssignMembership( clusts, data ):
    mmb = []
    NC = len( clusts )
    for i in range( NC ):
        mmb.append( [] )
    for i in range( len( data )):
        sc = zeros( NC )
        for j in range( NC ):
            sc[j] = sqrt( ((clusts[j]-data[i])**2 ).sum() )
        mn = sc.argmin()
        mmb[mn].append( i )
    return mmb

# Code 17-8
# compute the average of the clusters
def ClusterAverage( mmb, data ):
    K = len( mmb )
    N = len( data[0] )
    clusts = zeros( (K,N), float )
    for i in range( K ):
        vecs = data.take( mmb[i],0 )
        clusts[i] = vecs.mean(0)
    return clusts

# Code 17-9
# typical driver
def KMeans( K, data ):
    clust1 = Init2( K, data )
    ok = 1
    while ok:
        mmb = AssignMembership( clust1, data )
        clust2 = ClusterAverage( mmb, data )
        diff = ( abs( ravel(clust1)-ravel(clust2))).sum()
        if diff==0:
            ok = 0
        print 'Difference', diff
        clust1 = clust2 + 0
    return clust1, mmb

# Code 17-21
# split a cluster
def Split( mmbi ):
    # mmbi is a single mmb[i]
    m1, m2 = [], []
    N = len( mmbi )
    for i in range( N ):
        r = random.rand()
        if r < 0.5:
            m1.append( mmbi[i] )
        else:
            m2.append( mmbi[i] )
    return m1, m2
