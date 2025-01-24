#!/bin/bash

#Parameters
SCRIPTPATH="$( cd -- "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
TARGET_DIRECTORY="$SCRIPTPATH/target"
PRODUCT=${1}
VERSION=${2}
DATE=`date +%Y-%m-%d`
TIME=`date +%H:%M:%S`
LOG_PREFIX="[$DATE $TIME]"

function printSignature() {
  cat $SCRIPTPATH/utils/ascii_art.txt
  echo
}
#Start the generator
printSignature

function printUsage() {
  echo -e "\033[1mUsage:\033[0m"
  echo "$0 [APPLICATION_NAME] [APPLICATION_VERSION]"
  echo
  echo -e "\033[1mOptions:\033[0m"
  echo "  -h (--help)"
  echo
  echo -e "\033[1mExample::\033[0m"
  echo "$0 wso2am 2.6.0"

}



#Argument validation
if [[ "$1" == "-h" ||  "$1" == "--help" ]]; then
    printUsage
    exit 1
fi
if [ -z "$1" ]; then
    echo "Please enter a valid application name for your application"
    echo
    printUsage
    exit 1
else
    echo "Application Name : $1"
fi
if [[ "$2" =~ [0-9]+.[0-9]+.[0-9]+ ]]; then
    echo "Application Version : $2"
else
    echo "Please enter a valid version for your application (fromat [0-9].[0-9].[0-9])"
    echo
    printUsage
    exit 1
fi

#Functions
go_to_dir() {
    pushd $1 >/dev/null 2>&1
}

log_info() {
    echo "${LOG_PREFIX}[INFO]" $1
}

log_warn() {
    echo "${LOG_PREFIX}[WARN]" $1
}

log_error() {
    echo "${LOG_PREFIX}[ERROR]" $1
}

#Main script
log_info "Installer generating process started."


deleteInstallationDirectory() {
    log_info "Cleaning $TARGET_DIRECTORY directory."
    rm -rf $TARGET_DIRECTORY

    if [[ $? != 0 ]]; then
        log_error "Failed to clean $TARGET_DIRECTORY directory" $?
        exit 1
    fi
}

createInstallationDirectory() {
    if [ -d ${TARGET_DIRECTORY} ]; then
        deleteInstallationDirectory
    fi
    mkdir $TARGET_DIRECTORY

    if [[ $? != 0 ]]; then
        log_error "Failed to create $TARGET_DIRECTORY directory" $?
        exit 1
    fi
}

copyDarwinDirectory(){
    createInstallationDirectory


    mkdir -p ${TARGET_DIRECTORY}/root/Applications
    mkdir -p ${TARGET_DIRECTORY}/root/Library/

    cp -r ../es.cragenomica.espiga/releng/es.cragenomica.espiga.product/target/products/es.cragenomica.espiga/macosx/cocoa/x86_64/eSPiGA.app ${TARGET_DIRECTORY}/root/Applications


    mkdir -p ${TARGET_DIRECTORY}/flat
    mkdir -p ${TARGET_DIRECTORY}/flat/${PRODUCT}.pkg
    cp -r $SCRIPTPATH/source/scripts ${TARGET_DIRECTORY}/
    cp -r $SCRIPTPATH/source/flat ${TARGET_DIRECTORY}
    cp  $SCRIPTPATH/source/PackageInfo ${TARGET_DIRECTORY}/flat/${PRODUCT}.pkg/

    cp  $SCRIPTPATH/source/uninstall.sh  ${TARGET_DIRECTORY}/root/Library/${PRODUCT}.uninstall.sh
    cp  $SCRIPTPATH/source/java-fix.sh  ${TARGET_DIRECTORY}/root/Library/${PRODUCT}.java-fix.sh

    chmod -R 765 ${TARGET_DIRECTORY}/scripts
    chmod -R 765 ${TARGET_DIRECTORY}/root/Library/
    chmod -R 755 ${TARGET_DIRECTORY}/flat
    chmod 755 ${TARGET_DIRECTORY}/flat/Distribution
}

