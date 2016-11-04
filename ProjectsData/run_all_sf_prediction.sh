#!/bin/bash

time find . -type d -path "*ARC\\/*\.*\.*" -exec bash -c "Rscript {}/PredictingSpecificReleaseSFsmell.R &> {}.sf.out" \;
