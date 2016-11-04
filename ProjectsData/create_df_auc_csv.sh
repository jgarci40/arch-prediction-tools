#!/bin/bash

echo "Models,AUC,Recovery"; find . -name "*.df.out" -exec grep -Hn "AUC" {} \; | sed 's/-AUC:/,/g' | sed 's/\[1\]//g' | gsed -r 's#\./.*(Packages|ARC)/.*\..*\..*\.(.*)\.out:.*: \"(.*)\"#\3,\1#g' | awk '{print toupper($0)}' | sed 's/PACKAGES/Packages/g' | sort --field-separator=',' --key=3
