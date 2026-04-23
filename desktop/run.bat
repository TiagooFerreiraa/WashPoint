@echo off
javac -cp "lib/*" src/Main.javac
java -cp "lib/*;src" Main
pause