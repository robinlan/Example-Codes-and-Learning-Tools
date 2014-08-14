# marray.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See chapters 24-27 for explanation of codes

from numpy import zeros, fromstring, int16, uint16, reshape, ones, \
     greater, greater_equal, less, nonzero, logical_and, log, array, \
     equal, logical_or, logical_not, sqrt, concatenate, log2, argsort, take
import Image
import akando

# Code 24-2
def ReadRawFile( fname ):
    # read as binary data
    fp = file( fname, 'rb' )
    # move to dimensions
    fp.seek( 13L )
    # read dimensions
    a = fp.read( 4 )
    H,V = fromstring( a, int16 )
    # get data
    a = fp.read( V*H )
    data = fromstring( a, uint16 )
    print len( data), V, H
    data = data.reshape( (V,H) )
    return data

# Code 24-12
# Read a GEL file from NCBI
# BE CAREFUL.  If a file has an anomaly then this code will need to be adapted.
def ReadGEL( fname ):
    fp = file( fname, 'rb' )
    # read header
    head = fp.read(8 )
    if head[:2] != 'II':
        print "Not an Intel based image."
        return -1
    # read location of tag
    tag = fromstring( head[4:], int )[0]
    # go there
    fp.seek( tag )
    # number of tags
    a = fp.read(2)
    ntags = fromstring( a, int16 )[0]
    width, height, nbits, comp, begin = FindImportantTags( fp, ntags )
    # confirm
    if nbits!=16:
        print "Not a 16-bit image"
        return -2
    if comp != 1:
        print "Image Compressed"
        return -3
    # read
    fp.seek( begin )
    data = fp.read( width*height*2)
    data = fromstring( data, uint16 )
    data = reshape( data, (height,width) )
    return data
    
# Code 24-11
def FindImportantTags( fp, ntags ):
    # fp is the file and the file pointer is at the beginning of the first tag
    important = [ 256, 257, 258, 259, 273]
    data = zeros( 5, int )
    for i in range( ntags ):
        tag = fp.read(12)
        idf = fromstring( tag[:2], int16 )[0]
        if idf in important:
            ndx = important.index(idf)
            d = fromstring( tag[8:], int)[0]
            data[ndx] = d
    return data


# SPOT FINDING

# Code 25-3
def MarkPeaks( vec ):
    N = len( vec )
    M = zeros( (3,N), float )
    M[0] = vec + 0
    M[1,:-1] = vec[1:] + 0
    M[2,1:] = vec[:-1] + 0
    mx = M.argmax(0)
    nz = nonzero( equal(mx,0) )[0]
    return nz

# Code 25-4
def Scorefor5( nz ):
    vec = array( nz )
    diff = vec[1:] - vec[:-1]
    mask = array( [17,17,17,17,22] )
    N = len( diff )
    scores = zeros( N-5, float )
    for i in range( N-5 ):
        a = abs(mask-diff[i:i+5] )
        scores[i] = (a*a).sum()
    return scores

# Code 25-7
def VertMarks( lata , gamma=20 ):
    # allocate
    V,H = lata.shape
    mark = zeros( (V,H) )
    # for each 500 row chunk
    for i in range( 0, V, 500 ):
        # cut up in to 500 row chunks; vsum
        vsum = lata[i:i+500].sum(0)
        # find the 120 best peaks
        mp = MarkPeaks( vsum )
        sc5 = Scorefor5( mp )
        # mark
        ag = argsort( sc5 )
        vec = zeros( H )
        for j in ag:
            if sc5[ag[j]] < gamma:
                vec[mp[ag[j]]] = 1
        K = 500
        if i+500>V:
            K = V-i
        for j in range( K ):
            mark[i+j] = vec + 0
    return mark

# Code 25-8
def HorzMarks( lata, gamma=15) :
    # allocate
    V,H = lata.shape
    mark = zeros( (V,H) )
    # for each 500 row chunk
    for i in range( 0, H, 500 ):
        print i
        # cut up in to 500 row chunks; vsum
        hsum = lata[:,i:i+500].sum(1)
        # find the best peaks
        mp = MarkPeaks( hsum )
        sc5 = Scorefor5( mp )
        # mark
        ag = argsort( sc5 )
        vec = zeros( V )
        for j in ag:
            if sc5[ag[j]] < gamma:
                vec[mp[ag[j]]] = 1
        K = 500
        if i+500>H:
            K = H-i
        for j in range( K ):
            mark[:,i+j] = vec + 0
    return mark

