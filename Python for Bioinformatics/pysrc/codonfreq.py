# codonfreq.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See chapter 20 for explanation of codes

import genbank, som, akando
import Image
from numpy import zeros

# Code 20-1
def FileReadConvert( fname, codkeys ):
    # fname is the name of a Genbank file
    # codkeys is codons.keys( ) were codons is from genbank.Codons()
    # read in the DNA sequences for the genes
    data = genbank.ReadGenbank( fname )
    dna = genbank.ParseDNA( data )
    klocs = genbank.FindKeywordLocs( data )
    glocs = genbank.GeneLocs( data, klocs )
    NG = len( glocs ) # number of genes
    codons = []
    for i in range( NG ):
        # extract DNA for this sequence
        cdna = genbank.GetCodingDNA( dna, glocs[i] )
        # convert to codons
        c = []  # codons for this gene
        for j in range( 0, len(cdna), 3 ):
            c.append( codkeys.index( cdna[j:j+3] ))
        codons.append( c )
    return codons

# Code 20-2
# convert a list of numbers to a histogram
def Freq64( inlist ):
    cnts = zeros( 64, float )
    for i in range( 64 ):
        cnts[i] = inlist.count( i )
    freq = cnts/cnts.sum()
    return freq

# Code 20-3
# convert a list of codon-integers into codon frequencies
def CodonFrequencies( cods, lim=100 ):
    # cods from FileReadConvert
    N = len( cods ) # number of genes
    codfreqs = [ ]
    for i in range( N ):
        if len( cods[i]) >= 3*lim:
            codfreqs.append( Freq64( cods[i] ))
    return codfreqs

######  SOM

# See Code 20-10
# create the SOM
def MakeSOM( data ):
    net = som.SOMinit2( 100, 4 )
    hrad = 100
    for i in range( 100 ):
        print 'Iteration ',i
        for vec in data:
            bmu = som.GetBMU( net, vec )
            som.Update( net, vec, hrad, bmu )
        hrad -=1
    return net

# Code 20-11
def Color4( net ):
    # net from Make SOM
    # convert 4 channels to 3colors
    V,H,N = net.shape
    red = zeros( (V,H), float )
    green = zeros( (V,H), float )
    # first channel
    blue = net[:,:,0] + 0.67*net[:,:,1] + 0.33*net[:,:,2]
    green = 0.1*net[:,:,0] + 0.4*net[:,:,1] + 0.4*net[:,:,2]+0.1*net[:,:,3]
    red = net[:,:,3] + 0.67*net[:,:,2] + 0.33*net[:,:,1]
    #
    r = akando.a2i( red )
    g = akando.a2i( green )
    b = akando.a2i( blue )
    mg = Image.merge( 'RGB', (r,g,b) )
    return mg

# Code 20-12
# locate points
def LocateMany( net, data ):
    N = len( data ) # number of points to plot
    V,H,n = net.shape
    pix = zeros( (V,H) )
    for i in range( N ):
        bmu = som.GetBMU( net, data[i] )
        pix[bmu] = 1
    return pix

