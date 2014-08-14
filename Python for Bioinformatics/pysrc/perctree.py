# perctree.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008


import copy

# Code 14-13
def StarterTree( ):
    tree = {}
    tree[0] = [0.1, -1, 1, 2]
    tree[1] = [0.4, 0, 3, 4]
    tree[2] = [0.3, 0, -1, -1]
    tree[3] = [0.3, 1, -1, -1]
    tree[4] = [0.1, 1, 5, 6]
    tree[5] = [0.6, 4, -1, -1]
    tree[6] = [0.7, 4, -1, -1]
    return tree

# Code 14-14
def Swap( tree, mom, kid ):
    # swap tree[mom] with tree[kid]
    grandma = tree[mom][1]
    if grandma != -1:
        if tree[grandma][2] == mom:
            tree[grandma][2] = kid
        else:
            tree[grandma][3] = kid
    # grandkids
    gkid1, gkid2 = tree[kid][2:]
    if gkid1 != -1:
        tree[gkid1][1] = mom
    if gkid2 != -1:
        tree[gkid2][1] = mom
    # aunts
    if tree[mom][2] == kid:
        aunt = tree[mom][3]
    else:
        aunt = tree[mom][2]
    # swap
    tree[mom][1] = kid
    tree[kid][1] = grandma
    tree[aunt][1] = kid
    temp = tree[kid][2:]
    if tree[mom][2] == kid:
        tree[kid][2:] = mom,tree[mom][3]
    else:
        tree[kid][2:] = tree[mom][2], mom
    tree[mom][2:] = temp

# Code 14-15
def AddNode( tree, newmom, ident, data ):
    att = 2 # attach the kid here unless...
    if tree[newmom][2]!=-1:
        att = 3
    # new node
    tree[ident] = [data, newmom, -1, -1 ]
    tree[newmom][att] = ident

# Code 14-16
def PercUp( tree, me ):
    ok = 1
    while ok:
        mom = tree[me][1]
        if tree[mom] < tree[me]:
            ok = 0
        else:
            # perculate one step
            Swap( tree, mom, me )
        if tree[me][1] == -1:
            ok = 0  # reached the top

# Code 14-17
def PercDown( tree, me ):
    ok = 1
    while ok:
        # data from the kids
        kid1, kid2 = tree[me][2:]
        # if both kids are -1 then stop
        if kid1 == -1 and kid2 == -1:
            ok = 0
        else:
            # find kid with lowest value
            if kid1 == -1:
                kid = kid2
            elif kid2 == -1:
                kid = kid1
            else:
                # tree[me] has two kids
                if tree[kid1][0] < tree[kid2][0]:
                    kid = kid1
                else:
                    kid = kid2
            # kid is the kid to swap with.  He is valid.
            # if mom value > kid value : swap
            if tree[me][0] > tree[kid][0]:
                Swap( tree, me, kid )

