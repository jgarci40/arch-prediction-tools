#!/bin/bash

time find . -type d -path "*\.*\.*" -exec bash -c "Rscript {}/PredictingSpecificReleaseLOsmell.R &> {}.lo.out" \;
