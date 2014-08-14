# swussroll.py
# using k-means to solve the Swiss Roll problem
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See chapter 17 for explanation of codes

from math import atan2
from numpy import add, cos, greater, less, pi, random, sqrt,sin, zeros
import Image
import kmeans, akando

# Code 17-11
# create the data file
def MakeRoll( N=1000 ):
    # N = number of data points
    data = zeros( (N,2), float )
    for i in range( N ):
        r = random.rand( 2 )
        theta = 720*r[0] *pi/180
        radius = r[0] + (r[1]-0.5)*0.2
        # convert to x-y coords
        x = radius * cos( theta )
        y = radius * sin( theta )
        data[i] = x,y
    return data

# create an image from the data points
def MakePic( data ):
    # assume that the maximum radius of the data is 1
    pic = zeros( (512,512), int )
    for i in data:
        x,y = (i+1)*240
        pic[ int(x), int(y)] +=1
    mg = akando.a2i( pic )
    return mg

# Code 17-12
# run k-means
def RunKMeans( data, K=4 ):
    clust1 = kmeans.Init2( K, data )
    dff = 1
    while dff > 0:
        mmb = kmeans.AssignMembership( clust1, data )
        clust2 = kmeans.ClusterAverage( mmb, data )
        dff = ( abs( clust1.ravel()-clust2.ravel() )).sum()
        print dff
        clust1 = clust2 + 0
    return clust1, mmb

# Code 17-12
# save multiple files for GnuPlot
def GnuPlotFiles( mmb, data, fname ):
    # mmb is from RunKMeans
    # fname is a partial filename
    NC = len( mmb )  # number of clusters
    for i in range( NC ):
        filename = fname + str(i) + '.txt'
        fp = open( filename, 'w' )
        for j in mmb[i]:
            x,y = data[j]
            fp.write( str(x) + ' ' + str(y) + '\n')
        fp.close()

# make a color image showing the different clusters
# returns an Image
def MakeColorPic( mmb, data ):
    pic = zeros( (512,512,3), int )
    NC = len( mmb ) # number of clusters = number of colors
    for i in range( NC ):
        # for each color
        for j in mmb[i]:
            x,y = (data[j]+1)*240
            x,y = int(x), int(y)
            pic[x,y,0] = 250 - 250/NC*i # red
            if i>150: pic[x,y,1] = (i-150) *20 # green
            pic[x,y,2] = i*250 # blue
    r = akando.a2i( pic[:,:,0] )
    g = akando.a2i( pic[:,:,1] )
    b = akando.a2i( pic[:,:,2] )
    mg = Image.merge( 'RGB', (r,g,b) )
    return mg

# Code 17-13
# Change data from rectangular coordinates to polar coordinates
def GoPolar( data ):
    N = len( data ) # number of data points
    pdata = zeros( (N,2), float )
    for i in range( N ):
        x,y = data[i]
        r = sqrt( x*x + y*y )
        theta = atan2( y,x)
        pdata[i] = r, theta
    pdata[:,0] *=10    # scale the radius
    return pdata


# create a color image from Polar data by converting it to rect
# >>> clusts, mmb = RunKMeans( pdata )
# >>> mg = MakeColorPic( mmb, data )

# Code 17-16
# compute the distances for all pairs
def Neighbors( data ):
    ND = len( data )
    d = zeros( (ND,ND), float )
    for i in range( ND ):
        for j in range( i ):
            a = data[i] - data[j]
            a = sqrt( ( a*a ).sum() )
            d[i,j] = d[j,i] = a
    return d

def FastFloyd( w ):
    d = w + 0
    N = len( d )
    oldd = d + 0
    for k in range( N ):
        print k,
        newd = add.outer( oldd[:,k], oldd[k] )
        m = greater( newd, 700 )
        newd = (1-m)*newd + m * oldd
        mask = less( newd, oldd )
        mmask = 1-mask
        g = mask*newd + mmask * oldd
        oldd = g + 0
    return g

# Code 17-17
# Decide which cluster each vector belongs to
def AssignMembership( clusts, data, floyd ):
    mmb = []
    NC = len( clusts )
    ND = len( data )
    for i in range( NC ):
        mmb.append( [] )
    # for each cluster - compute the distance to all data points
    dists = zeros( (NC,ND), float )
    for i in range( NC ):
        # find the data point closest to the cluster
        d = zeros( ND, float )
        for j in range( ND ):
            t = clusts[i] - data[j]
            d[j] = sqrt( sum( t*t) )
            mn = d.argmin() # index of closest point
        mndist = d[mn] # distance from cluster to closest point
        # use floyd distances
        dists[i] = mndist + floyd[mn] # dists from cluster to all points
    # for each data point - find the closest cluster and assign
    for i in range( ND ):
        # find the cluster with the min distance
        mn = dists[:,i].argmin()
        # assign
        mmb[mn].append( i )
    return mmb

### compute the average of the clusters
##def ClusterAverage( mmb, data, floyd ):
##    K = len( mmb )
##    N = len( data[0] )
##    clusts = zeros( (K,N), float )
##    for i in range( K ):
##        vecs = data.take( mmb[i],0 )
##        vecs = 
##        clusts[i] = vecs.mean(0)
##    return clusts
