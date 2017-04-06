#!/bin/bash

cat release_dirs_list.txt | xargs -I{} bash -c 'cd {}; Rscript ../../pred_sf_em_release_unchanged.R &> sf_em_unchanged.out'
