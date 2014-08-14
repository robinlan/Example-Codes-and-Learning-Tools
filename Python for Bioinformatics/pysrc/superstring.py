# superstring.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See chapter 16 for explanation of codes

from numpy import ones, nonzero, random, zeros, concatenate, \
     greater, equal, array
import copy, string
import ga, gasort

# Code 16-9
def CutItUp( seq, ncuts, mincut=20, maxcut=40 ):
    answ = []
    lseq = len( seq )
    unseen = ones( lseq ) # tracks elements that have been used
    folds = zeros( lseq )
    for i in range( ncuts ):
        # get a list of elements not yet used
        nz = nonzero( unseen )[0]
        # randomly select a starting point
        beg = int( random.rand() * (lseq-mincut) )
        # randomly select an ending point
        lg = int( random.rand()*(maxcut-mincut)) +mincut # length of cut
        end = beg + lg
        if end >= lseq:
            end = lseq
        # make this cut
        answ.append( seq[beg: end] )
        # change unseen
        unseen[beg:end] = zeros( end-beg)
        folds[beg:end] += ones( end-beg )
    # it must be complete
    nz = nonzero( unseen )[0]
    while len(nz)>0:
        beg, end = nz[0], nz[0]+mincut
        if end>=lseq: end = lseq
        answ.append( seq[beg:end] )
        unseen[beg:end] = zeros( end-beg)
        nz = nonzero( unseen)[0]
        folds[beg:end] += ones( end-beg )
    return answ, folds

# Code 16-10
def SuperFromGene( gene, cuts ):
    sst = cuts[gene[0]]
    for i in range( 1, len(gene)):
        # look for cuts[i][:3] in sst
        snip = cuts[gene[i]][:3]
        cnt = sst.count( snip )
        # for all possible matches see if it is perfect
        if cnt == 0:
            # append
            sst += cuts[gene[i]]
        else:
            hit = 0 # set to 1 if a perfect match is found
            k = 0
            for j in range( cnt ):
                me = sst.find( snip, k )
                k = me + 1
                # is it a perfect match?
                L1 = len( sst )-me # length of remainder of sst
                L2 = len( cuts[i] )
                L = min((L1,L2) )
                if sst[me:me+L] == cuts[i][:L]:
                    # a perfect match
                    hit = 1
                    # attach
                    if L2>L1: sst += cuts[i][L1:]
                    break
            if hit==0:
                # all locations were viewed and none matched
                sst += cuts[gene[i]]
    return sst

# determine |S|.  length of subtring set
def LenS( S ):
    ls = 0
    for i in S:
        ls += len( i )
    return ls

# Code 16-23
def ScoreAll( genes, cuts ):
    LG = len( genes )
    sc = zeros( LG, int )
    for i in range( LG ):
        sst = SuperFromGene( genes[i], cuts )
        sc[i] = len( sst )
    return sc

# Code 16-14
def DriveGA( cuts, Ngenes ):
    # create genes
    a = range( len( cuts ))
    folks = []
    for i in range( Ngenes ):
        random.shuffle( a )
        folks.append( copy.copy(a))
    # drive
    oldbest = 999999
    fcost = ScoreAll( folks, cuts )
    valid = range( len (cuts))
    for i in range( 2000 ):
        kids = ga.CrossOver( folks, fcost )
        for j in range( Ngenes ):
            kids[j] = list( kids[j] )
            gasort.Legalize( valid, kids[j] )
        kcost = ScoreAll( kids, cuts )
        ga.Feud( folks, kids, fcost, kcost )
        ga.ShuffleMutate( folks[1:], 0.05 )
        fcost = ScoreAll( folks, cuts )
        best = fcost.min()
        if fcost.argmin() != Ngenes-1:
            random.shuffle( folks[-1] )
        if oldbest < best:
            folks[0] = copy.copy( oldgene )
            fcost[0] = oldbest
        else:
            oldbest = best
            oldgene = copy.copy( folks[ fcost.argmin()] )
        #if i%50==0: print best
    besti = fcost.argmin()
    return folks[besti]

# Code 16-16
# running complexity of a DNA string.
def SSTComplexAtStarts( glocs, dna, wind ):
    # glocs from genbank.GeneLocs
    hits = []
    for i in glocs:
        print 'I',i, i[1], i[0][0][0]
        if i[1] == False and i[0][0][0]>500:
            # considering only non-complements
            start = i[0][0][0]
            seq = dna[start-500:start+500]
            vec = []
            print i, start, len(dna), len(seq)
            for j in range( len(seq)-wind):
                print j,
                cuts, folds = CutItUp( seq[j:j+wind], 20,10,20 )
                gene = DriveGA( cuts, 16 )
                sst = SuperFromGene( gene, cuts )
                vec.append( len(sst)/float(LenS(cuts)) )
            print ''
            vec = array( vec )
            hits.append( vec )
    return hits


