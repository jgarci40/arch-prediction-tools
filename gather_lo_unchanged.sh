#!/bin/bash

find . -name "lo_unchanged.out" -exec grep -Hn "N-AUC\|L-AUC\|F-AUC" {} \; | sed 's#./ProjectsData/##g' | sed 's#/# #g' | sed -E 's#lo_unchanged.out:[0-9]+:\[1\]##g' | sed 's#ARC# ARC#g' | sed 's#Packages# PKG#g'
