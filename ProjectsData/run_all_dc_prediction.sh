#!/bin/bash

time find . -type d -path "*\.*\.*" -exec bash -c "./pred_dc_for_release.R -r {}/TrainingData.csv -e {}/TestData.csv &> {}.dc.out" \;
