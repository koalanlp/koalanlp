#!/usr/bin/env bash

ACTION=$1

extract_version()
{
    if [ -f './gradle.properties' ]; then
        JAR_VER=$(cat gradle.properties | grep "version=" | cut -d= -f2 | cut -d- -f1)
        JAR_VER_MAJOR=$(echo $JAR_VER | cut -d. -f1)
        JAR_VER_MINOR=$(echo $JAR_VER | cut -d. -f2)
        JAR_VER_INCRM=$(echo $JAR_VER | cut -d. -f3)
        JAR_VER_CURRENT=$JAR_VER_MAJOR.$JAR_VER_MINOR.$JAR_VER_INCRM
    elif [ -f './build.gradle' ]; then
        JAR_VER=$(cat build.gradle | grep "version " | cut -d\" -f2 | cut -d- -f1)
        JAR_VER_MAJOR=$(echo $JAR_VER | cut -d. -f1)
        JAR_VER_MINOR=$(echo $JAR_VER | cut -d. -f2)
        JAR_VER_INCRM=$(echo $JAR_VER | cut -d. -f3)
        JAR_VER_CURRENT=$JAR_VER_MAJOR.$JAR_VER_MINOR.$JAR_VER_INCRM
    fi
}

add_incremental_ver()
{
    JAR_VER_NEXT=$JAR_VER_MAJOR.$JAR_VER_MINOR.$(($JAR_VER_INCRM + 1))
}

add_minor_ver()
{
    JAR_VER_NEXT=$JAR_VER_MAJOR.$(($JAR_VER_MINOR + 1)).0
}

set_version()
{
    if [ -f 'gradle.properties' ]; then
        cat gradle.properties | sed -e 's/version=\s*.*/version='$1'/g' > gradle.properties.new
        rm gradle.properties
        mv gradle.properties.new gradle.properties
        git add gradle.properties
    elif [ -f 'build.gradle' ]; then
        cat build.gradle | sed -e 's/^version\s*.*/version "'$1'"/g' > build.gradle.new
        rm build.gradle
        mv build.gradle.new build.gradle
        git add build.gradle
    fi    
}

set_publish_version()
{
    cat gradle.properties | sed -e 's/corePublishedVer=\s*.*/corePublishedVer='$1'/g' > gradle.properties.new
    rm gradle.properties
    mv gradle.properties.new gradle.properties
    git add gradle.properties
}

read_module_name()
{
    MODULE_NAME=$(cat settings.gradle | grep ":$1" | cut -d\' -f4)
}

ask_proceed()
{
    read -p "Proceed $1 [Y/n/p]? " YN
    if [ "${YN,,}" = "n" ]; then
        exit 0
    fi
}


case ${ACTION} in
    help)
        echo -n ./release.sh "[help"

        MODULES=`ls */build.gradle | cut -d/ -f1`
        for MODULE in $MODULES
        do
            echo -n "|$MODULE"
        done

        echo "]"
        ;;
    core)
        extract_version
        MODULES=`ls */build.gradle | cut -d/ -f1`

        # reset version code
        echo BUILD $JAR_VER_CURRENT
        ask_proceed "SET VERSION"
        if [ "${YN,,}" != "p" ]; then
            set_version $JAR_VER_CURRENT

            for MODULE in $MODULES
            do
                if [ "$MODULE" != "core" ]; then
                    cd $MODULE
                    echo SET $JAR_VER_CURRENT to $MODULE
                    set_version $JAR_VER_CURRENT
                    cd ..
                fi
            done
        fi

        # Upload core and close, because of the dependencies
        ask_proceed "UPLOAD CORE"
        if [ "${YN,,}" != "p" ]; then
            ./gradlew clean :koalanlp-core:uploadArchives :koalanlp-core:install --info
        fi

        ask_proceed "RELEASE CORE"
        if [ "${YN,,}" != "p" ]; then
            ./gradlew closeAndReleaseRepository
        fi

        ask_proceed "TEST PACKAGES"
        if [ "${YN,,}" != "p" ]; then
            set_publish_version $JAR_VER_CURRENT

            for MODULE in $MODULES
            do
                if [ "$MODULE" != "core" ]; then
                    read_module_name $MODULE
                    echo ">>"$MODULE_NAME
                    ./gradlew $MODULE_NAME:check --info
                fi
            done
        fi

        ask_proceed "UPLOAD"
        if [ "${YN,,}" != "p" ]; then
            for MODULE in $MODULES
            do
                if [ "$MODULE" != "core" ]; then
                    read_module_name $MODULE
                    echo ">>"$MODULE_NAME
                    ./gradlew $MODULE_NAME:uploadArchives --info
                fi
            done
        fi

        echo UPLOAD FINISHED
        ask_proceed "RELEASE"
        if [ "${YN,,}" != "p" ]; then
            ./gradlew closeAndReleaseRepository
        fi

        ask_proceed "SET NEXT"
        if [ "${YN,,}" != "p" ]; then
            # GENERATE DOC
            ./doc.sh
            git tag v$JAR_VER_CURRENT

            # SET NEXT
            add_incremental_ver
            set_version "$JAR_VER_NEXT-SNAPSHOT"
            for MODULE in $MODULES
            do
                if [ "$MODULE" != "core" ]; then
                    cd $MODULE
                    set_version "$JAR_VER_NEXT-SNAPSHOT"
                    cd ..
                fi
            done
        fi

        ask_proceed "COMMIT"
        if [ "${YN,,}" != "p" ]; then
            git commit -a -m "inital commit of v$JAR_VER_NEXT"
            git push origin master
            git push --tags
        fi
        ;;
    *)
        if [ -f "$ACTION/build.gradle" ]; then
            read_module_name $ACTION
            cd $ACTION
            extract_version

            # reset version code
            echo BUILD $JAR_VER_CURRENT for $ACTION
            ask_proceed "SET VERISON"
            if [ ${YN,,} != "p" ]; then
                set_version $JAR_VER_CURRENT
            fi

            cd ..
            ask_proceed "UPLOAD"
            if [ ${YN,,} != "p" ]; then
                ./gradlew $MODULE_NAME:clean $MODULE_NAME:uploadArchives --info
            fi

            ask_proceed "RELEASE"
            if [ ${YN,,} != "p" ]; then
                ./gradlew closeAndReleaseRepository
            fi

            cd $ACTION
            ask_proceed "SET NEXT"
            if [ ${YN,,} != "p" ]; then
                add_incremental_ver
                set_version "$JAR_VER_NEXT-SNAPSHOT"
            fi
            cd ..

            ask_proceed "COMMIT"
            if [ ${YN,,} != "p" ]; then
                git commit -a -m "inital commit of v$JAR_VER_NEXT of $ACTION"
                git push
            fi
        else
            echo -n ./release.sh "[help"

            MODULES=`find */build.gradle -type f | cut -d/ -f1`
            for MODULE in $MODULES
            do
                echo -n "|$MODULE"
            done

            echo "]"
        fi
    ;;
esac
