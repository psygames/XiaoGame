@echo off
echo 当前CMD默认目录：%cd%

echo 协议文件路径
set SOURCE_FOLDER=..\..\java\org\redstone\protobuf\proto
echo %SOURCE_FOLDER%

echo C#文件生成路径
set CS_TARGET_PATH=..\..\java\org\redstone\protobuf\csharp

echo C#编译器路径
set CS_COMPILER_PATH=.\protogen.exe

echo 删除之前创建的文件
del %CS_TARGET_PATH%\*.* /f /s /q

::遍历所有文件
for /f "delims=" %%i in ('dir /b "%SOURCE_FOLDER%\*.proto"') do (
    
    ::生成 C# 代码
    echo %CS_COMPILER_PATH% -i:%SOURCE_FOLDER%\%%i -o:%CS_TARGET_PATH%\%%~ni.cs
    %CS_COMPILER_PATH% --proto_path=%SOURCE_FOLDER%  -i:%SOURCE_FOLDER%\%%i -o:%CS_TARGET_PATH%\%%~ni.cs
)
echo 协议生成完毕。

pause