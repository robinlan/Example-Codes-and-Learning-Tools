# greedy.py
# greedy assembly
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See Chapter 12 for explanation of the codes

from numpy import *
import easyalign

# Code 12-1
# chop a long sequence into overlapping segments.
# Complete converage is not guaranteed
def ChopSeq( inseq, nsegs, length ):
    # inseq is the input sequence
    # nsegs is the number of segments that will be returned
    # length is the length of each segment
    # returns a list of sequence segments
    segs = []
    G = len( inseq ) - length   # last possible starting location in inseq
    for i in range( nsegs ):
        r = int( random.rand()*G )   # start the cut here
        segs.append( inseq[r:r+length] )
    return segs

# Code 12-3
def FastMat( seqs, submat, abet ):
    # seqs is a list of strings
    # submat: substitution matrix.  BLOSUM
    # abet: alphabet
    N = len( seqs ) # number of seqs
    M = zeros( (N,N), int )
    L = zeros( (N,N), int )
    for i in range( N ):
        for j in range( i+1,N ):
            bsl = easyalign.BruteForceSlide( submat, abet, seqs[i], seqs[j] )
            M[i,j] = bsl.max()  # peak value
            L[i,j] = bsl.argmax()   # location
    return M,L

# Code 12-5
def ShiftedSeqs( seq1, seq2, loc ):
    L2 = len( seq2 )
    act = L2 - loc
    if act <=0:
        st1 = seq1
        st2 = (-act) *'.' + seq2
    else:
        st1 = act*'.' + seq1
        st2 = seq2
    return st1, st2

# Code 12-9
def NewContig( smb, s1, s2, fnams1, fnams2 ):
    #s1, s2 = ShiftedSeqs( seq1, seq2, loc )
    c = []  # the new contig
    c.append( (fnams1, s1) )
    c.append( (fnams2, s2) )
    smb.append( c )

# Code 12-10
def ShowContigs( smb, st=0 ):
    lc = len( smb )
    for i in range( lc ):
        for j in smb[i]:
            print j[0],'\t', j[1][st:st+50]
        print ''
    print''

# Code 12-12
# which contig is it in
def Finder( smb, seqid ):
    lc = len( smb )
    answ = -1
    ndx = -1
    for i in range( lc ):
        for j in range( len( smb[i] )):
            if seqid == smb[i][j][0]:
                answ = i
                ndx = j
                break
    return answ, ndx

# Code 12-14
def Add2Contig( smb, seqa, seqb, seqbid, ctgn, ctgndx ):
    # seq1a seq2a from ShiftedSeqs
    # ctgn, ctgndx from Finder
    if seqb[0] == '.':
        ndots = smb[ctgn][ctgndx][1].count('.')
        temp = '.'*ndots + seqb
        smb[ctgn].append( (seqbid,temp) )
    else:
        nd1 = seqa.count('.')
        ndc = smb[ctgn][ctgndx][1].count('.')  # number of dots in ctg seq
        diff = nd1 - ndc
        if diff >0:
            for i in range( len( smb[ctgn] )):
                smb[ctgn][i] = smb[ctgn][i][0], diff*'.' + smb[ctgn][i][1]
        else:
            seqb = (-diff)*'.' + seqb
        smb[ctgn].append( (seqbid,seqb) )

# Code 12-17
def JoinContigs( smb, cnumb1, cnumb2, cseq1, cseq2, sa, sb ):
    # create a new contig from two old ones.
    c = []
    # how many dots should ctg1 be shifted?
    sh1 = sa.count('.') - smb[cnumb1][cseq1][1].count('.')
    # how many dots for ctg2
    sh2 = sb.count('.') - smb[cnumb2][cseq2][1].count('.')
    if sh1 < 0:
        sh2 -= sh1
        sh1 = 0
    if sh2 < 0 :
        sh1 -= sh2
        sh2 = 0
    # combine
    for i in range( len( smb[cnumb1] )):
        temp = ( smb[cnumb1][i][0], sh1*'.'+smb[cnumb1][i][1] )
        c.append( temp )
    for i in range( len( smb[cnumb2] )):
        temp = ( smb[cnumb2][i][0], sh2*'.'+smb[cnumb2][i][1] )
        c.append( temp )
    if cnumb1 > cnumb2:
        a = smb.pop( cnumb1 )
        a = smb.pop( cnumb2 )
    else:
        a = smb.pop( cnumb2 )
        a = smb.pop( cnumb1 )
    smb.append( c )

# Code 12-19
def Assemble( fnms, seqs, submat, abet, gamma = 500 ):
    used = zeros( len( fnms ))  # set these elements to 1 when a seq is used
    M,L = FastMat( seqs, submat, abet )
    #print M
    ok = 1
    smb = []
    nseqs = len( seqs )
    while ok:
        v,h = divmod( M.argmax(), nseqs )
        if M[v,h] >= gamma:
            vnum, vseqno = Finder( smb, fnms[v] )
            hnum, hseqno = Finder( smb, fnms[h] )
            s1, s2 = ShiftedSeqs( seqs[v], seqs[h], L[v,h] )
            if vnum == -1 and hnum == -1:
                # create a new contig
                NewContig( smb, s1, s2, fnms[v], fnms[h] )
            if vnum != -1 and hnum == -1:
                Add2Contig( smb, s1, s2, fnms[h], vnum, vseqno )
            if vnum == -1 and hnum != -1:
                Add2Contig( smb, s2, s1, fnms[v], hnum, hseqno )
            if vnum != -1 and hnum != -1 and vnum != hnum:
                JoinContigs( smb, vnum, hnum, vseqno, hseqno, s1, s2 )
            M[v,h] = 0
            used[v] = used[h] = 1
        else:
            ok = 0
    # make single contigs for all sequences not used
    notused = nonzero( 1-used)[0]
    for i in notused:
        smb.append( [(fnms[i],seqs[i])] )
    return smb
