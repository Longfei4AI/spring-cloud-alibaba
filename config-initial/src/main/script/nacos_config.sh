#!/bin/sh

# sh nacos_config.sh -t dev

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

urlencode() {
  length="${#1}"
  i=0
  while [ $length -gt $i ]; do
    char="${1:$i:1}"
    case $char in
    [a-zA-Z0-9.~_-]) printf $char ;;
    *) printf '%%%02X' "'$char" ;;
    esac
    i=`expr $i + 1`
  done
}

failCount=0
tempLog=$(mktemp -u)
function addConfig() {
  dataId=`urlencode $1`
  content=`urlencode $2`
  type=`urlencode $3`
  curl -X POST -H "${contentType}" "http://$nacosAddr/nacos/v1/cs/configs?dataId=$dataId&group=$group&type=$type&content=$content&tenant=$tenant&username=$username&password=$password" >"${tempLog}" 2>/dev/null
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
  #echo "$key, $type, $count"
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