# decidetree.py
# copyright (c) Jason M. Kinser 2008
# This code is intended for non-commercial, educational use.
# This code may not be used for commercial purposes without written permission from the author.
# Many routines in this file are found in:
#  "Python for Bioinformatics", J. Kinser,  Jones & Bartlett pub, 2008

from numpy import random, array, sqrt

# Code 14-26
# generate fake data
def FakeIt( N, M ):
    # N is the number of patients
    # M is the number of parameters
    # each parameter affects the chance of being sick
    prms = random.rand( M )
    data = []
    for i in range( N ):
        mylife = random.rand(M)
        temp = (mylife * prms).sum()/sqrt(M)
        sick = temp > 0.7
        data.append( (i, sick, mylife) )
    return data

# Code 14-27
# decision tree
# score the ability of a parameter to separate the data
def ScoreParam( data, prm ):
    # prm is the index of the parameter to score
    # separate the parameters according to sickness
    well, sick = [], []
    for d in data:
        if d[1] == False:
            well.append( d[2][prm] )
        else:
            sick.append( d[2][prm] )
    # convert to vectors and get stats
    well = array( well );   sick = array( sick )
    wellavg = well.mean();  wellstd = well.std()
    sickavg = sick.mean();  sickstd = sick.std()
    # find crossover
    if wellavg > sickavg:
        alpha = (wellavg-sickavg)/(wellstd+sickstd)
        x = sickavg + alpha * sickstd
    else:
        alpha = (sickavg-wellavg)/(wellstd+sickstd)
        x = sickavg - alpha * sickstd
    # score
    # count the sicks on the left side
    cnt = 0  # sick on left side
    tot = 0.  # total sick
    for d in data:
        if d[1]==True and d[2][prm]<x:
            cnt += 1
        if d[1]==True:
            tot += 1
    # if prm is perfect then cnt=0 or cnt=tot
    sc1 = max( (cnt/tot, (tot-cnt)/tot) )
    # count the healthy on the left side
    cnt = 0  # sick on left side
    tot = 0.  # total sick
    for d in data:
        if d[1]==False and d[2][prm]<x:
            cnt += 1
        if d[1]==False:
            tot += 1
    # if prm is perfect then cnt=0 or cnt=tot
    sc2 = max( (cnt/tot, (tot-cnt)/tot) )
    score = (sc1+sc2)/2
    return score, x

# make tree
def DecTree( data ):
    N = len( data ) # number of samples
    P = len( data[2] ) # number of parameters
    tree = []
    
