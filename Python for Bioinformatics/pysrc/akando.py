# akando.py  (Native American name meaning Ambush)
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008


from scipy import fftpack
from numpy import arange, array, ravel, transpose, zeros, sqrt, \
     nonzero, greater_equal, equal, conjugate, indices, less_equal, \
     arctan, pi, less, logical_and
import Image
import copy

########  Section 1. Plotting
# save a vector so that GnuPlot or a spreadsheet can read and plot
# Code 4-2
def PlotSave( fname, data ):
    L = len( data )
    fp = file( fname, 'w' )
    for i in range( L ):
        fp.write( str(data[i]) + '\n' )
    fp.close()

def PlotMultiple( fname, data ):
    V, H = data.shape
    fp = file( fname, 'w' )
    for i in range( V ):
        for j in range( H ):
            fp.write( str(data[i,j]) + ' ' )
        fp.write( '\n' )
    fp.close()

# surface plot
def Plot2D( fname, data ):
    V,H = data.shape
    fp = file( fname, 'w' )
    for i in range( V ):
        for j in range(  H ):
            fp.write( str(data[i,j]) + '\n' )
        fp.write( '\n' )
    fp.close()

    
########  Section 2. Algebra and Geometry
# Code 4-8
# Compute the distance between two vectors
def Distance( vec1, vec2 ):
    a = vec1 - vec2
    dist = sqrt( a*a )
    return dist

# Code 4-9
# Smooth a vector
def Smooth(data,window):    
    # data is the input data
    # window is the linear dimension of the smoothing kernel
    dim = data.shape
    ndim = len( dim )	# the number of dimensions
    
    # for a 1D vector smooth it
    if ndim == 1 :
        ans = zeros( dim, float )
        K = sum(data[0:window+1])
        ans[0] = K/(window+1)
    	# ramp up
    	for i in range(1,window+1):
    		K = K + data[i+window]
    		ans[i] = K / (i+window+1)
        # steady as she goes
        for i in range(window+1,dim[0]-window) :
            K = K + data[window+i] - data[i-window-1];
            ans[i] = K / (2*window+1)
        # Ramp down
        j = 0
        if dim<window+window : j =window+window-dim[0]
        for i in range(dim[0]-window,dim[0]):
            K = K - data[i-window-1]
            ans[i] = K / (2*window-j)
            j = j+1
        # end of vector smooth
    else :	# you have more than 1 dimension.  For now assume that it is 2
    	# smooth the columns and then the rows
    	t = data + 0	# the +0 insures that t and data do not use the same memory
    	for i in range(0,dim[0]):
    		t[i,:] = Smooth(t[i,:],window)
    	for j in range(0,dim[1]):
    		z = (Smooth(t[:,j],window))[0:dim[0]]
    		t[:,j] = z
    	ans = t
    # end the 2D
    return ans

# Code 4-11
# Subtract the baseline from an oscillating signal
def Baseline(data, WN=100):
    # data: input vector
    # WN: window size
    # returns smoothed vector
    L = len(data)
    pts=[]
    for i in range(0, L, WN):
        a=data[i:i+WN]
        mn = a.min()
        x=nonzero(equal(a, mn))[0][0]
        pts.append([i+x, mn])
    nd=zeros(len(data), float)
    nsegs=len(pts)-1    
    for i in range(nsegs):
        x1, y1 = pts[i]
        x2, y2 = pts[i+1]
        m = (y2-y1)/(x2-x1)
        b = y1 - m*x1
        w = arange(x1,x2)
        y=m*w + b
        nd[x1:x2]=data[x1:x2]-y
    # create a vector containing 0 if nd is less than zero
    mask=greater_equal(nd, 0)
    # new new data
    nnd = mask*nd
    return nnd

# histogram.
# if min and max not given then
# automatically sets the range to the max and min value
# Code 4-13
def RangeHistogram( indata, nbins, mn=-1, mx=-1 ):
    # indata is the input data
    # nbins is the number of bins
    ans = zeros(nbins)
    L = len( indata )
    data = indata + 0
    fix = 0
    if mn==-1 and mx==-1:
        # no limits were given so - create an autoscale
        mx = indata.max( )*1.01
        mn = indata.min()
    else:
        mx *= 1.01
    data = (indata-mn)/(mx-mn)*nbins
    print data.max()
    hst = zeros( nbins, int )
    for i in range( L ):
        k = int( data[i] )
        hst[k] += 1
    return hst

