#!/bin/bash

echo "Creating files for Assert test cases:"

ROOT=${SOG}/tmp/assert

if [ ! -d ${ROOT} ] 
then
    echo "Creating tmp directory ${ROOT}"
    mkdir ${ROOT}
fi

READABLE_FILE=${ROOT}/readable_file
if [ ! -f ${READABLE_FILE} ]
then
    echo "Creating ${READABLE_FILE}"
    touch ${READABLE_FILE}
fi
chmod +r ${READABLE_FILE}

UNREADABLE_FILE=${ROOT}/unreadable_file
if [ ! -f ${UNREADABLE_FILE} ]
then
    echo "Creating ${UNREADABLE_FILE}"
    touch ${UNREADABLE_FILE}
fi
chmod -r ${UNREADABLE_FILE}

WRITEABLE_FILE=${ROOT}/writeable_file
if [ ! -f ${WRITEABLE_FILE} ]
then
    echo "Creating ${WRITEABLE_FILE}"
    touch ${WRITEABLE_FILE}
fi
chmod +w ${WRITEABLE_FILE}

UNWRITEABLE_FILE=${ROOT}/unwriteable_file
if [ ! -f ${UNWRITEABLE_FILE} ]
then
    echo "Creating ${UNWRITEABLE_FILE}"
    touch ${UNWRITEABLE_FILE}
fi
chmod -w ${UNWRITEABLE_FILE}


READABLE_DIR=${ROOT}/readable_dir
if [ ! -d ${READABLE_DIR} ]
then
    echo "Creating ${READABLE_DIR}"
    mkdir ${READABLE_DIR}
fi
chmod +r ${READABLE_DIR}

UNREADABLE_DIR=${ROOT}/unreadable_dir
if [ ! -d ${UNREADABLE_DIR} ]
then
    echo "Creating ${UNREADABLE_DIR}"
    mkdir ${UNREADABLE_DIR}
fi
chmod -r ${UNREADABLE_DIR}

WRITEABLE_DIR=${ROOT}/writeable_dir
if [ ! -d ${WRITEABLE_DIR} ]
then
    echo "Creating ${WRITEABLE_DIR}"
    mkdir ${WRITEABLE_DIR}
fi
chmod +w ${WRITEABLE_DIR}

UNWRITEABLE_DIR=${ROOT}/unwriteable_dir
if [ ! -d ${UNWRITEABLE_DIR} ]
then
    echo "Creating ${UNWRITEABLE_DIR}"
    mkdir ${UNWRITEABLE_DIR}
fi
chmod -w ${UNWRITEABLE_DIR}

