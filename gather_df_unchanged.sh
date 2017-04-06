#!/bin/bash

find . -name "df_unchanged.out" -exec grep -Hn "N-AUC\|L-AUC\|N-spearman\|L-spearman" {} \; | sed 's#./ProjectsData/##g' | sed 's#/# #g' | sed -E 's#df_unchanged.out:[0-9]+:\[1\]##g' | sed 's#ARC# ARC#g' | sed 's#Packages# PKG#g'
