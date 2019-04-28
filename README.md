# Lexical normalisation of social media data using various approximate matching techniques
# Overview
This program includes implementation of Global Edit Distance, Soundex and Editex on a supplied data set. To use the system:

1. Compile the src folder using the javac command.

Eg. javac -cp ".:java-string-similarity-1.2.1.jar" spell/*.java

2. Run the program in the /src folder and specify the method to be used:
java -cp ".:java-string-similarity-1.2.1.jar" spell.{PROGRAM_NAME} {METHOD}

PROGRAM_NAME will either be Program or Program_refined to use the refined GED method.

METHOD will be either GED, Soundex or Editex.

Be sure to include the jar in the class path ("-cp").

eg. java -cp ".:java-string-similarity-1.2.1.jar" spell.Program_refined GED

This will return the accuracy and recall values from the report and will print the predicted tokens to "predicted.txt".

To switch between the LD and CS GED, the parameters used in the GED algorithm need to be altered. 

This program included code from:

Robert Sedgewick and Kevin Wayne, https://introcs.cs.princeton.edu/java/31datatype/Soundex.java.html, Soundex.java, used in Program.java and Program_refined.java

Thibault Debatty, https://github.com/tdebatty/java-string-similarity, N-Gram.java, used in Program.java and Program_refined.java

rpjavp@rit.edu, https://www.cs.rit.edu/~rpj/courses/bic1/labs/lab5/lab53.html, used in Program.java and Program_refined.java