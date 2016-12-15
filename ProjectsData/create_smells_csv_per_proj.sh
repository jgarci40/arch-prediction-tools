#!/bin/bash

find . -maxdepth 1 ! -path . -type d | sed 's#\./##g' | xargs -I% bash -c "find % -name '*.*.out' -not -name '*.*_em.out' -not -name '*.df.out' -not -name '*.cf.out' -exec grep -Hn 'AUC' {} \; | sed 's/-AUC:/,/g' | sed 's/\[1\]//g' | sed 's/\"//g' > %_smells.csv"
find . -maxdepth 1 -name "*_smells.csv" -exec sed -E -i '' 's#.*(Packages|ARC)/.*\..*\..*\.(.*)\.out:.*: (.*)#\3,\1-\2#' {} +
find . -maxdepth 1 -name "*_smells.csv" -exec gsed -i -e 's/\(.*\)/\U\1/' {} \;
find . -maxdepth 1 -name "*_smells.csv" -exec sed -i '' 's/PACKAGES/Pkg/g' {} \;
find . -maxdepth 1 -name "*_smells.csv" -exec gsed -i -e "1s/^/Models,AUC,Smell\\n/" {} \;