# Code 25-9
# over lay the block grid
def FinalMarks( vmark, hmark ):
    mark = vmark + hmark
    mark = greater( mark, 1.9 )
    nz = nonzero( mark )
    a = array( nz )
    a = a.transpose()
    b = list( a )
    return b

# Code 25-10
def MarkCenters( lata, pts ):
    mx = lata.max()*1.1
    N = len( pts )
    marked = lata + 0
    for i in range( N ):
        v,h = pts[i]
        marked[v-2:v+3,h] = ones( 5 )*mx
        marked[v,h-2:h+3] = ones( 5 )*mx
    return marked

# Code 25-11
# create marks for all spots in the grids
def InGrid( fm ):
    pts = []
    for p in fm:
        v,h = p
        for i in range( 5 ):
            for j in range( 5 ):
                pts.append( (v+i*17,h+j*17) )
    return pts

# Code 25-12
def FindTop( vec ):
    D = len( vec )
    svec = akando.Smooth( vec, 3 )
    k = D/2
    drc = 1
    if svec[k-1] > svec[k]: drc = -1
    while svec[k+drc] > svec[k] and 5<=k<=35:
        k += drc
    return k

# Code 25-13
def AllSpots( pts, data, Gamma=2000 ):
    N = len( pts )
    npts = []
    for i in range( N ):
        #if i%1000==0: print i
        v,h = pts[i]
        hsamp = data[v,h-20:h+20]
        nh = FindTop( hsamp )
        vsamp = data[v-20:v+20,h]
        nv = FindTop( vsamp )
        nv, nh = nv+v-20, nh+h-20
        dist = sqrt( (nv-v)*(nv-v) +(nh-h)*(nh-h) )
        if data[nv,nh] > Gamma and dist < 10:
            npts.append( (nv,nh) )
        else:
            npts.append( (v,h) )
    npts = array( npts )
    return npts

# SPOT MEASURE

# Code 25-15
def Isolate( cut ):
    V,H = cut.shape
    V2, H2 = V/2, H/2
    avg = cut[V2-3:V2+4,H2-2:H2+4].mean()
    df = abs( avg - cut )
    targ = less( df, 1000 ).astype(int)
    return targ

# Code 25-16
def FindLimits( targ ):
    V, H = targ.shape
    V2, H2 = V/2, H/2
    a = 1-targ[V2:0:-1, H2]
    up = V2 - nonzero(a)[0][0] - 1
    a = 1-targ[V2:, H2]
    down = nonzero(a)[0][0] + 1 + V2
    a = 1-targ[V2, H2:0:-1]
    left = H2 - nonzero(a)[0][0] - 1
    a = 1-targ[V2, H2:]
    right = nonzero(a)[0][0] + 1 + H2
    return up, down, left, right

# Code 25-17
def Measure( targ, limits, cut ):
    up, down, left, right = limits
    snip = cut[up+2:down-2, left+2:right-2]
    ontarg = nonzero( snip )
    valson = snip[ontarg]
    intensity = valson.mean()
    border = concatenate( (cut[up,left:right],cut[down,left:right],\
                           cut[up:down,left], cut[up:down,right]))
    backg = border.mean()
    return intensity, backg



### NCBI expression files

# Code 26-1
def ReadPlatform( fname ):
    d = file( fname ).read()
    rows = d.split('\n')
    # find title line
    k = 0
    while rows[k][0] == '#':
        k +=1
    header = rows[k]
    k +=1
    # parse title
    header = header.split('\t')
    # read in data
    NRows = len( rows) - k
    dct = {}
    for i in range( NRows ):
        items = rows[k+i].split('\t')
        if len( items )>1:
            n = int( items[0] )
            dct[n] = items[1:]
    return header, dct

# Code 26-2
def Read2Col( fname ):
    d = file( fname ).read()
    rows = d.split('\n')
    # find title line
    k = 0
    while rows[k][0] == '#':
        k +=1
    header = rows[k]
    k +=1
    # parse header
    header = header.split('\t')
    # read data
    NRows = len( rows )-k
    dct = {}
    for i in range( NRows ):
        items = rows[k+i].split('\t')
        if len( items ) > 1:
            n = int( items[0] )
            val = float( items[1] )
            dct[n] = val
    return header, dct

# Code 26-4
def ReadSection2( fname ):
    d = file( fname ).read()
    rows = d.split('\n')
    # find row that "Begin Measurements"
    k = 0
    while not("Begin Measurements" in rows[k]):
        k+=1
    k +=1
    # title
    header = rows[k]
    header = header.split('\t')
    k+=1
    # read data
    data = []
    while not ('End Measurements' in rows[k] ):
        items = rows[k].split('\t')
        data.append( items )
        k+=1
    return header, data

