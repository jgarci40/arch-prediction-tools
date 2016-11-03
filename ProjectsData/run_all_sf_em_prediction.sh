#!/bin/bash

time find . -type d -path "*ARC\\/*\.*\.*" -exec bash -c "./pred_sf_em_for_release.R -r {}/TrainingData.csv -e {}/TestData.csv &> {}.sf_em.out" \;
