# gapalign.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See chapter 13 for explanation of these codes

from numpy import zeros, argsort, array, ones, random, less, nonzero
import dynprog, easyalign
import copy

GDNA = array( [[3,-1,-1,-1,0,0,0],\
               [-1,3,-1,-1,0,0,0],\
               [-1,-1,3,-1,0,0,0],\
               [-1,-1,-1,3,0,0,0],
               [0,0,0,0,0,0,0],\
               [0,0,0,0,0,0,0],\
               [0,0,0,0,0,0,0]] )
GBET = 'ACGTN-.'

# Code 13-1
# chop up a sequence and insert some anomalies
def ChopFunny( inseq, nsegs, length ):
    segs = []
    locs = []
    G = len( inseq ) - length   # last possible starting location in inseq
    for i in range( nsegs ):
        r = int( random.rand()*G )   # start the cut here
        locs.append( r )
        segs.append( inseq[r:r+length] )
    # remove random elements
    for i in range( len( segs) ):
        a = list( segs[i] )
        r = less( random.rand(len(a)), 0.05 )
        nz = nonzero( r )[0][::-1]
        for j in nz:
            dump = a.pop( j )
        segs[i] = ''.join( a )
    return segs, locs

# Code 13-2
def ShowOverlap( locs, L, W, scale=10):
    # L = length of initial sequence
    # W = length of segs[0]
    N = len( locs )
    for i in range( N ):
        st = ''
        for j in range( 0, L, scale ):
            if locs[i]<j<locs[i]+W:
                st += 'X'
            else:
                st += ' '
        print i,'\t', st

# Code 13-5
def LeadTrailGaps( st ):
    w = st.lstrip('-')
    d = len( st ) - len(w)
    st = '.'*d + st[d:]
    w = st.rstrip('-')
    d = len( st ) - len( w )
    if d>0:
        st = st[:-d] + '.'*d
    return st

# Code 13-6
def SWBacktrace2( scormat, arrow, seq1, seq2 ):
    st1, st2 = '',''
    v,h = arrow.shape
    ok = 1
    v,h = divmod( scormat.argmax(), len(seq2)+1 )
    v2,h2 = v,h
    vskips, hskips = [], []
    while ok:
        if arrow[v,h] == 0:
            st1 += seq1[v-1]
            st2 += '-'
            vskips.append( v-1 )
            v -= 1
        elif arrow[v,h] == 1:
            st1 += '-'
            st2 += seq2[h-1]
            hskips.append( h-1 )
            h -= 1
        elif arrow[v,h] == 2:
            st1 += seq1[v-1]
            st2 += seq2[h-1]
            v -= 1
            h -= 1
        elif arrow[v,h] == 3:
            ok = 0
        if v==0 and h==0:
            ok = 0
    v1,h1 = v,h
    # reverse the strings
    st1 = st1[::-1]
    st2 = st2[::-1]
    # replace leading and trailing gaps
    st1 = LeadTrailGaps( st1 )
    st2 = LeadTrailGaps( st2 )
    # append portions that weren't aligned
    st1 = seq1[:v1] + st1 + seq1[v2:]
    st2 = seq2[:h1] + st2 + seq2[h2:]
    return st1, st2,vskips, hskips

# Code 13-7
# receives a list of strings.  Uses fastSW to align
def InitAligns( ids, seqs, submat, abet  ):
    lines = []
    N = len( seqs )
    for i in range( N ):
        for j in range( i ):
            if i!=j:
                bv = dynprog.FastSubMatrix( submat, abet, seqs[i], seqs[j] )
                sc, ar = dynprog.FastSW( bv, seqs[i], seqs[j] )
                t1, t2, vs, hs = SWBacktrace2( sc, ar, seqs[i], seqs[j] )
                lines.append( (ids[i], ids[j],t1,t2) )
    return lines

# Code 13-8
# score the alignments
def InitScores( lines, submat, abet ):
    N = len( lines )
    sc = zeros( N, int )
    for i in range( N ):
        sc[i] = easyalign.BlosumScore( submat, abet, lines[i][2], lines[i][3] )
    return sc

