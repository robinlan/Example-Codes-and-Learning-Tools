# som.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See Chapter 18 for explanation of codes

import Image
from numpy import zeros, random, sqrt, indices, less, multiply, ones,\
     logical_and
import akando

# Code 18-1
def ReadImage( fname ):
    mg = Image.open( fname )
    r,g,b = mg.split()
    h,v = r.size
    data = zeros( (v,h,3), float )
    data[:,:,0] = akando.i2a( r )/255.
    data[:,:,1] = akando.i2a( g )/255.
    data[:,:,2] = akando.i2a( b )/255.
    return data

# Code 18-2
def SOMinit( N, dm ):
    som = random.ranf( (N,N,dm) )
    return som

# Code 18-2
def SOMinit2( N, dm ):
    som = zeros( (N,N,dm), float )
    ndx = indices( (N,N) )
    som[:,:,0]= ndx[0]/256.
    som[:,:,1]=ndx[1]/256.
    som[:,:,2] = (ndx[0]-ndx[1])/256. + 0.5
    return som

# Code 18-3
def SOMmg( som ):
    r = akando.a2i( som[:,:,0] )
    g = akando.a2i( som[:,:,1] )
    b = akando.a2i( som[:,:,2] )
    mg = Image.merge( 'RGB', (r,g,b) )
    return mg

# Code 18-4
def RandomVec( data ):
    V,H,n = data.shape
    y = int( H*random.rand() )
    x = int( V*random.rand() )
    vec = data[x,y]
    return vec

# Code 18-5
def GetBMU( som, vec ):
    # measure the distance to all vectors
    t = som - vec
    dist = sqrt( ( t*t).sum(2) )
    # find the smallest distance
    H = som.shape[1]
    mn = dist.min()
    v,h = divmod( dist.argmin(), H )
    return v,h

# Code 18-12
def Update( som, vec, hrad, bmu ):
    # compute the vectors inside of a radius
    V,H, n = som.shape
    bmuv, bmuh = bmu
    ndx = indices( (V,H) )
    ndx[0] -= bmuv
    ndx[1] -= bmuh
    dist = sqrt( ndx[0]**2 + ndx[1]**2 )
    mask = less( dist, hrad ).astype(float)
    # weight these vectors
    mask *= 0.05
    # update
    #for i in range( n ):
    #    som[:,:,i] = som[:,:,i] + mask*(vec-som)[:,:,i]
    mask = multiply.outer( mask,ones(n,int) )
    som += mask*(vec-som)

# Code 18-14
def SOMiterate( data, som, hradinit, hrate ):
    hrad = hradinit
    while hrad > 1:
        vec = RandomVec( data )
        bmu = GetBMU( som, vec )
        Update( som, vec, hrad, bmu )
        hrad *= hrate
    return som

# Code 18-17
# find boundaries given a SOM
def FindBoundaries( som, gamma=0.3 ):
    V,H,N = som.shape
    used = zeros( (V,H), int )
    groups = zeros( (V,H), int )
    ctr = 1
    while used.sum() < V*H:
        # pick a point
        for i in range( V ):
            for j in range( H ):
                if used[i,j]==0:
                    pi, pj = i,j
                    break
            if used[i,j]==0:
                break
        # find similar vectors
        probe = som[pi,pj]
        hits = abs(som-probe).sum(2)
        hits = less( hits, gamma).astype(int)
        # define a group, update mask
        hits = logical_and( hits, (1-used) )
        used += hits
        if hits.sum() > 10:
            groups += ctr*hits
        ctr +=1
    return groups

# Code 18-18
def Cluster( data, som, groups ):
    M = groups.max()
    mmbs = []
    for i in range( M+1 ):
        mmbs.append( [] )
    V,H,N = data.shape
    for i in range( V ):
        for j in range( H ):
            bmu = GetBMU( som, data[i,j] )
            groupid = groups[bmu]
            mmbs[groupid].append( data[i,j] )
    return mmbs

# Code 18-19
def StdDevs( mmbs ):
    for i in range(len(mmbs)):
        mat = array( mmbs[i] )
        print i, mat.std(0)
