Please run this command line utility as a runnable jar. The first parameter is one of (val, xtoj, jtox),
then the input files follow. The output will be in your terminal, if you want to see the output in a file,
simply redirect it there.

You can find the runnable jar in out/artifacts/Rekrew_jar/ directory
You can find an xsd and a json file matching the sample XML in misc/ directory

Examples:
java -jar Rekrew.jar val addressBook.xsd addressBook.xml
java -jar Rekrew.jar xtoj addressBook.xml
java -jar Rekrew.jar jtox addressBook.json
java -jar Rekrew.jar jtox addressBook.json > myOutput.txt