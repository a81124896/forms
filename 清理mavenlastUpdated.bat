
rem ����д��Ĳֿ�·��
set REPOSITORY_PATH=D:\mavenLib\repository
rem ��������...
for /f "delims=" %%i in ('dir /b /s "%REPOSITORY_PATH%\*lastUpdated*"') do (
    del /s /q %%i
)
rem �������
