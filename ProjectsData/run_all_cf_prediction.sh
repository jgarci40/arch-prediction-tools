#!/bin/bash

time find . -type d -path "*\.*\.*" -exec bash -c "./pred_cf_for_release.R -r {}/TrainingData.csv -e {}/TestData.csv &> {}.cf.out" \;
