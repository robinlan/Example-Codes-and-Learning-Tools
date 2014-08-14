# normalizer.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008


from numpy import array, random, zeros
import akando

# generate a normal distribution of data
# Code 5-4
def MakeNormData( N=100, mu=3.2, dev=2.1 ):
    v = random.normal( mu, dev, N )
    return v

# plot using GnuPlot candelsticks
# Code 5-7
def PlotCandle( filename, data ):
    N, L = data.shape # number of vectors, length of vectors
    avgs = zeros( N, float ) # will contain the averages
    devs = zeros( N, float )
    for i in range( N ):
        avgs[i] = data[i].mean()
        devs[i] = data[i].std()
    # save the data
    M = zeros( (10,5), float )
    for i in range( 10 ):
        mx = data[i].max()
        mn = data[i].min()
        M[i] = i+1, avgs[i]-devs[i], mn, mx, avgs[i]+devs[i]        
    akando.PlotMultiple( filename, M )

# normalize via the mean.  Create data that has a 0-mean
# Code 5-8
def MeanNorm( vec ):
    sm = vec.sum()
    answ = vec - sm/len(vec)
    return answ

# normalize via the std dev
# Code 5-9
def StdNorm( vec ):
    b = vec.std()
    return vec/b

# create data for LOESS normalization
def MakeLData( ):
    ydata = MakeNormData( 1000 )
    xdata = random.rand( 1000 )*100
    # impose a long range average bias
    for i in range( 1000 ):
        ydata[i] = ydata[i] + 3*((-((xdata[i]-50.)/10)**2)/25 + 1)
    # plot this data
    M = zeros( (1000,2), float )
    M[:,0] = xdata + 0
    M[:,1] = ydata + 0
    return M
    #akando.Plot2DSave( 'ldata.txt', M )

def Loess( M, np=50 ):
    # sort the data
    ag = M[:,0].argsort()
    M[:,0] = M[:,0].take( ag )
    M[:,1] = M[:,1].take( ag )
    # normalize
    V,H = M.shape
    niters = V/np
    for i in range( niters ):
        avg = M[i*np:i*np+np,1].mean()
        M[i*np:i*np+np,1] -= avg
    
