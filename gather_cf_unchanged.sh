#!/bin/bash

find . -name "cf_unchanged.out" -exec grep -Hn "N-AUC\|L-AUC\|F-AUC\|N-spearman\|L-spearman\|F-spearman" {} \; | sed 's#./ProjectsData/##g' | sed 's#/# #g' | sed -E 's#cf_unchanged.out:[0-9]+:\[1\]##g' | sed 's#ARC# ARC#g' | sed 's#Packages# PKG#g'
