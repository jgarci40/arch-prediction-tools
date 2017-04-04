#!/bin/bash

cat release_dirs_list.txt | xargs -I{} bash -c 'cd {}; Rscript ../../pred_dc_em_release_unchanged.R &> dc_em_unchanged.out'
