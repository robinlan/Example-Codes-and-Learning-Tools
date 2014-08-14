# hmm.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See Chapter 10 for explanation of these routines

from numpy import zeros

# Code 10-2
# find a match for a single node
def EMatch( hmmi, letter ):
    hit = (-1,-1)
    for i in hmmi[0]:
        if letter == i[0]:
            hit = i
            break
    return hit

# Code 10-3
# compute the probability
def EProb( hmm, instr ):
    L = len( instr )
    k = 0
    pbs = 1.0
    for i in range( L ):
        # get emission
        emit = EMatch( hmm[k], instr[i] )
        pbs *= emit[1]
        # get transition
        k = hmm[k][1]
    return pbs

# Code 10-4
def SimpleTHMM( ):
    hmm = {}
    hmm['begin'] = ('',{0:1.0} )
    hmm[0] = ('A', {1:0.3,2:0.7} )
    hmm[1] = ('B', {3:1.0} )
    hmm[2] = ('C', {3:1.0} )
    hmm[3] = ('D', {'end':1.0} )
    hmm['end'] = ('',{} )
    return hmm

# Code 10-5
def NextNode( hmm, k, ask ):
    t = hmm[k][1].keys() # transition for this node
    hit = []
    for i in t:
        if hmm[i][0]==ask:
            hit = i, hmm[k][1][i]
            break
    return hit

# Code 10-6
def TProb( hmm, instr ):
    L = len( instr )
    pbs = 1.0
    k = 'begin'
    for i in range( L ):
        tran = NextNode( hmm,k,instr[i])
        k = tran[0]
        pbs *= tran[1]
    return pbs

# Code 10-7
# build a node table
def NodeTable( sts, abet):
    # sts is a list of data strings
    L = len( sts )  # the number of strings
    D = len( sts[0] )   # length of string
    A = len( abet )
    NT = zeros( (A,D),int )-1
    nodecnt = 0
    for i in range( D ):
        for j in range( L ):
            ndx = abet.index( sts[j][i] )
            if NT[ndx,i] ==-1:
                NT[ndx,i] = nodecnt
                nodecnt +=1
    return NT

# Code 10-8
def MakeNodes( sts, abet, weights, nodet ):
    L = len( sts )
    D = len( sts[0] )   # length of string
    hmm = {}
    for j in range( D-1):
        for i in range( L ):
            # current letter
            clet = sts[i][j]
            # next letter
            nlet = sts[i][j+1]
            # node associated with current letter
            cnode = nodet[ abet.index(clet), j ]
            # node associated with next letter
            nnode = nodet[ abet.index(nlet), j+1]
            print i,j,clet,nlet, cnode, nnode
            # connect the nodes
            if hmm.has_key( cnode ):
                # adjust the transition
                if hmm[cnode][1].has_key( nnode ):
                    hmm[cnode][1][nnode] += weights[i]
                else:
                    hmm[cnode][1][nnode] = weights[i]
            else:
                hmm[cnode]= ( clet ,{ nnode: weights[i] })
    return hmm

# Code 10-9
def Normalization( hmm ):
    t = hmm.keys()
    for i in t:
        sm = 0
        for j in hmm[i][1].keys():
            sm += hmm[i][1][j]
        for j in hmm[i][1].keys():
            hmm[i][1][j] /= sm

# Code 10-10
def Ends( hmm, sts, abet, weights, nodet ):
    # add begin node
    T = {}
    L = len( sts )
    for i in range(L):
        clet = sts[i][0]
        nlet = sts[i][1]
        idt = nodet[ abet.index(clet) ,0]
        #print idt, T
        if idt != -1:
            if T.has_key( idt ):
                T[ idt] += weights[i]
            else:
                T[ idt] = weights[i]
    hmm['begin'] = ( '', T )
    # add end node
    hmm['end'] = ('',{} )
    for i in range( L ):
        clet = sts[i][-1]
        idt = nodet[ abet.index(clet) ,-1]
        hmm[idt] = (clet,{'end':1})
            
# Code 10-11
def BuildHMM( sts, abet, weights ):
    nodet = NodeTable( sts, abet )
    hmm = MakeNodes( sts, abet, weights, nodet )
    Normalization( hmm )
    Ends( hmm, sts, abet, weights, nodet )
    return hmm

