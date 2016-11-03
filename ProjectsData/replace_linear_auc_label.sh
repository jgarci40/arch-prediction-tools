#!/bin/bash

find . -name "Predicting*.R" -exec perl -0777 -i.orig_pre_l-auc -pe "s#(classification_linear.*?\{.*?print\(paste0\(\")( AUC)(:\", auc\)\))#\1L-AUC\3#s" {} \;
