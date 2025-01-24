App_Name="eSPiGA"
App_Name_folders="espiga"

curPath=$(pwd)
set -e
echo "Working on $curPath"


## get last version
## if no change then just push updates
## otherwise create new release as well
## buidling common projects

#./build/build_common.sh $App_Name $App_Version

# return again
cd $curPath/es.cragenomica.espiga
# change version
#mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=${App_Version}.qualifier -Dtycho.mode=maven
# start build
mvn clean verify


sed -e 's/^/V/' bundles/es.cragenomica.espiga.ui/target/about.mappings > ../build/VERSION.sh

source ../build/VERSION.sh
echo "Build Finished with : $V1.$V0"
App_Version=$V1
## build mac installer
cd $curPath/mac_installer
./build.sh $App_Name $App_Version
cd $curPath/build
rm -rf target
mkdir -p target
mkdir -p target/products
#mkdir -p target

cp $curPath/es.cragenomica.espiga/releng/es.cragenomica.espiga.product/target/products/eSPiGA* target/products/
cp -r $curPath/es.cragenomica.espiga/releng/es.cragenomica.espiga.product/target/repository target/
cp $curPath/mac_installer/target/package/eSPiGA* target/products/

cd $curPath
#./build/publish.sh $App_Name $App_Version