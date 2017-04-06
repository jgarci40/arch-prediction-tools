#!/bin/bash

cat release_dirs_list.txt | xargs -I{} bash -c 'cd {}; Rscript ../../pred_lo_em_release_unchanged.R &> lo_em_unchanged.out'
