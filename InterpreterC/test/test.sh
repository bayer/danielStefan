#!/bin/bash

if [ ! -f ../interpreter ]; then
    echo "./interpreter not available, do 'make interpreter'"
    exit 1;
fi

SEARCH="NOT SATISFIABLE"

for ((i=00; ;i++ ))
do
    j=$(printf "%02d" $i)

    if [ -f test_${j}.c ]; then

	for (( k=1; ; k++ ))
	do
	    l=$(printf "%02d" $k)
	    if [ -f test_yes_${j}_${l}.tks ]; then
		echo "../interpreter test_${j}.c -t test_yes_${j}_${l}.tks -cl -b 2> /dev/null | grep -c \"${SEARCH}\""
		COUNT=$(../interpreter test_${j}.c -t test_yes_${j}_${l}.tks -cl -b 2> /dev/null | grep -c "${SEARCH}")
		if [ ${COUNT} -ne 0 ]; then
		    echo "WARNING: test_yes_${j}_${l}.tks returned not expected result"
		fi
	    else
		break;
	    fi

	done

	for (( k=1; ; k++ ))
	do
	    l=$(printf "%02d" $k)
	    if [ -f test_no_${j}_${l}.tks ]; then
		echo "../interpreter test_${j}.c -t test_no_${j}_${l}.tks -cl -b 2> /dev/null | grep -c \"${SEARCH}\""
		$(../interpreter test_${j}.c -t test_no_${j}_${l}.tks -cl -b 2> /dev/null > /dev/null)
		if [ $? -eq 0 ]; then
		    COUNT=$(../interpreter test_${j}.c -t test_no_${j}_${l}.tks -cl -b 2> /dev/null | grep -c "${SEARCH}")
		    if [ ${COUNT} -eq 0 ]; then
			echo "WARNING: test_no_${j}_${l}.tks returned not expected result"
		    fi
		fi
	    else
		break;
	    fi

	done

    else
	break;
    fi
done

echo "Finished all test cases"

