# foura.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See chapter 21 for explanation of codes

from numpy import zeros, conjugate
from scipy import fftpack
import genbank

# Code 21-4
# part 1. Get data
# read a Genbank file
def GetData( fn):
    gb = genbank.ReadGenbank( fn )
    dna = genbank.ParseDNA( gb )
    klocs = genbank.FindKeywordLocs( gb )
    genes = genbank.GeneLocs( gb, klocs )
    return genes, dna

# Code 21-5
# separate DNA into coding and non-coding regions
def Separate( genes, dna ):
    Ngenes = len( genes )
    coding = []
    noncoding = []
    for i in range( Ngenes-1):
        cdna = genbank.GetCodingDNA( dna, genes[i] )
        coding.append( cdna )
        # noncoding
        start = genes[i][0][-1][-1]
        end = genes[i+1][0][0][0]
        nc = dna[start:end]
        if end-start>100: noncoding.append( nc )
    return coding, noncoding

# see numseq.py
# Part 2.
# Chang's method
def Encode( gene ):
    N = len( gene )
    temp = zeros( N, complex )
    for i in range( N ):
        if gene[i] == 'a': temp[i] = 1 + 1j
        if gene[i] == 't': temp[i] = 1 - 1j
        if gene[i] == 'g': temp[i] = -1 + 1j
        if gene[i] == 'c': temp[i] = -1 - 1j
    vec = temp[2:] + 0.5*temp[1:-1] + 0.25*temp[:-2]
    return vec

# Code 21-7
def EncodeMany( clist ):
    c = []
    for i in range( len( clist )):
        c.append( Encode( clist[i] ))
    return c

# Code 21-7
def EncodeBoth( coding, noncoding ):
    vcode = EncodeMany( coding )
    ncode = EncodeMany( noncoding )
    return vcode, ncode


### Part 3
# Code 21-8
def PowerSpectrum( vecs ):
    N = len( vecs )
    ps = []
    for i in range( N ):
        pad = zeros( 8192, complex )
        T = len( vecs[i] )
        pad[:T] = vecs[i][:T]- vecs[i][:T].mean()+ 0
        a = fftpack.fft( pad )
        ps.append( (a*conjugate(a)).real )
    return ps

# Code 21-8
def PowerBoth( vcode, ncode ):
    vps = PowerSpectrum( vcode )
    nps = PowerSpectrum( ncode )
    return vps, nps
