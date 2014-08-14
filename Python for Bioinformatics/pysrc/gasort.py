# gasort.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008


from numpy import random, equal, nonzero, zeros, less, array
import copy
import string, ga

# Code 11-17
# create the data
def Jumble( abet, ngenes ):
    folks = []
    ape = copy.copy( abet )
    for i in range( ngenes ):
        random.shuffle( ape )
        folks.append( copy.copy( ape ))
    return folks

# Code 11-18
def CostFunction( target, genes ):
    NG = len( genes ) # number of genes
    cost = []
    for gene in genes:
        c = 0
        for i in range( len( target )):
            if target[i] != gene[i]:
                c += 1
        cost.append( c )
    return cost

# Code 11-20
# make sure that the gene has all of the letters in valid
def Legalize( valid, gene ):
    # get the count for each letter
    LV = len( valid )
    LG = len( gene )    # length of this gene
    cnts = zeros( LV, int )
    for i in range( LV ):
        cnts[i] = gene.count( valid[i] )
    # get a list of the missing and duplicates
    mssg = nonzero( equal( cnts,0))[0]
    dups = nonzero( equal( cnts,2))[0]
    random.shuffle( dups )
    # replace
    for i in range(len(mssg)):
        # pick one of the dups
        k1 = gene.index( valid[dups[i]] )
        k2 = gene.index( valid[dups[i]], k1+1 )
        if random.rand() > 0.5:
            me = k1
        else:
            me = k2
        # replace
        gene[me] = valid[mssg[i]]

# Code 11-22
def Mutate( genes, rate ):
    NG = len( genes )
    for i in range( NG ):
        DM = len( genes[i] )
        r = nonzero(less( random.rand(DM), rate ))[0]
        for j in r:
            k = int( random.rand()*DM)
            a = genes[i][k]
            genes[i][k] = genes[i][j]
            genes[i][j] = a

# Code 11-23
def DriveSortGA( ):
    target = list(string.lowercase[:26])
    alpha = list(string.lowercase[:26])
    folks = Jumble( alpha, 10 )
    ok = 1
    fcost = CostFunction( target, folks )
    while ok:
        kids = ga.CrossOver( folks, array(fcost) )
        for k in range( len( kids )):
            kids[k] = list( kids[k] )
        for g in kids:
            Legalize( alpha, g )
        kcost = CostFunction( target, kids )
        ga.Feud( folks, kids, fcost, kcost )
        Mutate( folks, 0.01 )
        fcost = CostFunction( target, folks )
        #print array(fcost).min()
        if array(fcost).min() == 0:
            ok = 0
    me = array(fcost).argmin()
    return folks[me]
