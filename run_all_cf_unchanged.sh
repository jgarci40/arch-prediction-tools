#!/bin/bash

cat release_dirs_list.txt | xargs -I{} bash -c 'cd {}; Rscript ../../pred_cf_release_unchanged.R &> cf_unchanged.out'
