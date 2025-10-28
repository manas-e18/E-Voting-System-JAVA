@echo off
cd /d "%~dp0"
(for /R %%f in (*.java) do @echo %%f) > sources.txt
javac @sources.txt
del sources.txt
java com.evoting.ui.Main
pause

