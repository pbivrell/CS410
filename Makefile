class_path="lib/Jama-1.0.3.jar"

all:
	javac -d bin -sourcepath src -cp $(class_path) src/cs410/**/*.java src/Modeltoworld.java

setup:
	cp models/* bin/

clean:
	rm -rf bin/*
