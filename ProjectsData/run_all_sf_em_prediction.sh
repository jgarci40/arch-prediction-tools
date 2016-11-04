#!/bin/bash

time find . -type d -path "*ARC\\/*\.*\.*" -exec bash -c "Rscript {}/PredictingSpecificReleaseSFsmellEmergence.R &> {}.sf_em.out" \;
