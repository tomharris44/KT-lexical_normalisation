# Lexical normalisation of social media data using various approximate matching techniques
# Overview
This program includes implementation of Global Edit Distance, Soundex and Editex on a supplied data set. To use the system:

1. Compile the src folder using the javac command.

2. Run the program in the /src folder and specify the method to be used:
java -cp ".:java-string-similarity-1.2.1.jar" spell.{PROGRAM_NAME} {METHOD}

PROGRAM_NAME will either be Program or Program_refined to use the refined GED method.

METHOD will be either GED, Soundex or Editex.

Be sure to include the jar in the class path ("-cp").

eg. java -cp ".:java-string-similarity-1.2.1.jar" spell.Program_refined GED

This will return the accuracy and recall values from the report and will print the predicted tokens to "predicted.txt".
