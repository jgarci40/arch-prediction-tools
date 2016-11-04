#!/bin/bash

time find . -type d -path "*\/*\.*\.*" -exec bash -c "Rscript {}/PredictingSpecificReleaseDCsmellEmergence.R &> {}.dc_em.out" \;
