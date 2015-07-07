Using the GUI
* Hit the "Choose"-button and select the (TinyC)-program you want to examine
* Eigther check "Guess" or enter predicates for the abstraction. If guess is selected all entered predicates are ignored
* If the output should be provieded as a picture -> check the picture option
* For displaying the picture select your program of choice or enter a different one
* Hit the run button and wait

Entering predicates:
Click on the "+"-button to enter a new predicate.
Predicates can be provided in SMT-syntax (with or without surrounding braces)
To remove a predicate from the list select it and then click on the "-"-button
Please keep in mind that entered predicates are not stored once the GUI is closed.

Keep in mind that abstraction can take quite long even with a few predicates. Unfortunately, the GUI does not allow to abort a running abstraction. If you want to abort, just exit the GUI and start again.

The "Guess" heuristic guesses up to 5 predicates. For 5 predicates, abstraction can already be quite expensive. Be sure to enter predicates manually if you want it to run faster. Examples of predicates would be

"< a b"
"<= a b"
"= a b"
"= b (* c (+ (d (- e))))"
... and any other MathSAT5 SMT-LIB2 expression

If you do not want to use the GUI, use the script "abstractor". The script takes as arguments:
* The path to a tiny-c file (make sure the directory is writable, there are some compiled files to be created)
* A bunch of predicates like the ones above. This is optional; if no predicates are given, the "guess" heuristic is employed.

Example usage:
./abstractor TestPrograms/test01.tc "= a b" "< d e"
./abstractor TestPrograms/test02.tc

For the abstractor program to open the generated abstraction image automatically, the "xv" image viewer must be installed. If this does not work, either install xv or change the occurrence of "xv" in the abstractor script to "gwenview" or "eog" or a different image viewer.

The output image is stored, in any case, as "path-to-tiny-c-file.png".
