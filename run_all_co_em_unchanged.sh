#!/bin/bash

cat release_dirs_list.txt | xargs -I{} bash -c 'cd {}; Rscript ../../pred_co_em_release_unchanged.R &> co_em_unchanged.out'
