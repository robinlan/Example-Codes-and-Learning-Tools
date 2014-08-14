# chaos.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See chapter 23 for explanation of codes

from numpy import arange, zeros, array, take, ravel
import copy
import Image
import akando, genbank, fasta, kmeans

# Code 23-12
# create the 2D array of strings.
def AllPossStrings( abet, SZ=8 ):
    # SZ is number of iterations.  8 will make a 256x256 array
    # is also the length of teh strings
    a = array( [[abet[0],abet[1]],[abet[2],abet[3]]] )
    for n in range( SZ-1 ):
        N = len( a )
        grab = arange( 2*N )/2
        b = zeros( (2*N,2*N), 'S8' )
        # expand the matrix
        for i in range( N ):
            b[2*i] = take( a[i], grab )
            b[2*i+1] = take( a[i], grab )
        # left add the new character
        for i in range( 2*N ):
            for j in range( 2* N ):
                k, l = i%2, j%2
                m = k*2+l
                b[i,j] = abet[m] + b[i,j]
        a = copy.copy( b )
    return b

# Code 23-13
# count the number of occurences of each string.
def Counter( aps, data ):
    # aps from all possible strings
    N = len( aps )
    ctr = zeros( (N,N), int )
    for i in range( N ):
        if i%50==0: print i,
        for j in range( N ):
            ctr[i,j] = data.count( aps[i,j] )
    return ctr

def Run1():
    data = genbank.ReadGenbank( '/science/bartlett/genbank/nc_00918.gb.txt')
    dna = genbank.ParseDNA( data )
    aps = AllPossStrings( 'cgat' )
    ctr = Counter( aps, dna )
    akando.a2i( ctr ).save('dud.gif')


# a big test.  Get several images
# >>> flist
# ['ASBs', 'athaliana_chr2.txt', 'chr01.seq', 'pine_EST_10132005', 'Triticum_aestivum_release_2.fasta', 'Triticum_monococcum_release_2.fasta', 'Triticum_turgidum_release_1.fasta']
# Code 23-15
def Run2( flist ):
    aps = AllPossStrings( 'ACGT',7)
    NF = len( flist ) # the number of files
    for i in range( NF ):
        print 'Working on ',flist[i]
        # get the data
        dna = fasta.Fasta( 'bacteria/' + flist[i] )
        # count the number of genes
        NG = len( dna )
        # if there are multiple genes then combine
        if NG >1:
            t = []
            for j in range( NG ):
                t.append( dna[j][1] )
            st = ''.join(t)
            del t
        else:
            st = dna[0][1]
        del dna
        # for every million bases make a plot
        NP = int(len( st )/1000000)
        #if NP > 3:
            #NP = 3
        for j in range( NP ):
            print '\tPortion',j,'of',NP
            ctr = Counter( aps, st[j*1000000:j*1000000+1000000] )
            akando.a2i( ctr ).save('work/chaos'+str(i)+'c'+str(j)+'.gif' )
            
# >>> mglist =['chaos0c0.gif', 'chaos0c1.gif', 'chaos0c2.gif', 'chaos1c0.gif', 'chaos1c1.gif', 'chaos1c2.gif', 'chaos2c0.gif', 'chaos2c1.gif', 'chaos2c2.gif', 'chaos3c0.gif', 'chaos3c1.gif', 'chaos3c2.gif', 'chaos4c0.gif', 'chaos4c1.gif', 'chaos4c2.gif', 'chaos5c0.gif', 'chaos5c1.gif', 'chaos5c2.gif', 'chaos6c0.gif', 'chaos6c1.gif', 'chaos6c2.gif']
def SortRun( mglist, K ):
    N = len( mglist )
    D = 128**2  # word length of 7
    data = zeros( (N,D), float )
    for i in range( N ):
        mg = Image.open( mglist[i] )
        d = akando.i2a( mg )/255.
        data[i] = ravel( d + 0 )
    # perform k-means
    clusts1 = kmeans.Init2( K, data )
    ok = 1
    while ok:
        mmb = kmeans.AssignMembership( clusts1, data )
        clusts2 = kmeans.ClusterAverage( mmb, data )
        diff = ( abs( ravel(clusts1)-ravel(clusts2))).sum()
        if diff==0:
            ok = 0
        clusts1 = clusts2 + 0
        print diff
    return clusts1, data


###############
# Code 23-14
def ChaosJeffrey( seq, D=256 ):
    # D is the dimension of the picture space
    N = len( seq )
    A = zeros( (D,D) )
    abet = 'acgt'
    tg = array( [[0,0], [0,D], [D,0], [D,D]] )
    me = array([D/2., D/2.])
    for i in range( N ):
        ndx = abet.find( seq[i] )
        half = (me+tg[ndx])/2.
        v,h = half.astype(int)
        A[v,h ] += 1
        me = half + 0
    return A

###########
# Code 23-17
def PlotColors( mglist, cffs ):
    N = 7
    pts = []
    for i in range( 7 ):
        pts.append( [] )
    for i in range( len( cffs )):
        d = int( mglist[i][5] )
        pts[d].append( cffs[i] )
    for i in range( 7 ):
        akando.PlotMultiple( 'd'+str(i)+'.txt' , array(pts[i]))
        

