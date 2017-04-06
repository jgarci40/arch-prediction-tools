#!/bin/bash

cat release_dirs_list.txt | xargs -I{} bash -c 'cd {}; Rscript ../../pred_co_release_unchanged.R &> co_unchanged.out'
