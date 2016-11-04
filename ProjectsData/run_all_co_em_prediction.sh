#!/bin/bash

time find . -type d -path "*ARC\\/*\.*\.*" -exec bash -c "Rscript {}/PredictingSpecificReleaseCOsmellEmergence.R &> {}.co_em.out" \;
