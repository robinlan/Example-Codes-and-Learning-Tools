# ga.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008



from numpy import *

# Code 11-7
# Sample cost function
def CostFunction( data, genes ):
    ND = len( data ) # number of data points
    NG = len( genes ) # number of genes
    costs = zeros( NG, float )
    for i in range( NG ):
        c = 0
        for j in range( ND ):
            c += abs( dot( data[j], genes[i] ))
        costs[i] = c + 0
    return costs

# Code 11-8    
def CrossOver( folks, fcost ):
    # convert costs to probabilities
    dim = len( folks[0] )
    prob = fcost + 0.0
    mx = prob.max()
    prob = mx - prob	# lowest cost is now highest numbr.
    mx = prob.max()
    prob = prob / mx	# makes sure numbers aren't too high
    prob = prob / prob.sum()	# normalized.  sum(prob) = 1.0
    # make new kids
    kids = []
    NG = len( folks )
    for i in range( NG/2 ):
        rdad = random.rand()
        rmom = random.rand()
        # find which vectors to use
        sm = 0.0
        idad = 0
        while rdad > sm:
            sm = sm + prob[idad]
            idad = idad + 1
        sm = 0.0
        imom = 0
        while rmom > sm:
            sm = sm + prob[imom]
            imom = imom+1
        idad,imom = idad-1,imom-1
        # make babies
        x = int(random.rand()*(dim-2))+1	# crossover
        kids.append(concatenate((folks[idad][:x],folks[imom][x:])))
        kids.append(concatenate((folks[imom][:x],folks[idad][x:])))
    return kids

# Code 11-9
def Feud( folks, kids, fcost, kcost ):
    for i in range( 0, len(kids) ):
        if kcost[i] < fcost[i]:
            folks[i] = kids[i]
            fcost[i] = kcost[i]

# Code 11-10
def Mutate( folks, rate ):
    # will mutate genes.  Rate is the percentage of genes that will change
    # rate = 0.01 => 1%
    # if you have inbreeding then automatically raise the rate
    # inbreeding is defined as more than half of the vectors being the same
    cnt = 0
    NG = len( folks ) # number of genes
    for i in range( NG ):
        if sum( abs( folks[0]-folks[i]))< 0.01: cnt = cnt+1
    if cnt >= NG/2 or random.rand()<0.01: 
        rate = 0.9
        print 'Inbreeding warning'
    D = len( folks[0] ) # dim of vector
    for i in range( NG ):
        mx = folks[i].max()
        mn = folks[i].min()
        if mx > 0 :
            mx = mx * 1.05
        else:
            mx = mx * 0.95
        if mn>0:
            mn = mn * 0.95
        else:
            mn = mn * 1.05
        r = random.rand( D )
        hit = less(r,rate)
        r = (mx-mn)*r + mn
        folks[i] = (1.0-hit)*folks[i] + hit*r

def ShuffleMutate( genes, rate ):
    NG = len( genes )
    for i in range( NG ):
        K = len( genes[i] )
        for j in range( K ):
            if random.rand() < rate:
                r2 = int( random.rand()*K )
                a = genes[i][j]
                genes[i][j] = genes[i][r2]
                genes[i][r2] = a
                

# Code 11-11
def DriveGA( vecs, NG=10, DM=10 ):
    # generate initial folks
    folks = []
    for i in range( NG ):
        folks.append( 2 * random.rand(DM) - 1 )
    # iterate
    fcost = CostFunction( vecs, folks )
    ok = 1
    while ok:
        kids = CrossOver( folks, fcost )
        kcost = CostFunction( vecs, kids )
        Feud( folks, kids, fcost, kcost )
        Mutate( folks, 0.05 )
        fcost = CostFunction( vecs, folks )
        # get the best vector
        best = fcost.min()
        besti = fcost.argmin()
        if best < 0.1:
            ok = 0
        #print best
    return folks[besti]
