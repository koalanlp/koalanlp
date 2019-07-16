--------

[목차로 이동](./index.md)

--------

# KoalaNLP 사용 준비

이 문서에서는 각 플랫폼별 사용 준비를 위한 Java JDK 설치 과정을 설명합니다.


- [Windows 10+](#Windows)
- [Mac OSX](#Mac)
- [Linux](#Linux)

## Windows

1. 설치 과정의 편의를 위해서 [Chocolately](https://chocolatey.org/install)를 설치합니다. 

   시작 메뉴를 누른 다음 '명령 프롬프트(Command prompt)'를 __관리자 권한으로 실행__ 합니다.
   
   그리고 Chocolately 웹페이지에 적힌 다음 명령을 붙여넣고 ENTER를 누릅니다.
   
   ```commandline
   @"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -InputFormat None -ExecutionPolicy Bypass -Command "iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))" && SET "PATH=%PATH%;%ALLUSERSPROFILE%\chocolatey\bin"
   ```

2. 사용하려는 언어에 상관 없이 Java JDK 설치가 필요합니다. 명령 프롬프트에서 다음을 입력하고 ENTER를 누릅니다.

   ```commandline
   choco install openjdk --version 12.0.0 -y
   ```
   
3. Java를 사용하지 않고 다른 언어를 사용할 계획이라면, 해당 언어를 설치합니다.

    * Python 3.6.8의 설치는 다음 명령으로 가능합니다. (마찬가지로 명령 프롬프트에서 입력합니다.)
    
    ```commandline
    choco install python --version 3.6.8 -y
    ```
    
    * Node.JS 12.6.0의 설치는 다음 명령으로 가능합니다. (마찬가지로 명령 프롬프트에서 입력합니다.)
    
    ```commandline
    choco install nodejs --version 12.6.0 -y
    ```
    
    * Scala 2.12 또는 2.13의 설치는 [홈페이지](http://scala-lang.org)에서 다운받아 실행해야 합니다.

4. 이제 사전 설치 과정이 모두 끝났습니다. [설치하기](./installation.md)로 이동하여 KoalaNLP를 설치해주세요. 

## Mac

1. 설치 과정의 편의를 위해서 [Homebrew](https://brew.sh/index_ko)를 설치합니다. 

   터미널(Terminal) 앱을 실행합니다.
   
   Homebrew 웹페이지의 "Homebrew 설치하기" 아래에 적힌 다음 명령을 붙여넣고 ENTER를 누릅니다.

    ```bash
    /usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
    ``   

2. 사용하려는 언어에 상관 없이 Java JDK 설치가 필요합니다. 터미널에서 다시 다음을 입력하고 ENTER를 눌러 실행합니다.

    ```bash
    brew cask info java
    brew cask install java
    ```
    
3. Java를 사용하지 않고 다른 언어를 사용할 계획이라면, 해당 언어를 설치합니다.

    * Python 3.6.8의 설치는 다음 명령으로 가능합니다. (마찬가지로 터미널에서 입력합니다.)
    
    ```bash
    brew install python3
    brew install --ignore-dependencies https://raw.githubusercontent.com/Homebrew/homebrew-core/f2a764ef944b1080be64bd88dca9a1d80130c558/Formula/python.rb --ignore-dependencies
    ```
    
    * Node.JS 12.6.0의 설치는 다음 명령으로 가능합니다. (마찬가지로 터미널에서 입력합니다.)
    
    ```bash
    brew install nodejs
    ```
    
    * Scala 2.13.0의 설치는 다음 명령으로 가능합니다. (마찬가지로 터미널에서 입력합니다.)
    
    ```bash
    brew install scala
    ```

4. 이제 사전 설치 과정이 모두 끝났습니다. [설치하기](./installation.md)로 이동하여 KoalaNLP를 설치해주세요.

## Linux

1. 사용하려는 언어에 상관 없이 Java JDK 설치가 필요합니다. 다음을 실행하여 자바를 설치합니다.

   (Debian 계열의 경우, JDK 9를 설치한다고 가정했을 때)
   ```bash
   sudo apt install openjdk-9-jdk
   ```
   
   (RedHat 계열의 경우, JDK 8를 설치한다고 가정했을 때)
   ```bash
   sudo yum install java-1.8.0-openjdk
   ```
   
2. Java를 사용하지 않고 다른 언어를 사용할 계획이라면, 해당 언어를 설치합니다.

3. 이제 사전 설치 과정이 모두 끝났습니다. [설치하기](./installation.md)로 이동하여 KoalaNLP를 설치해주세요.