#!/bin/bash
######################################################
# Author: HangX-Ma
# Date: 2023.04.08
# Copyright (c) 2023 HangX-Ma (MContour)
# License: MIT License
#
# reference website: https://imshuai.com/using-javac
#
######################################################

# Change these variables to your own configuations
PROJECT_DIR=/home/mcontour/cs61b-sp21/your_project_name
OUTPUT_DIR=${PROJECT_DIR}/build
SOURCE_DIR=${PROJECT_DIR}/your_src_folder
LIB_DIR=${PROJECT_DIR}/your_lib_folder
# determine your run class
MAIN_CLASS=your_run_class

function clean() {
    if [ -d ${OUTPUT_DIR} ]; then
        rm -rf ${OUTPUT_DIR:?}/*
        echo "Build folder has been cleaned"
    fi
}

function build() {
    echo "Java project building ..."
    # create output folder
    if [ ! -d ${OUTPUT_DIR} ]; then
        mkdir -p ${OUTPUT_DIR}
        echo "Create a build folder at ${OUTPUT_DIR}"
    fi

    # clean build output directory
    clean

    # prepare arg files
    find ${SOURCE_DIR} -name "*.java" > ${OUTPUT_DIR}/javaFiles.txt
    echo "-d ${OUTPUT_DIR}" > ${OUTPUT_DIR}/javaOptions.txt

    # compile
    javac -cp "${LIB_DIR}/*" @${OUTPUT_DIR}/javaOptions.txt @${OUTPUT_DIR}/javaFiles.txt

    # clean temp files
    rm -rf ${OUTPUT_DIR}/javaOptions.txt ${OUTPUT_DIR}/javaFiles.txt

    echo "Compelete compiling for java"
}

function run() {
    # run
    echo "Run ${MAIN_CLASS}"
    java -cp "${OUTPUT_DIR}:${LIB_DIR}/*" ${MAIN_CLASS}
}

function build_and_run() {
    build
    run
}

function help()
{
    echo
    echo "Usage:"
    echo " ./make.sh [sub-command]"
    echo
    echo "  - sub-command:  build|clean|run|BR"
    echo
    echo "Example:"
    echo "  ./make.sh build         --- build java project"
    echo "  ./make.sh clean         --- clean output folder"
    echo "  ./make.sh run           --- run program"
    echo "  ./make.sh BR            --- build and run program"
    echo
}

# main
if [ "$1" == "build" ]; then
    build
elif [ "$1" == "clean" ]; then
    clean
elif [ "$1" == "run" ]; then
    run
elif [ "$1" == "BR" ]; then
    build_and_run
else
    help
fi