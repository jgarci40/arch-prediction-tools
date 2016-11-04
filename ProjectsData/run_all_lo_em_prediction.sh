#!/bin/bash

time find . -type d -path "*\/*\.*\.*" -exec bash -c "Rscript {}/PredictingSpecificReleaseLOsmellEmergence.R &> {}.lo_em.out" \;
