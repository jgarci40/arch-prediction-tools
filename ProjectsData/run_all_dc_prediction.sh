#!/bin/bash

time find . -type d -path "*\.*\.*" -exec bash -c "Rscript {}/PredictingSpecificReleaseDCsmell.R &> {}.dc.out" \;
