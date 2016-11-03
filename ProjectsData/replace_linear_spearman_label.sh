#!/bin/bash

find . -name "Predicting*.R" -exec perl -0777 -i.orig_pre_l-spearman -pe "s#(classification_linear.*?\{.*?print\(paste0\(\")( spearman)(: \", spearman, \")( spearman.p)(: \", spearman.p\)\))#\1L-spearman\3 L-spearman.p\5#s" {} \;
