#!/bin/bash

cat release_dirs_list.txt | xargs -I{} bash -c 'cd {}; Rscript ../../pred_df_release_unchanged.R &> df_unchanged.out'
