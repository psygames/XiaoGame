@echo off
for /f "delims=" %%i in ('dir /b ".\*.proto"') do (
	echo protogen -i:%%i -o:%%~ni.cs
    protogen  -i:%%i -o:%%~ni.cs
)