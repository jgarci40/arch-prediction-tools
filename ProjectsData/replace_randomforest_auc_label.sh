#!/bin/bash

find . -name "Predicting*.R" -exec perl -0777 -i.orig_pre_f-auc -pe "s#(classification_randomForest.*?\{.*?print\(paste0\(\")( AUC)(:\", auc\)\))#\1F-AUC\3#s" {} \;
