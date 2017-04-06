#!/bin/bash

find . -name "co_em_unchanged.out" -exec grep -Hn "L-AUC" {} \; | sed 's#./ProjectsData/##g' | sed 's#/# #g' | sed -E 's#co_em_unchanged.out:[0-9]+:\[1\]##g' | sed 's#ARC# ARC#g' | sed 's#Packages# PKG#g'
