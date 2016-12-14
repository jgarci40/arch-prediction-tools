#!/bin/bash

find . -maxdepth 1 ! -path . -type d | sed 's#\./##g' | xargs -I[] bash -c "find [] -name '*.cf.out' -exec grep -Hn 'spearman' {} \; | grep -v 'sig:NOT' | gsed -r 's#.*(Packages|ARC)/.*\..*\..*\.(.*)\.out:.*:\[1\] \"(.)-spearman: ([0-9\.]*) .*\"#\3,\4,\1#g'  > []_cf_rank_auc.csv"
find . -maxdepth 1 -name "*_cf_rank_auc.csv" -exec gsed -i -e "1s/^/Models,Spearman,Recovery\\n/" {} \;
