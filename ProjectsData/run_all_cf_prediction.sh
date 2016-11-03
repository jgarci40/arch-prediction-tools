#!/bin/bash

time find . -type d -path "*\.*\.*" -exec bash -c "echo {}; Rscript {}/PredictingSpecificReleaseMQ.R" \;
