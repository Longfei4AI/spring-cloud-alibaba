#!/bin/sh

# sh init-nacos-config.sh -t dev

while getopts ":h:p:g:t:u:w:" opt
do
  case $opt in
  h)
    host=$OPTARG
    ;;
  p)
    port=$OPTARG
    ;;
  g)
    group=$OPTARG
    ;;
  t)
    tenant=$OPTARG
    ;;
  u)
    username=$OPTARG
    ;;
  w)
    password=$OPTARG
    ;;
  ?)
    echo " USAGE OPTION: $0 [-h host] [-p port] [-g group] [-t tenant] [-u username] [-w password] "
    exit 1
    ;;
  esac
done

if [ -z ${host} ]; then
    host=localhost
fi
if [ -z ${port} ]; then
    port=8848
fi
if [ -z ${group} ]; then
    group="DEFAULT_GROUP"
fi
if [ -z ${tenant} ]; then
    tenant=""
fi
if [ -z ${username} ]; then
    username=""
fi
if [ -z ${password} ]; then
    password=""
fi

nacosAddr=$host:$port
contentType="content-type:application/json;charset=UTF-8"

echo "set nacosAddr=$nacosAddr"
echo "set group=$group"

tempLog=$(mktemp -u)
function initNamespace(){
    customNamespaceId="$1"
    namespaceName="$2"
    namespaceDesc="$3"
    curl -X POST -H "${contentType}" -G --data-urlencode "customNamespaceId=$customNamespaceId" --data-urlencode "namespaceName=$namespaceName" --data-urlencode "namespaceDesc=$namespaceDesc" "http://$nacosAddr/nacos/v1/console/namespaces" >"${tempLog}" 2>/dev/null
    if [ -z $(cat "${tempLog}") ]; then
      echo " Please check the cluster status. "
      exit 1
    fi
    result=$(cat "$tempLog")
    if [[ "$result" == "true" ]]; then
      echo "Set $1 successfully "
    else
      echo "Set $1 failure "
    fi
}

for ns in 'dev' 'staging' 'prod'; do
	initNamespace "${ns}" "${ns}" "Deploy for ${ns} environment"
done


failCount=0
tempLog=$(mktemp -u)
function addConfig() {
  dataId="$1"
  content="$2"
  type="$3"
  curl -X POST -H "${contentType}" -G --data-urlencode "dataId=$dataId" --data-urlencode "group=$group" --data-urlencode "type=$type" --data-urlencode "content=$content" --data-urlencode "tenant=$tenant" --data-urlencode "username=$username" --data-urlencode "password=$password" "http://$nacosAddr/nacos/v1/cs/configs" >"${tempLog}" 2>/dev/null
  if [ -z $(cat "${tempLog}") ]; then
    echo " Please check the cluster status. "
    exit 1
  fi
  result=$(cat "$tempLog")
  if [[ "$result" == "true" ]]; then
    echo "Set $1 successfully "
  else
    echo "Set $1 failure "
    failCount=`expr $failCount + 1`
  fi
}

count=0
for file in ../resources/*; do
  count=`expr $count + 1`
	key=${file#*/*/}
  value=$(cat $(dirname "$PWD")/resources/"$key")
  type=${key#*.}
  #echo "$key, $value, $count"
	addConfig "${key}" "${value}" "${type}"
done

echo "========================================================================="
echo " Complete initialization parameters,  total-count:$count ,  failure-count:$failCount "
echo "========================================================================="

if [ ${failCount} -eq 0 ]; then
	echo " Init nacos config finished. "
else
	echo " init nacos config fail. "
fi