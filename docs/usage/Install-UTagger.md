--------

[목차로 이동](./index.md)

--------

# UTagger 2018년 10월 31일자 설치

- [윈도우의 경우](#windows)
- [리눅스의 경우](#linux)

## Windows
다음과 같은 과정을 따라 설치하실 수 있습니다. (64비트만 지원)

1. [울산대학교 한국어처리연구실 홈페이지](http://nlplab.ulsan.ac.kr/doku.php?id=start)에 접속하여, [UTagger 파이썬 공개 2018(윈도우, 우분투, 센토스)](ftp://203.250.77.242/utagger%20delay%202018%2010%2031.zip)라고 적힌 파일을 다운받습니다.

1. 적당한 위치에 압축을 해제합니다. 압축을 해제한 위치를 `C:\UNZIPPED\PLACE`라고 합시다. 그러면 다음과 같은 상태일겁니다.
   
    * `C:\UNZIPPED\PLACE` 폴더 하위에
    
        * `Hlxcfg.txt`: 설정 파일입니다. 위치를 기억하세요. 
        
        * `bin`: 실행 파일과 라이브러리 파일을 포함하는 폴더입니다.
        
            * `bin\UTaggerR64.dll`: 윈도우용 라이브러리 DLL파일입니다. 이 파일의 위치를 절대경로로 기억하십시오.
            
            * `bin\실행 안될시 설치 c2017재배포64비트 VC_redist.x64.exe`: 만약 Visual Studio 2017 C++가 설치되어 있지 않다면, 이 파일을 실행해서 필요한 라이브러리를 설치하세요.
        
        * `udic4_3`: 사전 파일을 포함하는 폴더입니다.

1. 마지막으로 `C:\UNZIPPED\PLACE\Hlxcfg.txt`를 열어 다음과 같이 수정합니다.
    
    * 파일을 열고 닫을 때에는 반드시 `EUC-KR` 인코딩으로 열고 저장해야 합니다. 
    
    * 파일의 11행 `HLX_DIR ../udic4_3`을 사전 디렉터리를 가리키는 절대경로로 수정합니다. 즉 다음과 같이 변경되어야 합니다.
    ```text
    HLX_DIR C:\UNZIPPED\PLACE\udic4_3\
    ```

1. 축하합니다. 설치가 끝났습니다. 이제 UTagger를 사용할 수 있습니다. 언어에 따라 UTagger의 설치 위치를 다음과 같이 지정하세요.

    * Kotlin의 경우
    ```kotlin
    import kr.bydelta.koala.utagger.UTagger
   
    UTagger.setPath("라이브러리 파일 절대경로", "설정 파일 절대경로")
    ```
   
    * Java의 경우
    ```java
    import kr.bydelta.koala.utagger.UTagger;
   
    UTagger.setPath("라이브러리 파일 절대경로", "설정 파일 절대경로");
    ```
   
    * Scala의 경우
    ```scala
    import kr.bydelta.koala.utagger.UTagger
   
    UTagger.setPath("라이브러리 파일 절대경로", "설정 파일 절대경로")
    ```
   
    * Python의 경우
    ```python3
    from koalanlp.proc import UTagger
   
    UTagger.setPath("라이브러리 파일 절대경로", "설정 파일 절대경로")
    ```
   
    * JavaScript의 경우
    ```ecmascript 6
    const {UTagger} = require('koalanlp/proc');
   
    UTagger.setPath("라이브러리 파일 절대경로", "설정 파일 절대경로");
    ```
 
## Linux
다음과 같은 과정을 따라 설치하실 수 있습니다. (우분투 18.04와 CentOS 7.5.1804를 기준으로 빌드되어 있다고 합니다.)

1. 우분투의 경우는 GCC 7.3.0, libboost 1.65.1이 설치되어 있어야하고, CentOS의 경우는 GCC 7.3.1, libboost 1.53이 설치되어 있어야 합니다.

    * 대부분의 경우 기본 설정이 충족하지만, 설치가 안 된 경우 apt와 yum, dnf 등을 사용하여 설치합니다.

1. [울산대학교 한국어처리연구실 홈페이지](http://nlplab.ulsan.ac.kr/doku.php?id=start)에 접속하여, [UTagger 파이썬 공개 2018(윈도우, 우분투, 센토스)](ftp://203.250.77.242/utagger%20delay%202018%2010%2031.zip)라고 적힌 파일을 다운받습니다.

1. 적당한 위치에 압축을 해제합니다. 압축을 해제한 위치를 `/PATH/WHERE/UNZIPPED/`라고 합시다. 그러면 다음과 같은 상태일겁니다.
   
    * `/PATH/WHERE/UNZIPPED/` 디렉터리 하위에
    
        * `./Hlxcfg.txt`: 설정 파일입니다. 설정 파일의 위치를 절대경로로 기억하세요.
        
        * `./bin`: 실행 파일과 라이브러리 파일을 포함하는 디렉터리입니다.
        
            * `./bin/UTagger.so`: 우분투 18.04용 라이브러리 파일입니다. 이 파일의 위치를 절대경로로 기억하십시오.
            
            * `./bin/UTagger centos.so`: CentOS 7.5용 라이브러리 파일입니다. 이 파일의 위치를 절대경로로 기억하십시오. (띄어쓰기가 있음에 주의)
        
        * `./udic4_3`: 사전 파일을 포함하는 디렉터리입니다.

1. 라이브러리 파일이 있는지 확인합니다.

1. 마지막으로 `/PATH/WHERE/UNZIPPED/Hlxcfg.txt`를 열어 다음과 같이 수정합니다.
    
    * 파일을 열고 닫을 때에는 반드시 `EUC-KR` 인코딩으로 열고 저장해야 합니다. 
    
    * 파일의 11행 `HLX_DIR ../udic4_3`을 사전 디렉터리를 가리키는 절대경로로 수정합니다. 즉 다음과 같이 변경되어야 합니다.
    ```text
    HLX_DIR /PATH/WHERE/UNZIPPED/udic4_3/
    ```

1. 축하합니다. 설치가 끝났습니다. 이제 UTagger를 사용할 수 있습니다. 언어에 따라 UTagger의 설치 위치를 다음과 같이 지정하세요.

    * Kotlin의 경우
    ```kotlin
    import kr.bydelta.koala.utagger.UTagger
   
    UTagger.setPath("라이브러리 파일 절대경로", "설정 파일 절대경로")
    ```
   
    * Java의 경우
    ```java
    import kr.bydelta.koala.utagger.UTagger;
   
    UTagger.setPath("라이브러리 파일 절대경로", "설정 파일 절대경로");
    ```
   
    * Scala의 경우
    ```scala
    import kr.bydelta.koala.utagger.UTagger
   
    UTagger.setPath("라이브러리 파일 절대경로", "설정 파일 절대경로")
    ```
   
    * Python의 경우
    ```python3
    from koalanlp.proc import UTagger
   
    UTagger.setPath("라이브러리 파일 절대경로", "설정 파일 절대경로")
    ```
   
    * JavaScript의 경우
    ```ecmascript 6
    const {UTagger} = require('koalanlp/proc');
   
    UTagger.setPath("라이브러리 파일 절대경로", "설정 파일 절대경로");
    ```
 