@echo off
echo:
echo The following dependencies will be installed using pip:
echo:
type requirements.txt
echo:
echo:
pause

pip install -r ./requirements.txt
python.exe ./src/Main.py