replacePV() {
    #echo ${TARGET_DIRECTORY}/scripts/postinstall
    sed -i  -e 's/__VERSION__/'${VERSION}'/g' ${TARGET_DIRECTORY}/scripts/postinstall
    sed -i  -e 's/__PRODUCT__/'${PRODUCT}'/g' ${TARGET_DIRECTORY}/scripts/postinstall
    chmod -R 755 ${TARGET_DIRECTORY}/scripts/postinstall

    sed -i  -e 's/__VERSION__/'${VERSION}'/g' ${TARGET_DIRECTORY}/flat/Distribution
    sed -i  -e 's/__PRODUCT__/'${PRODUCT}'/g' ${TARGET_DIRECTORY}/flat/Distribution
    chmod -R 755 ${TARGET_DIRECTORY}/flat/Distribution

    sed -i  -e 's/__VERSION__/'${VERSION}'/g' ${TARGET_DIRECTORY}/flat/Resources/welcome.html
    sed -i  -e 's/__VERSION__/'${VERSION}'/g' ${TARGET_DIRECTORY}/flat/Resources/conclusion.html

    sed -i  -e 's/__PRODUCT__/'${PRODUCT}'/g' ${TARGET_DIRECTORY}/flat/Resources/welcome.html
    sed -i  -e 's/__PRODUCT__/'${PRODUCT}'/g' ${TARGET_DIRECTORY}/flat/Resources/conclusion.html

    chmod -R 755 ${TARGET_DIRECTORY}/flat/Resources/



    sed -i  -e 's/__VERSION__/'${VERSION}'/g' ${TARGET_DIRECTORY}/root/Library/${PRODUCT}.uninstall.sh
    sed -i  -e 's/__PRODUCT__/'${PRODUCT}'/g' ${TARGET_DIRECTORY}/root/Library/${PRODUCT}.uninstall.sh
    chmod -R 755 ${TARGET_DIRECTORY}/root/Library/${PRODUCT}.uninstall.sh

    sed -i  -e 's/__VERSION__/'${VERSION}'/g' ${TARGET_DIRECTORY}/root/Library/${PRODUCT}.java-fix.sh
    sed -i  -e 's/__PRODUCT__/'${PRODUCT}'/g' ${TARGET_DIRECTORY}/root/Library/${PRODUCT}.java-fix.sh
    chmod -R 755 ${TARGET_DIRECTORY}/root/Library/${PRODUCT}.java-fix.sh


    rm -rf ${TARGET_DIRECTORY}/package
    mkdir -p ${TARGET_DIRECTORY}/package
    chmod -R 755 ${TARGET_DIRECTORY}/package
}



buildPackage() {
    NUM_FILES=$(find ${TARGET_DIRECTORY}/root | wc -l)
    INSTALL_KB_SIZE=$(du -k -s ${TARGET_DIRECTORY}/root | awk '{print $1}')

    log_info "NUM_FILES $NUM_FILES"
    log_info "INSTALL_KB_SIZE $INSTALL_KB_SIZE"

    sed -i  -e 's/__NUM_FILES__/'${NUM_FILES}'/g' ${TARGET_DIRECTORY}/flat/${PRODUCT}.pkg/PackageInfo
    sed -i  -e 's/__INSTALL_KB_SIZE__/'${INSTALL_KB_SIZE}'/g' ${TARGET_DIRECTORY}/flat/${PRODUCT}.pkg/PackageInfo
    sed -i  -e 's/__VERSION__/'${VERSION}'/g'  ${TARGET_DIRECTORY}/flat/${PRODUCT}.pkg/PackageInfo
    sed -i  -e 's/__PRODUCT__/'${PRODUCT}'/g'  ${TARGET_DIRECTORY}/flat/${PRODUCT}.pkg/PackageInfo

    ( cd ${TARGET_DIRECTORY}/root && find . | cpio -o --format odc --owner 0:80 | gzip -c ) > ${TARGET_DIRECTORY}/flat/${PRODUCT}.pkg/Payload

    ( cd ${TARGET_DIRECTORY}/scripts && find . | cpio -o --format odc --owner 0:80 | gzip -c ) > ${TARGET_DIRECTORY}/flat/${PRODUCT}.pkg/Scripts


    mkbom -u 0 -g 80 ${TARGET_DIRECTORY}/root ${TARGET_DIRECTORY}/flat/${PRODUCT}.pkg/Bom


    ( cd ${TARGET_DIRECTORY}/flat && xar --compression none -cf ${TARGET_DIRECTORY}/package/${PRODUCT}.${VERSION}-macosx.cocoa.x86_64.pkg * )

}

copyDarwinDirectory
replacePV
buildPackage
