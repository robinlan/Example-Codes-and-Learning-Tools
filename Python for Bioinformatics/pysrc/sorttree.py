# sorttree.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

# see Chapter 14 for explanation of these codes

# tree is a dictionary
# each node is   id: (left-kid, right-kid, data )

# Code 14-7
# add a node
def AddNode( tree, top, newid, newdata ):
    # top is the ID of the first node
    # newid and newdata are the ID and data for new node
    k = top
    ok = 1
    while ok:
        # decide if the new node goes to the left or right
        go = 0 # left is the default
        if newdata > tree[k][2]:
            go = 1  # instead go to the right
        # is there a child hanging here
        if tree[k][go] == -1:
            # no child exists
            tree[k][go] = newid
            tree[newid] = [-1, -1, newdata ]
            ok = 0
        else:
            # there is a child.  move down the tree
            k = tree[k][go]

# Code 14-8
# find the left-most node
def FindLefty( tree, top ):
    k, mom = top, -1
    ok = 1
    while ok:
        if tree[k][0] == -1:
            ok = 0
            return k, mom
        else:
            mom = k
            k = tree[k][0]

# Code 14-9
# remove a node.  Replant the right child
def RemoveNode( tree, loc, mom, top ):
    # if the node has a right child then perform a transplant
    rchild = tree[loc][1]  # the right child
    if rchild != -1 :
        if mom != -1:
            tree[mom][0] = tree[loc][1]
        else:
            top = tree[loc][1]
    elif mom != -1:
        tree[mom][0] = -1  # left child no longer exists
    # remove the node
    trash = tree.pop( loc )
    return top
    