# Code 26-5
def GetMeasurements( hudata ):
    dct = {}
    for d in hudata:
        k = int( d[0] )
        v = (int(d[1])-1)*20+int(d[3] )-1
        h = (int(d[2])-1)*20+int(d[4] )-1
        name = d[5]
        ch1val, ch2val = float( d[14]), float(d[15])
        dct[k] = (v,h,name,ch1val, ch2val )
    return dct

# Code 26-6
def HeatMapData( gmdct ):
    # gmdct from GetMeasurements
    N = len( gmdct )
    # build grid
    k = gmdct.keys()
    vs, hs = zeros( N, int ), zeros( N, int )
    for i in range( N ):
        vs[i], hs[i] = gmdct[k[i]][:2]
    V, H = vs.max(), hs.max()
    heatg = zeros( (V+1,H+1), int )
    heatr = zeros( (V+1,H+1), int )
    for i in k:
        v,h = gmdct[i][:2]
        heatg[v,h] = gmdct[i][3]
        heatr[v,h] = gmdct[i][4]
    return heatg, heatr

# Code 26-7
def HeatMapPix( heatg, heatr ):
    V,H = heatg.shape
    avgr, avgg = heatg.mean(), heatr.mean()
    devr, devg = heatg.std(), heatr.std()
    mnr, mxr = avgr-3*devr, avgr+3*devr
    r = (heatr-mnr)/(mxr-mnr)
    rmask = greater( r, 0 ).astype(int)
    r = r*rmask
    rmask = greater( r, 1).astype(int)
    r = rmask*1 + (1-rmask)*r
    mng, mxg = avgg-3*devg, avgg+3*devg
    g = (heatg-mng)/(mxg-mng)
    gmask = greater( g, 0 ).astype(int)
    g = g*gmask
    gmask = greater( g, 1).astype(int)
    g = gmask*1 + (1-gmask)*g
    r = akando.a2if( r*255 )
    g = akando.a2if( g*255 )
    b = Image.new( 'L', (H,V) )
    mg = Image.merge( 'RGB', (r,g,b) )
    return mg

# Code 26-8
def Dct2Mat( dct ):
    N = len( dct )
    mat = zeros( (N,2), float )
    matkeys = dct.keys()
    for i in range( N ):
        mat[i] = dct[matkeys[i]][3:]
    return matkeys, mat

# Code 26-10
def RGvsI( data ):
    V,H = data.shape
    mat = zeros( (V,H) )
    mat[:,0] = (data[:,0]+data[:,1])/2 # intensity
    mat[:,1] = data[:,1]/data[:,0] # ratio
    return mat

# Code 26-11
def MvsA( data ):
    V,H = data.shape
    mat = zeros( (V,H) )
    mat[:,1] = log2( abs(data[:,1]/data[:,0])) #M
    mat[:,0] = log2( sqrt( abs(data[:,1]*data[:,0]))) #A
    return mat

# code 27-2
def Loess( mat ):
    N = len( mat )
    step = N/20
    # sort the data
    agg = argsort( mat[:,0] )
    smat = zeros( mat.shape, float )
    smat[:,0] = mat[:,0][agg]
    smat[:,1] = mat[:,1][agg]
    # find the boundaries
    ag = range( 20 )
    boundaries = smat[:,0][ag]
    boundaries = concatenate( (boundaries, [smat[-1,0]] ))
    boundaries[0] *= 0.999
    boundaries[-1] *= 1.001
    # find averages at the boundaries
    avgs = zeros( 21, float )
    avgs[0] = smat[:10,1].mean()
    for i in range( 1,21 ):
        avgs[i] = smat[i*20-10:i*20+10,1].mean()
    avgs[20] = smat[-10:,1].mean()
    # move points
    for i in range( N ):
        x = smat[i,0]
        nz = nonzero( less( boundaries, x ))[0]
        k = nz[-1]
        # weighted avg
        a1, a2 = avgs[k], avgs[k+1]
        rows, items = x-boundaries[k], boundaries[k+1]-x
        D = rows+items
        wavg = (D-rows)/float(D)*a1 + (D-items)/float(D)*a2
        smat[i,1] -= wavg
    # unsort
    umat = mat + 0
    umat[agg,1] = smat[:,1]
    return umat

# Code 27-4
# multiple slides
def ReadManyFiles( dr, nms ):
    N = len( nms )
    data = []
    for i in range( N ):
        header, hudata = ReadSection2( dr +'/' + nms[i] )
        dct = GetMeasurements( hudata )
        data.append( Dct2Mat( dct ))
    return data

