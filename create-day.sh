#!/bin/bash

# Description:
# A small script to create the Skeleton for a new day in AOC
#
# TODO
# - Verify if the day already exists
# - Create the test file
# - Rename the content of any String about Day1 with DayX


# Check if a parameter is provided
if [ "$#" -ne 1 ]; then
  echo "Usage: $0 <day>"
  exit 1
fi

# Read the parameter
DAY=$1

# Define the folder path
FOLDER_PATH="2024/src/main/java/info/jab/aoc/day$DAY"
FOLDER_PATH2="2024/src/test/java/info/jab/aoc/day$DAY"
FOLDER_PATH3="2024/src/test/resources/day$DAY"

# Create the folder
mkdir -p "$FOLDER_PATH"
mkdir -p "$FOLDER_PATH2"
mkdir -p "$FOLDER_PATH3"
cp 2016/src/main/java/info/jab/aoc/day1/Day1.java "$FOLDER_PATH/Day$DAY.java"
cp 2016/src/test/java/info/jab/aoc/day1/Day1Test.java "$FOLDER_PATH2/Day${DAY}Test.java"

# Confirm folder creation
if [ $? -eq 0 ]; then
    echo "Folder created: $FOLDER_PATH"
    echo "Folder created: $FOLDER_PATH2"
    echo "Folder created: $FOLDER_PATH3"
    echo "Day class created"
else
    echo "Failed to create folder: $FOLDER_PATH"
    exit 1
fi
