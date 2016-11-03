#!/bin/bash

find . -name "Predicting*.R" -exec perl -0777 -i.orig_pre_n-auc -pe "s#(classification_glmnb.*?\{.*?print\(paste0\(\")( AUC)(:\", auc\)\))#\1N-AUC\3#s" {} \;
