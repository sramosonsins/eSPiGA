App_Name=$1
App_Version=$2
curPath=$(pwd)
cd  $curPath/build
remoteUser="gitlab-agent"
remoteServer="updates.biotechvana.com"
remoteServer="192.168.1.81"
versionPath="${App_Version//./\/}"
release_date=$(date +%F)
echo $App_Version > VERSION
remote_folderPath="/home/web/updates/public/software/$App_Name/$versionPath"
echo $remote_folderPath
ssh -tt $remoteUser@$remoteServer "mkdir -p $remote_folderPath"
scp target/products/* $remoteUser@$remoteServer:$remote_folderPath
scp VERSION $remoteUser@$remoteServer:$remote_folderPath/.VERSION
ssh -tt $remoteUser@$remoteServer "cd /home/web/updates/public/software/  && ./.update $App_Name $versionPath "

remote_folderPath="/home/web/updates/public/e4.11/$App_Name/"
ssh -tt $remoteUser@$remoteServer "mkdir -p $remote_folderPath"
ssh -tt $remoteUser@$remoteServer "rm  -rf $remote_folderPath/repository"

## update json date for other site to read
#ssh -tt $remoteUser@$remoteServer "/home/web/updates/scripts/gpro_tools.py $App_Name $App_Version 11 $release_date"


scp -r target/repository $remoteUser@$remoteServer:$remote_folderPath
