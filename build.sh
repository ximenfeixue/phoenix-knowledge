version=$1
confile=`pwd`/sa-$version.properties
echo $confile
mvn clean compile package deploy -Dmaven.test.skip=true -P$version -Dsa.config.file=$confile
