#!/bin/bash

echo 'Models,Spearman,Recovery'; find . -name "*.cf.out" -exec grep -Hn "spearman" {} \; | grep -v "sig:NOT" | gsed -r 's#\./.*(Packages|ARC)/.*\..*\..*\.(.*)\.out:.*:\[1\] \"(.)-spearman: ([0-9\.]*) .*\"#\3,\4,\1#g' | sort --field-separator=',' -k 3,3 -k 1,1
