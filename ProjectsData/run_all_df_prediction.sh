#!/bin/bash

time find . -type d -path "*\.*\.*" -exec bash -c "Rscript {}/PredictingSpecificReleaseDefects.R &> {}.df.out" \;
