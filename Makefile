class_path="lib/commons-math3-3.6.1.jar"

all:
	javac -d bin -sourcepath src -cp $(class_path) src/cs410/**/*.java src/*.java

setup: all
	# Clean and rebuild testing dir
	#rm -rf testing/*
	# Get complied src
	cp -r bin testing/
	# Get libraries
	cp -r lib testing/
	# Get drivers/models/soln
	cp -r examples/* testing/
	# Unpack models
	mv testing/models/* testing/

clean:
	rm -rf bin/*

