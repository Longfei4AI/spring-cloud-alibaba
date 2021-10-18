#!/usr/bin/env bash
echo '================== ready to install ================================'

read -r -p "Install redis? [Y/N/exit] " envConfirm
case $envConfirm in
    [yY][eE][sS]|[yY])
		echo "Start installing redis"
		docker-compose -f ../docker-compose/docker-compose-all.yml up -d redis
		echo '================== Redis installed ================================'
		    ;;
    [nN][oO]|[nN])
		echo "no"
       	;;
    [eE][xX][iI][tT]|[eE])
    echo "exit"
    exit 1
        ;;
    *)
		echo "Invalid input... exit"
		exit 1
		;;
esac

read -r -p "Install mysql? [Y/N/exit] " envConfirm
case $envConfirm in
    [yY][eE][sS]|[yY])
		echo "Start installing mysql"
		docker-compose -f ../docker-compose/docker-compose-all.yml up -d mysql
		echo '================== Mysql installed ================================'
		;;
    [nN][oO]|[nN])
    read -r -p "Initial db? [Y/N/exit] " envConfirm
    case $envConfirm in
        [yY][eE][sS]|[yY])
        echo ""
        echo "======================= Execute the following sql in DB ====================================="
        sleep 5
        sql=$(cat $(dirname "$PWD")/sql/apn-init.sql)
    		echo "$sql"
    		echo "============================================================================================="
    		echo "============================================================================================="
    		sleep 10
    		    ;;
        [nN][oO]|[nN])
    		echo "no"
           	;;
        [eE][xX][iI][tT]|[eE])
        echo "exit"
        exit 1
            ;;
        *)
    		echo "Invalid input... exit"
    		exit 1
    		;;
    esac
       	;;
    [eE][xX][iI][tT]|[eE])
    echo "exit"
    exit 1
        ;;
    *)
		echo "Invalid input... exit"
		exit 1
		;;
esac

read -r -p "Install mongodb? [Y/N/exit] " envConfirm
case $envConfirm in
    [yY][eE][sS]|[yY])
		echo "Start installing mongodb"
		docker-compose -f ../docker-compose/docker-compose-all.yml up -d mongodb
		echo '================== Mongodb installed ================================'
		    ;;
    [nN][oO]|[nN])
		echo "no"
       	;;
    [eE][xX][iI][tT]|[eE])
    echo "exit"
    exit 1
        ;;
    *)
		echo "Invalid input... exit"
		exit 1
		;;
esac

read -r -p "Install rabbitmq? [Y/N/exit] " envConfirm
case $envConfirm in
    [yY][eE][sS]|[yY])
		echo "Start installing rabbitmq"
		docker-compose -f ../docker-compose/docker-compose-all.yml up -d rabbitmq
		echo '================== Rabbitmq installed ================================'
		    ;;
    [nN][oO]|[nN])
		echo "no"
       	;;
    [eE][xX][iI][tT]|[eE])
    echo "exit"
    exit 1
        ;;
    *)
		echo "Invalid input... exit"
		exit 1
		;;
esac

read -r -p "Install nacos? [Y/N/exit] " envConfirm
case $envConfirm in
    [yY][eE][sS]|[yY])
    read -r -p "Mac with M1 chip? [Y/N/exit] " envConfirm
    case $envConfirm in
        [yY][eE][sS]|[yY])
    		echo "Start installing nacos for Mac with M1 chip"
    		docker-compose -f ../docker-compose/docker-compose-all.yml up -d nacos-m1
    		echo '================== Nacos installed ================================'
    		    ;;
        [nN][oO]|[nN])
    		echo "Start installing nacos"
        docker-compose -f ../docker-compose/docker-compose-all.yml up -d nacos
        echo '================== Nacos installed ================================'
           	;;
        [eE][xX][iI][tT]|[eE])
        echo "exit"
        exit 1
            ;;
        *)
    		echo "Invalid input... exit"
    		exit 1
    		;;
    esac
		;;
    [nN][oO]|[nN])
		echo "no"
       	;;
    [eE][xX][iI][tT]|[eE])
    echo "exit"
    exit 1
        ;;
    *)
		echo "Invalid input... exit"
		exit 1
		;;
