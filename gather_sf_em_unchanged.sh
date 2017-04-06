#!/bin/bash

find . -name "sf_em_unchanged.out" -exec grep -Hn "N-AUC\|L-AUC\|F-AUC" {} \; | sed 's#./ProjectsData/##g' | sed 's#/# #g' | sed -E 's#sf_em_unchanged.out:[0-9]+:\[1\]##g' | sed 's#ARC# ARC#g' | sed 's#Packages# PKG#g'
