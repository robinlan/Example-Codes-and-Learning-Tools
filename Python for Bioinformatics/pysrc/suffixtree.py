# suffixtree.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# See Chapter 14 for explanation of these codes

import copy

# Code 14-19
# cut up the string
def CutUp( instr ):
    cuts = []
    for i in range( len(instr) ):
        cuts.append( instr[i:] )
    cuts.sort()
    return cuts

# Code 14-20
# separate according to first letters
def Divide( cuts ):
    # get first letters
    flett = []
    for i in cuts:
        flett.append( i[0] )
    flett = set( flett )
    # divide into groups by first letters
    divd = []
    for i in flett:
        temp = []
        for j in cuts:
            if j[0]==i: temp.append( j )
        divd.append( temp )
    return divd

# Code 14-21
# find common start
def CommonStart( alist ):
    st = alist[0][0]  # first letter
    ok = 1
    k = 1
    while ok:
        for j in alist:
            if len(j)<= len(st): ok =0
        if ok:
            t = st + alist[0][k]
            for j in alist:
                if not(j.startswith(t)):
                    ok = 0
                    break
            if ok:
                st = t
            k+=1
    return st

# Code 14-22
def Strip( alist, cs ):
    a = []
    N = len( cs )
    for i in alist:
        if len(i) > N:
            a.append( i[N:] )
    return a

# Code 14-23
def SuffixTree( instr ):
    cuts = CutUp( instr )
    divd = Divide( cuts )
    nodes = []
    wait = []  # parts waiting to be considered
    k = 0  # keeps track of current node
    for i in range( len( divd )):
        wait.append( (-1,divd[i]))
    while len(wait) >0:
        a = wait.pop(0)
        cs = CommonStart( a[1] )
        nodes.append( (k,a[0],cs) )
        sp = Strip( a[1], cs )
        divd = Divide( sp )
        for j in divd:
            wait.append( (k,j) )
        k +=1
    return nodes
