# hydro.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See chapter 23 for explanation of codes

from numpy import zeros

# Code 31-1
def MakeChart( ):
    phob = []
    phob.append( ('Alanine','Ala','A',0.616) )
    phob.append( ('Arginine','Arg','R',	0))
    phob.append( ('Asparagine','Asn','N',0.236))
    phob.append( ('Aspartate','Asp','D',0.028))
    phob.append( ('Cysteine','Cys','C',	0.68))
    phob.append( ('Glutamate','Glu','E',0.043))
    phob.append( ('Glutamine','Gln','Q',0.251))
    phob.append( ('Glycine','Gly','G',	0.501))
    phob.append( ('Histidine','His','H',0.165))
    phob.append( ('Isoleucine','Ile','I',0.943))
    phob.append( ('Leucine','Leu','L',0.943))
    phob.append( ('Lysine','Lys','K',0.283))
    phob.append( ('Methionine','Met','M',0.738))
    phob.append( ('Phenylalanine','Phe','F',1))
    phob.append( ('Proline','Pro','P',	0.711))
    phob.append( ('Serine','Ser','S',0.359))
    phob.append( ('Threonine','Thr','T', 0.45))
    phob.append( ('Tryptophan','Trp','W',0.878))
    phob.append( ('Tyrosine','Tyr','Y',	0.88))
    phob.append( ('Valine','Val','V',0.825 ))
    abet = 'ARNDCEQGHILKMFPSTWYV'
    return phob, abet

# Code 23-2
def ConvertSequence( seq, phob, abet ):
    N = len( seq )
    vec = zeros( N, float )
    for i in range( N ):
        ndx = abet.find( seq[i] )
        vec[i] = phob[ndx][3]
    return vec

