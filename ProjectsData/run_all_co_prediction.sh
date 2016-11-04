#!/bin/bash

time find . -type d -path "*ARC\\/*\.*\.*" -exec bash -c "Rscript {}/PredictingSpecificReleaseCOsmell.R &> {}.co.out" \;
