@echo off
echo 删除之前创建的cs文件
del	.\*.cs /f /s /q
echo copy proto临时文件
xcopy ..\proto\*.proto .\
echo 开始生成cs文件...
for /f "delims=" %%i in ('dir /b ".\*.proto"') do (
	echo protogen -i:%%i -o:%%~ni.cs
    protogen  -i:%%i -o:%%~ni.cs
)
echo cs文件生成完毕
echo 删除临时proto文件
del .\*.proto /f /s /q