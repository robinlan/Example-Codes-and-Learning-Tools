# nongreedy.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See chapter 12 for explanation of codes

from numpy import *
import greedy, ga, genbank, blosum
import copy

# Code 12-22
# get the best pairs from the matrix M
def BestPairs( M, gamma ):
    # M from greedy.FastMat
    work = M+0
    hits = []
    ok = 1
    V,H = work.shape
    while ok:
        mx = work.max()
        if mx > gamma:
            v,h = divmod( work.argmax(), H )
            hits.append( (v,h) )
            work[v,h] = gamma-1
        else:
            ok = 0
    return hits

# Code 12-23
# convert a GA-gene into an aligned assembly
def Gene2Assembly( gene, pairs, seqs, seqnames, L ):
    # L from greedy.FastMat
    smb = []
    used = zeros( len( seqs ), int )
    for g in gene:
        i1, i2 = pairs[g] # the indices of the sequences
        # find the locations of the sequences in the assembly
        b1, c1 = greedy.Finder( smb, seqnames[i1] )
        b2, c2 = greedy.Finder( smb, seqnames[i2] )
        # decide what to do with the information
        s1, s2 = greedy.ShiftedSeqs( seqs[i1], seqs[i2], L[i1,i2] )
        if b1==-1 and b2==-1:
            greedy.NewContig( smb, s1, s2, seqnames[i1], seqnames[i2] )
        if b1!=-1 and b2==-1:
            greedy.Add2Contig( smb, s1, s2, seqnames[i2], b1, c1 )
        if b1==-1 and b2!=-1:
            greedy.Add2Contig( smb, s2, s1, seqnames[i1], b2, c2 )
        if b1!=-1 and b2!=-1 and b1!=b2:
            greedy.JoinContigs( smb, b1, b2, c1, c2, s1, s2 )
        used[i1] = used[i2] = 1
    # unused
    for i in nonzero( 1-used)[0]:
        smb.append( [(seqnames[i], seqs[i])] )
    return smb

# Code 12-25
# given a list of characters, return the most common character
# exclude '.'
def ConsensusCol( stg ):
    chrs = []
    cnts = []
    ape = copy.copy( stg )
    while len( ape )>0:
        C = ape[0]
        chrs.append( C )
        N = ape.count( ape[0] )
        if C!='.':
            cnts.append( N )
        else:
            cnts.append(0)
        for i in range( N ):
            ape.remove( C )
    # find most common
    vec = array( cnts )
    ndx = vec.argmax( )
    return chrs[ndx]

# Code 12-26
def CatSeq( smb ):
    NC = len( smb ) # number of contigs
    sq = ''
    for i in range( NC ):
        NS = len( smb[i] )
        # get the length of the strings
        lgs = zeros( NS, int )
        for j in range( NS ):
            lgs[j] = len( smb[i][j][1] )
        # find the max length
        mxlg = lgs.max()
        # for each column
        for j in range( mxlg ):
            # grab all characters
            y = []
            for k in range( NS ):
                if lgs[k] > j:
                    y.append( smb[i][k][1][j] )
            sq += ConsensusCol( y )
    return sq

# Code 12-27
# generate several genes for GA
def InitGA( pairs, Ngenes ):
    # pairs from BestPairs
    # Ngenes = desired number of GA genes
    work = arange( len(pairs ))
    genes = []
    for i in range( Ngenes ):
        random.shuffle( work )
        genes.append( copy.deepcopy(work) )
    return genes

# Code 12-30
# fix a gene
def FixGene( gene, pairs ):
    # count the number of times each pairing is used
    NP = len( pairs )
    gene = list( gene )
    cnts = zeros( NP, int )
    for i in range( NP ):
        cnts[i] = gene.count( pairs[i] )
    # find the missing and the duplicates
    missg = nonzero( equal( cnts, 0) )[0]
    dups = nonzero( equal( cnts, 2 ))[0]
    # rearrange the dups
    random.shuffle( dups )
    # replace a duplicate with a missing
    for i in range( len( dups )):
        # locate the two duplicates
        d1 = gene.index( pairs[dups[i]] )
        d2 = gene.index( pairs[dups[i]], d1+1 )
        # select one
        if random.rand()>0.5:
            choose = d1
        else:
            choose = d2
        # replace
        gene[choose] = pairs[missg[i]]
    gene = array( gene )
    return gene

# Code 12-28
# score all genes
def CostAllGenes( genes, pairs, seqs, seqnames, L ):
    NG = len( genes )
    cost = zeros( NG )
    for i in range( NG ):
        smb = Gene2Assembly( genes[i], pairs, seqs, seqnames, L )
        cseq = CatSeq( smb )
        cost[i] = len( cseq )
    return cost

# Code 12-32
# mutate by swapping
def SwapMutate( genes, rate ):
    for i in range( len(genes) ):
        dm = len( genes[i] )
        for j in range( dm ):
            if random.rand() < rate:
                pick = int( random.rand() * dm )
                a = genes[i][pick]+0
                genes[i][pick] = genes[i][j] + 0
                genes[i][pick] = a + 0

# Code 12-33
# run the GA test
def RunGA( hits, seqs, seqnames, L ):
    NH = len( hits )
    folks = InitGA( hits, 10 )
    fcost = CostAllGenes( folks, hits, seqs, seqnames, L )
    print fcost.min(), fcost.argmin()
    for i in range( 10 ):
        kids = ga.CrossOver( folks, fcost )
        for i in range(len(kids)):
            kids[i] = FixGene( kids[i], arange(NH) )
        kcost = CostAllGenes( kids, hits, seqs, seqnames, L )
        ga.Feud( folks, kids, fcost, kcost )
        SwapMutate( folks, 0.03 )
        fcost = CostAllGenes( folks, hits, seqs, seqnames, L )
        print fcost.min(), fcost.argmin()
    return folks[fcost.argmin() ]
