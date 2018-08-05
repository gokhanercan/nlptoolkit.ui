
echo "Packaging vaadin web server app into the war file..."
rem call mvn clean 'often requires cleaning in order to remove older jar references'
rem call mvn compile 'still it can't compile via mvn standalone, we depend upon IDE's compilation settings!'
call mvn package
echo jetty:run
pause
