#!/bin/bash

##
## Update the *.s-model and *.ref file contents
##
#
#for source_file in ./tests/tema3/*.cl; do
#	echo "Running with `basename $source_file`"
#	java -jar Tema3.jar $source_file > ./tests/tema3/`basename $source_file .cl`.s-model
#	if [ "$source_file" = "./tests/tema3/32-big.cl" ]; then 
#		echo 5 | spim -exception_file trap.handler.nogc -file tests/tema3/`basename $source_file .cl`.s-model > tests/tema3/`basename $source_file .cl`.ref
#	else
#		spim -exception_file trap.handler.nogc -file tests/tema3/`basename $source_file .cl`.s-model > tests/tema3/`basename $source_file .cl`.ref
#	fi
#done

#CLASSPATH=.:/usr/local/lib/antlr4.jar
CLASSPATH=.:/mnt/c/Users/stanl/Documents/Uni/cpl/Tema3/untitled/out/production/Tema3/antlr4-4.10.1-complete.jar

passed=0
for source_file in ./tests/tema3/*.cl; do
	echo -e "\n`basename $source_file`"
	/usr/bin/java -cp $CLASSPATH cool.compiler.Compiler $source_file > ./tests/tema3/`basename $source_file .cl`.s
	if [ "$source_file" = "./tests/tema3/32-big.cl" ]; then
		echo 5 | /usr/bin/spim -exception_file trap.handler.nogc -file tests/tema3/`basename $source_file .cl`.s > tests/tema3/`basename $source_file .cl`.out
	else
		/usr/bin/spim -exception_file trap.handler.nogc -file tests/tema3/`basename $source_file .cl`.s > tests/tema3/`basename $source_file .cl`.out
	fi

	diff tests/tema3/`basename $source_file .cl`.ref tests/tema3/`basename $source_file .cl`.out

	if [ $? = 0 ]; then
		echo -e "Test passed!\n"
		passed=$(($passed + 1))
	else	
		echo -e "Test failed!\n"
	fi
done

echo -e "\nTotal: $(( $(($passed * 100)) / 32))"