# Code 13-12
# directly from Missouri
# smb = Assembly = list of contigs
def ShowMe( smb, start=0 ):
    N = len( smb )
    for i in range( N ):
        K = smb[i].keys()
        for k in K:
            spcs = 10-len(k)
            print k,' '*spcs, smb[i][k][start:start+50]
        print ''

# Code 13-13
def Finder( smb, askid ):
    N = len( smb )
    hit = -1
    for i in range( N ):
        if askid in smb[i].keys():
            hit = i
            break
    return hit

# Code 13-18    
def GapSeqByList( seq, glist ):
    glist.sort()
    temp = list( seq ) # convert to liste
    for i in glist:
        temp.insert( i, '-' )
    nseq = ''.join( temp ) # convert to string
    return nseq

# Code 13-19
def GapCtgByList( ctg, gaplist ):
    k = ctg.keys()
    for i in k:
        ctg[i] = GapSeqByList( ctg[i], gaplist )
        ctg[i] = LeadTrailGaps( ctg[i] )

# Code 13-10
def NewCtg( linei ):
    print 'NewCtg', linei[:2]
    ctg = { }
    id1, id2 = linei[:2]
    ctg[id1] = linei[2]
    ctg[id2] = linei[3]
    return ctg

# Code 13-25
def JoinCtgs( smb, f1, f2, id1, id2, submat, abet):
    print 'JoinCtgs', f1, f2
    bv = dynprog.FastSubMatrix( submat, abet, smb[f1][id1], smb[f2][id2] )
    scm, ar = dynprog.FastSW(bv, smb[f1][id1], smb[f2][id2] )
    t1,t2,vgap,hgap = SWBacktrace2( scm, ar,smb[f1][id1], smb[f2][id2] )
    GapCtgByList( smb[f1], hgap )
    GapCtgByList( smb[f2], vgap )
    for i in smb[f2].keys():
        smb[f1][i] = smb[f2][i]
    del( smb[f2] )

# Code 13-21
def AddToCtg(smb, fm, idm, idum, ilinei, submat, abet ):
    print 'AddToCtg', idum, 'added to ', fm
    if ilinei[0]==idm:
        matseq = ilinei[2] # the seq that is also in the smb
        umatseq = ilinei[3]
    else:
        matseq = ilinei[3] # the seq that is also in the smb
        umatseq = ilinei[2]
    # align match seq with the same seq in smb
    bv = dynprog.FastSubMatrix( submat, abet, smb[fm][idm], matseq )
    scm, ar = dynprog.FastSW(bv, smb[fm][idm], matseq )
    t1,t2,vgap,hgap = SWBacktrace2( scm, ar, smb[fm][idm], matseq )
    # gap contig
    GapCtgByList( smb[fm], hgap )
    # create and add new sequence
    newseq = GapSeqByList( umatseq, vgap )
    newseq = LeadTrailGaps( newseq )
    smb[fm][idum] = newseq

# Code 13-27
def Assemble( seqs, sc, ids, lines, mnsc, submat, abet ):
    smb = []
    ok = 1
    ag = argsort( sc )[::-1] 
    k = 0
    notused = copy.copy( ids )
    while ok:
        # get the best score
        i = ag[k]
        if sc[i]> mnsc:
            # find the elements in contigs
            id1, id2 = lines[i][:2]
            f1 = Finder( smb, id1 )
            f2 = Finder( smb, id2 )
            # decide to create, add, or join
            if f1==-1 and f2 == -1:
                # create
                smb.append( NewCtg( lines[i] ))
            if f1 != -1 and f2 == -1:
                # the id1 is in ctg (smb[f1] )
                AddToCtg( smb, f1, id1, id2, lines[i], submat, abet)
            if f1==-1 and f2 != -1:
                AddToCtg( smb, f2, id2, id1, lines[i], submat, abet )  
            if f1 != -1 and f2 != -1 and f1 != f2:
                # Join 2 contigs
                JoinCtgs( smb, f1, f2, id1, id2, submat, abet )
            if id1 in notused:
                notused.remove( id1 )
            if id2 in notused:
                notused.remove( id2 )
        k += 1
        if k>= len( ag ): ok = 0
    # add unused sequences
    for i in notused:
        ndx = ids.index( i )
        ctg = {i: seqs[ndx]}
        smb.append( ctg )
    return smb