esac

read -r -p "Install zipkin? [Y/N/exit] " envConfirm
case $envConfirm in
    [yY][eE][sS]|[yY])
		echo "Start installing zipkin"
		docker-compose -f ../docker-compose/docker-compose-all.yml up -d zipkin
		echo '================== Zipkin installed ================================'
		    ;;
    [nN][oO]|[nN])
		echo "no"
       	;;
    [eE][xX][iI][tT]|[eE])
    echo "exit"
    exit 1
        ;;
    *)
		echo "Invalid input... exit"
		exit 1
		;;
esac

read -r -p "Install sentinel? [Y/N/exit] " envConfirm
case $envConfirm in
    [yY][eE][sS]|[yY])
		read -r -p "Mac with M1 chip? [Y/N/exit] " envConfirm
    case $envConfirm in
        [yY][eE][sS]|[yY])
    		echo "Start installing sentinel for Mac with M1 chip"
    		docker-compose -f ../docker-compose/docker-compose-all.yml up -d sentinel-m1
    		echo '================== Sentinel installed ================================'
    		    ;;
        [nN][oO]|[nN])
    		echo "Start installing sentinel"
        docker-compose -f ../docker-compose/docker-compose-all.yml up -d sentinel
        echo '================== Sentinel installed ================================'
           	;;
        [eE][xX][iI][tT]|[eE])
        echo "exit"
        exit 1
            ;;
        *)
    		echo "Invalid input... exit"
    		exit 1
    		;;
    esac
		    ;;
    [nN][oO]|[nN])
		echo "no"
       	;;
    [eE][xX][iI][tT]|[eE])
    echo "exit"
    exit 1
        ;;
    *)
		echo "Invalid input... exit"
		exit 1
		;;
esac

read -r -p "Initial nacos configuration? (Nacos must be running!) [Y/N/exit] " envConfirm
case $envConfirm in
    [yY][eE][sS]|[yY])
		echo "Start setting up configuration"
		#for i in {1..40}
    #do
    #  sleep 0.2
    #  echo '=>\c'
    #done
    sleep 3
    echo ''

    sh init-nacos-config.sh -t dev

		echo '================== configuration was set up================================'
		    ;;
    [nN][oO]|[nN])
		echo "no"
       	;;
    [eE][xX][iI][tT]|[eE])
    echo "exit"
    exit 1
        ;;
    *)
		echo "Invalid input... exit"
		exit 1
		;;
esac

read -r -p "Install seata? [Y/N/exit] " envConfirm
case $envConfirm in
    [yY][eE][sS]|[yY])
    read -r -p "Mac with M1 chip? [Y/N/exit] " envConfirm
    case $envConfirm in
        [yY][eE][sS]|[yY])
    		echo "Start installing seata for Mac with M1 chip"
    		docker-compose -f ../docker-compose/docker-compose-all.yml up -d seata-m1
    		echo '================== Seata installed ================================'
    		    ;;
        [nN][oO]|[nN])
    		echo "Start installing seata"
        docker-compose -f ../docker-compose/docker-compose-all.yml up -d seata
        echo '================== Seata installed ================================'
           	;;
        [eE][xX][iI][tT]|[eE])
        echo "exit"
        exit 1
            ;;
        *)
    		echo "Invalid input... exit"
    		exit 1
    		;;
    esac
		;;
    [nN][oO]|[nN])
		echo "no"
       	;;
    [eE][xX][iI][tT]|[eE])
    echo "exit"
    exit 1
        ;;
    *)
		echo "Invalid input... exit"
		exit 1
		;;
esac

echo ''
echo '================== visit nacos ================================'
echo http://localhost:8848/nacos
echo '================== visit nacos ================================'
echo ''

echo '================== visit zipkin ================================'
echo http://localhost:9411/zipkin
echo '================== visit zipkin ================================'
echo ''

echo '================== visit sentinel ================================'
echo http://localhost:8090/#/dashboard/home
echo '================== visit sentinel ================================'
echo ''

echo '================== visit rabbitmq ================================'
echo http://localhost:15672/
echo '================== visit rabbitmq ================================'
echo ''
