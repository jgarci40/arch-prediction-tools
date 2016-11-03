#!/bin/bash

find . -name "Predicting*.R" -exec perl -0777 -i.orig_pre_n-spearman -pe "s#(classification_glmnb.*?\{.*?print\(paste0\(\")( spearman)(: \", spearman, \")( spearman.p)(: \", spearman.p\)\))#\1N-spearman\3 N-spearman.p\5#s" {} \;
