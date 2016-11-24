@echo off
echo 当前目录：%cd%

echo 协议文件路径
set SOURCE_FOLDER=..\proto

echo Java文件生成路径
set JAVA_TARGET_PATH=..\..\..\XiaoGameServer\src\main\java

echo Java编译器路径
set JAVA_COMPILER_PATH=.\protoc.exe

echo 删除之前创建的文件
del %JAVA_TARGET_PATH%\org\redstone\protobuf\msg\*.java /f /s /q

echo 遍历所有proto文件，生成 Java 代码
for /f "delims=" %%i in ('dir /b "%SOURCE_FOLDER%\*.proto"') do (
    echo %JAVA_COMPILER_PATH% --java_out=%JAVA_TARGET_PATH% %SOURCE_FOLDER%\%%i
    %JAVA_COMPILER_PATH% --proto_path=%SOURCE_FOLDER% --java_out=%JAVA_TARGET_PATH% %SOURCE_FOLDER%\%%i
)
echo 协议生成完毕
pause