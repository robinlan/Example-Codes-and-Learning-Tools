# simann.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008


from numpy import random, zeros, dot, array

# Code 11-1
# Generate data
def GenVectors( D=10,N=9 ):
    # D is the dimension of the vectors
    # N is the number of vectors
    vecs = random.ranf( (N,D) )
    return vecs

# Code 11-2
def SumInner( vecs, target ):
    N = len( vecs )
    answ = zeros( N )
    for i in range( N ):
        answ[i] = abs(dot( vecs[i], target ))
    return answ.sum()

# Code 11-4
# Simulated Annealing demonstration (see text - chapter 11)
def RunAnn( vecs, D, temp=1.0, scaltmp=0.99 ):
    target = 2*random.rand( D )-1
    # compute the inner products for all vectors
    cost = SumInner( vecs, target )
    ok = 1
    costs100,i = [],0
    while ok:
        # make a small change
        guess = target + temp*(2*random.rand(D)-1)
        # compute the inner products for all vectors
        gcost = SumInner( vecs, guess )
        # if it is an improvement then keep the change
        if gcost < cost:
            target = guess + 0
            cost = gcost + 0
            #print cost, temp
        # lower the temperature
        if i % 100 ==0:
            costs100.append( cost )
        i+=1
        temp *= scaltmp
        if cost<0.1:
            ok= 0
    return target, array( costs100 )
