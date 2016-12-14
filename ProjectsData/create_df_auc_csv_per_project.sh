#!/bin/bash

find . -maxdepth 1 ! -path . -type d | sed 's#\./##g' | xargs -I[] bash -c "find [] -name '*.df.out' -exec grep -Hn 'AUC' {} \; | sed 's/-AUC:/,/g' | sed 's/\[1\]//g' | gsed -r 's#.*(Packages|ARC)/.*\..*\..*\.(.*)\.out:.*: \"(.*)\"#\3,\1#g' | gsed 's/PACKAGES/Packages/g' > []_df_auc.csv"
find . -maxdepth 1 -name "*_df_auc.csv" -exec gsed -i -e "1s/^/Models,AUC,Recovery\\n/" {} \;
