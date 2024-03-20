#!/bin/sh

RET=0
TEST_CLASS=GenericTest

if [ "$1" != "" ] ; then
	TEST_CLASS=$1
fi

echo "Execute test ${TEST_CLASS}"

while [ ${RET} = 0 ] 
do 
	mvn test -Dtest=${TEST_CLASS}
	RET=$?
done

echo "Last test run returned ${RET}"
