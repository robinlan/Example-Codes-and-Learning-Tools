# spelltree.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008


##>>> tree = []
##>>> tree.append( [ [], [], '', False] )
##>>> AddWord( tree, word )

# Code 14-10  (see text for explanation)
def AddWord( tree, word ):
    treeloc = 0
    wi = 0  # location in word
    NW = len( word )
    ok = 1
    while ok:
        if word[wi] in tree[treeloc][0]:
            # this letter is already in the tree
            if wi==NW-1:
                # it is the last letter of the word
                ndx = tree[treeloc][0].index( word[wi] )
                treeloc = tree[treeloc][1][ndx]
                tree[treeloc][3] = True # end of word
                ok = 0
            else:
                # move down the tree
                ndx = tree[treeloc][0].index( word[wi] )
                treeloc = tree[treeloc][1][ndx]
                wi += 1
        else:
            # the rest of the word is not in the tree
            TL = len( tree ) # current tree length
            last = TL
            tree[treeloc][0].append( word[wi] )
            tree[treeloc][1].append( TL )
            for i in range( wi, NW-1 ):
                newnode = [ [word[i+1]], [last+1], word[i], False]
                tree.append( newnode )
                last +=1 
            # last letter
            TL = len( tree )
            newnode = [ [], [], word[-1], True ]
            tree.append( newnode )
            ok = 0
            
            
