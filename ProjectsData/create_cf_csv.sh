#!/bin/bash

find . -name "*.cf.out" -exec grep -Hn "AUC" {} \; | sed 's/-AUC:/,/g' | sed 's/\[1\]//g' | gsed -r 's#\./.*(Packages|ARC)/.*\..*\..*\.(.*)\.out:.*: \"(.*)\"#\3,\1-\2#g' | awk '{print toupper($0)}' | sed 's/PACKAGES/PKG/g' | sort --field-separator=',' --key=3
