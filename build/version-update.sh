## need to run this localing to update app version
App_Name="eSPiGA"
App_Name_folders="espiga"

curPath=$(pwd)
set -e
echo "Working on $curPath"
App_Version="1.1.0"

## get last version
## if no change then just push updates
## otherwise create new release as well
## buidling common projects

#./build/build_common.sh $App_Name $App_Version

# return again
cd $curPath/es.cragenomica.espiga
# change version
mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=${App_Version}.qualifier -Dtycho.mode=maven

## change RAP product too
#cd $curPath/es.cragenomica.espiga.rap/bundles/es.cragenomica.espiga.rap.ui
#mvn org.eclipse.tycho:tycho-versions-plugin:set-version -DnewVersion=${App_Version}.qualifier -Dtycho.mode=maven