# Code 27-5
def ManyMVA( data ):
    N = len( data )
    D = len( data[0][1] )
    M = zeros( (D,N), float )
    A = zeros( (D,N), float )
    for i in range( N ):
        mva = MvsA( data[i][1] )
        A[:,i] = mva[:,0] + 0
        M[:,i] = mva[:,1] + 0
        mat = zeros( (D,2), float )
        mat[:,0] = A[:,i]+0
        mat[:,1] = M[:,i]+0
        umat = Loess( mat )
        M[:,i] = umat[:,1] + 0
    return M, A

# Code 27-6
def ManyStats( M ):
    D,N = M.shape
    mat = zeros( (N,5) )
    for i in range( N ):
        mat[i,0] = i
        avg = M[:,i].mean()
        dev = M[:,i].std()
        print i, avg, dev
        mat[i,1] = avg - dev
        mat[i,2] = M[:,i].min()
        mat[i,3] = M[:,i].max()
        mat[i,4] = avg + dev
    #
    akando.PlotMultiple( 'dud.txt', mat )

# Code 27-7
def SameAverage( M ):
    avgs = M.mean(0)
    nM = M - avgs
    return nM

# Code 27-8
def SameStdev( M ):
    dev = M.std(0)
    nM = M / dev
    return nM

# Code 27-9
def Outliers( M, sigma=3 ):
    D,N = M.shape
    outies = []
    for i in range( N ):
        a = greater( M[:,i], sigma )
        hit = list(nonzero( a )[0])
        a = less( M[:,i], -sigma )
        hit += list(nonzero( a )[0])
        outies.append( hit )
    return outies

# Code 27-10
# yes = [1,0,0,0,1,0,0,0,1,0]
def SpecificOutliers( outies, yes ):
    # collect outies in yes
    couts = []
    for i in range( len( outies )):
        if yes[i]==1:
            couts.extend( outies[i] )
    group = list( set( couts )) # list of yes expressed
    N = len( group )
    scores = zeros( N, int )
    for i in range( N ):
        for j in range( len( outies )):
            if group[i] in outies[j] and yes[j]==1:
                scores[i] += 1
            if group[i] not in outies[j] and yes[j]==0:
                scores[i] += 1
    ag = argsort( scores )[::-1]
    hits = take( group, ag )
    cnts = scores[ag]
    return hits, cnts


    
######  NCBI text files



    

def ReadCY( fname ):
    d = file( fname ).read()
    rows = d.split('\n')
    # find title line
    k = 0
    while rows[k][0] == '#':
        k +=1
    header = rows[k]
    k +=1
    # parse title
    header = header.split('\t')
    NCols = len( header )
    # read data
    NRows = len( rows )-k
    dct = {}
    for i in range( NRows ):
        items = rows[k+i].split('\t')
        if len( items ) > 1:
            t = []
            print items
            kk = int( items[0] )
            for j in range( NCols-1):
                if items[j+1]=='null' or items[j+1]=='':
                    t.append( 'null' )
                elif '.' in items[j+1]:
                    t.append( float(items[j+1]))
                else:
                    t.append( int(items[j+1]))          
            dct[kk] = t
    return header, dct
    
def Grid( plat ):
    # plat from ReadPlatform
    k = plat.keys()
    N = len( k )
    grid = zeros( (N,5), int )
    for i in range( N ):
        grid[i,0] = k[i]
        d = plat[k[i]]
        for j in range( 4 ):
            grid[i,1+j] = int( d[j] )
    return grid

def BigGrid( grid ):
    mxs = grid.max(0)
    V = mxs[1]*mxs[3]
    H = mxs[2]*mxs[4]
    N = len( grid )
    bg = zeros( (N,3), int )
    bg[:,0] = grid[:,0] + 0
    bg[:,1] = (grid[:,1]-1)*mxs[3] + grid[:,3]-1
    bg[:,2] = (grid[:,2]-1)*mxs[4] + grid[:,4]-1
    return bg

def GridToDict( bg ):
    bgdct = {}
    for i in range( len( bg )):
        bgdct[bg[i,0]] = bg[i,1:]
    return bgdct



















def OutlierSort( outies ):
    couts = []
    for i in outies:
        couts.extend( i )
    group = set( couts )
    group = list( group )
    N = len( group )
    cnt = zeros( N, int )
    for i in range( N ):
        cnt[i] = couts.count( group[i] )
    ag = argsort( cnt )[::-1]
    hits = take( group, ag )
    cnts = cnt[ag]
    return hits, cnts
