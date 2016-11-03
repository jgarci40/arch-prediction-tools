#!/bin/bash

find . -name "Predicting*.R" -exec perl -0777 -i.orig_pre_f-spearman -pe "s#(classification_randomForest.*?\{.*?print\(paste0\(\")( spearman)(: \", spearman, \")( spearman.p)(: \", spearman.p\)\))#\1F-spearman\3 F-spearman.p\5#s" {} \;
