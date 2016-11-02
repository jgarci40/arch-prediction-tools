#!/bin/bash

time find . -type d -path "*ARC\\/*\.*\.*" -exec bash -c "./pred_co_for_release.R -r {}/TrainingData.csv -e {}/TestData.csv &> {}.co.out" \;
