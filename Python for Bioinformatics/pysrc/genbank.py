# genbank.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008


import string

# Code 6-5
# loads entire Genbank text file
def ReadGenbank( filename ):
    fp = file( filename )
    data = fp.read()
    fp.close()
    return data

# Code 6-6
# extracts DNA string from 'data' (from ReadGenbank)
def ParseDNA( data ):
     # find ORIGIN
    orgn = data.find( 'ORIGIN' )
    # find the first row after that
    start = data.find( '1', orgn )
    # find the ending slashes
    end = data.find( '//', orgn )
    # split the substring into lines
    a = data[start:end].split('\n')
    dna = ''    # blank string
    for i in a:
        spl = i.split()
        dna += ''.join( spl[1:] )
    return dna

# Code 8-9
# fine the number of occurrences of '   CDS   ' keyword
# returns location in data where keyword exists
def FindKeywordLocs( data, keyword = '   CDS   ' ):
    # number of occurences
    ngenes = data.count( keyword )
    # find the occurences
    keylocs = []
    k =0
    for i in range( ngenes ):
        t = data.find( keyword,k)
        keylocs.append( t )
        k = t + 10
    return keylocs

# Code 6-11        
# get the start and end location for a normal or complement-only
def EasyStartEnd( data, loc, cflag=0 ):
    # find the ..
    dots = data.find( '..', loc )
    # targets
    if cflag==0:
        t1, t2 = ' ','\n'
    if cflag==1:
        t1, t2 = '(',')'
    # find the first preceding blank
    b1 = data.rfind( t1,loc, dots )
    # find the first following newline
    b2 = data.find( t2, dots )
    # extract the numbers
    temp = data[b1+1:dots]
    temp = temp.replace( '>', '' )
    temp = temp.replace( '<', '' )
    start = int( temp )
    temp = data[dots+2:b2]
    temp = temp.replace( '>', '' )
    temp = temp.replace( '<', '' )
    end = int( temp )
    return start, end

# Code 6-17, 6-18
# Creates complement of DNA string
def Complement( st ):
    table = string.maketrans( 'acgt', 'tgca' )
    st = st.translate( table )
    st = st[::-1]
    return st

# Code 6-13
# data: from ReadGenbank
# loc: a single location of a keyword
# returns a list of splice locations (keylocs[i])
def Splices( data, loc ):
    # loc is the location of '
    join = data.find( 'join', loc )
    # find the parentheses
    p1 = data.find( '(', join )
    p2 = data.find( ')', join )
    # count the number of dots
    ndots = data.count( '..', p1, p2 )
    # extract the numbers
    numbs = []
    # the first has a ( .. ,
    dots = data.find( '..', p1 )
    t = data[p1+1:dots]
    t = t.replace( '<','')
    st = int( t )
    comma = data.find( ',',p1 )
    en = int( data[dots+2:comma] )
    numbs.append( (st,en) )
    # consider the rest except first and last
    # code is , .. ,
    for i in range( ndots - 2):
        dots = data.find( '..', comma )
        comma2 = data.find( ',', dots )
        st = int( data[comma+1:dots] )
        en = int( data[dots+2:comma2] )
        numbs.append( (st,en) )
        comma = comma2
    # last one has code , .. )
    dots = data.find( '..', comma )
    st = int( data[comma+1:dots] )
    en = int( data[dots+2: p2] )
    numbs.append( (st,en) )
    return numbs

# Code 6-15
# extract the locations of the genes
def GeneLocs( data, keylocs  ):
    genes = []
    for i in range( len( keylocs  )):
        # get this line and look for join or complement
        end = data.find( '\n', keylocs [i] )
        temp = data[keylocs [i]: end ]
        joinf = 'join' in temp
        compf = 'complement' in temp
        # get the extracts
        c = 0
        if compf == True: c = 1
        if joinf:
            numbs = Splices( data, keylocs [i] )
            genes.append( (numbs, compf ))
        else:
            sten = EasyStartEnd( data, keylocs [i], c )
            genes.append( ([sten],compf) )
    return genes

# Code 6-19
# using a gene location from GeneLocs, extract the DNA
def GetCodingDNA( dna, genesi ):
    # dna from ParseDNA
    # genesi is genes[i] from GeneLocs
    codedna = ''
    N = len( genesi[0] ) # number of splices
    for i in range( N ):
        st, en = genesi[0][i]
        codedna += dna[st-1:en]
    # complment flag
    if genesi[1]:
        codedna = Complement( codedna )
    return codedna

# Code 6-20
# codon dictionary
# stop = 'p'
# start = 'M'
def Codons( ):
    codons = {}
    CODONS = ( ('TTT','F'), ('TTC','F'), ('TTA','L'),('TTG','L'),\
               ('TCT','S'), ('TCC','S'), ('TCA','S'),('TCG','S'),\
               ('TAT','Y'), ('TAC','Y'), ('TAA','p'),('TAG','p'),\
               ('TGT','C'), ('TGC','C'), ('TGA','p'),('TGG','W'),\
               ('CTT','L'), ('CTC','L'), ('CTA','L'),('CTG','L'),\
               ('CCT','P'), ('CCC','P'), ('CCA','P'),('CCG','P'),\
               ('CAT','H'), ('CAC','H'), ('CAA','Q'),('CAG','Q'),\
               ('CGT','R'), ('CGC','R'), ('CGA','R'),('CGG','R'),\
               ('ATT','I'), ('ATC','I'), ('ATA','I'),('ATG','M'),\
               ('ACT','T'), ('ACC','T'), ('ACA','T'),('ACG','T'),\
               ('AAT','N'), ('AAC','N'), ('AAA','K'),('AAG','K'),\
               ('AGT','S'), ('AGC','S'), ('AGA','R'),('AGG','R'),\
               ('GTT','V'), ('GTC','V'), ('GTA','V'),('GTG','V'),\
               ('GCT','A'), ('GCC','A'), ('GCA','A'),('GCG','A'),\
               ('GAT','D'), ('GAC','D'), ('GAA','E'),('GAG','E'),\
               ('GGT','G'), ('GGC','G'), ('GGA','G'),('GGG','G'))
    for i in CODONS:
        codons[i[0].lower()] = i[1]
    return codons

# Code 6-21
# convert a coding DNA string to a protein
def Codons2Protein( codedna, codons ):
    protein = ''
    for i in range( 0, len(codedna), 3 ):
        codon = codedna[i:i+3]
        protein += codons[ codon ]
    return protein

# Code 6-23
# get the protein translation data from the file
def Amino( data, loc ):
    # get the amino acids
    trans = data.find( '/translation', loc )
    # find the second "
    quot = data.find( '"', trans + 15 )
    # extract
    prot = data[trans+14:quot]
    # remove newlines and blanks
    prot = prot.replace( '\n','' )
    prot = prot.replace( ' ','')
    return prot
