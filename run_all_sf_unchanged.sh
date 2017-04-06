#!/bin/bash

cat release_dirs_list.txt | xargs -I{} bash -c 'cd {}; Rscript ../../pred_sf_release_unchanged.R &> sf_unchanged.out'