# Code 4-14
# x,y: x and y values for multiple data points
# returns slope, intercept
def linearRegression( x,y ):
    """Returns m,b.  x and Y are vectors"""
    sxy = ( x * y +0.0).sum()	# also ensures at least a float type
    sx = ( x +0.0).sum()
    sy = ( y +0.0).sum()
    sx2 = ( x*x +0.0).sum()
    n = len( x )
    m = ( n * sxy - sx * sy ) / ( n * sx2 - sx*sx)
    b = ( sy - m * sx ) / n
    return m,b

# Code 4-14
def Circle( size, loc,rad):
    # size is (v,h) of size of array
    # loc is (v,h) of circle location
    # rad is integer of radius
    # returns matrix with binary values.  Circle is filled with 1
    b1,b2 = indices( size )
    b1,b2 = b1-loc[0], b2-loc[1]
    mask = b1*b1 + b2*b2
    mask = less_equal( mask, rad*rad ).astype(int)
    return mask

def WedgeFilter( vh, t1, t2 ):
    """in degrees"""
    ans = zeros( vh )
    ndx = indices( vh ).astype(float)
    ndx[0] = ndx[0] - vh[0]/2
    ndx[1] = ndx[1] - vh[1]/2
    # watch out for divide by zero
    mask = equal( ndx[0], 0 )
    ndx[0] = (1-mask)*ndx[0] + mask*1e-10
    # compute the angles
    ans = arctan( ndx[1] / ndx[0] )
    # mask off the angle
    ans = ans + pi/2    # scales from 0 to pi
    mask = greater_equal( ans, t1/180.*pi )
    mask2 = less( ans, t2/180.*pi )
    mask = logical_and( mask, mask2).astype(int)
    return mask

########  Section 3. Correlations
#  Code 4-17
# performs a correlation between two vectors or two matrices
# returns correlation.
def Correlate( a, b ):
    # performs Fourier space correlation
    n = len( a.shape )
    if n==1:
        A = fftpack.fft(a)
        B = fftpack.fft(b)
        C = A * conjugate( B )
        d = fftpack.ifft( C );
        d = Swap(d);
    if n==2:
        A = fftpack.fft2(a) 
        B = fftpack.fft2(b)
        C = A * conjugate( B )
        d = fftpack.ifft2( C )
        d = Swap(d)
    return d

# Code 4-17
# swaps half-segments of vectors or quadrants of matrices
# A: input array
# returns swapped array
def Swap( A ):
    #performs a quadrant swap
    if len(A.shape) == 2:
        (v,h) = A.shape
        ans = zeros(A.shape,A.dtype)
        ans[0:v/2,0:h/2] = A[v/2:v,h/2:h]
        ans[0:v/2,h/2:h] = A[v/2:v,0:h/2]
        ans[v/2:v,h/2:h] = A[0:v/2,0:h/2]
        ans[v/2:v,0:h/2] = A[0:v/2,h/2:h]
    # perform a vector swap
    if len(A.shape) ==1 :
        v = A.shape[0]
        ans = zeros(A.shape,A.dtype)
        if v%2==0:	# even number of elements
            ans[0:v/2] = A[v/2:v]
            ans[v/2:v] = A[0:v/2]
        else:		# odd number of elements
            ans[0:v/2] = A[v-v/2:v]
            ans[v/2:v] = A[0:v/2+1]
    return ans


########  Section 4. Image Conversions
# Code 4-18
# converts 2D array to an image
def a2i( data ):
    mg = Image.new( 'L', transpose(data).shape)
    mn = data.min()
    a = data - mn
    mx = a.max()
    a = a*256./mx
    mg.putdata( ravel(a))
    return mg

# Code 4-18
# converts 2D array to image without auto-scaling
# user is reponsible to ensure all values in data are between 0 and 255
def a2if( data ):
    mg = Image.new( 'L', transpose(data).shape)
    mg.putdata( ravel(data))
    return mg

# Code 4-18
# Converts gray scale image to a matrix.  Do not use if mg.mode != 'L'
def i2a( mg ):
    mgt = mg.transpose(2).transpose(1)
    f = mgt.getdata()	# a structure
    z = array(f)
    zz = z.reshape( mg.size )
    zz = zz.transpose()
    #zz = transpose(reshape(z,mg.size))
    return zz

