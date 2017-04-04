#!/bin/bash

cat release_dirs_list.txt | xargs -I{} bash -c 'cd {}; Rscript ../../pred_dc_release_unchanged.R &> dc_unchanged.out'
