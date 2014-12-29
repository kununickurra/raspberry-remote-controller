Hey,

This project will simply create a web application (war file) that can be deployed on a web server located on your raspberry
to drive your home devices using the GPIO bus.

REST and SOAP services are deployed and can be accessed from anywhere.

Make sure your maven uses java 1.7 or above.

mvn -version

SOAP service is build using a contract-first web service.
Specification project is needed to be able to build the project, to build the project,
run the following commands.

git clone https://github.com/kununickurra/raspberry-remote-controller-soap-spec.git
cd raspberry-remote-controller-soap-spec
mvn clean install


Please go back to the main directory or raspberry-remote-controller project install it and generate the maven site.

mvn clean install
mvn site site

Access your maven site in target/site/index.html
Follow the menu on the upper right corner to get it up and running on your raspberry.

Enjoy.
Any question, comments or remarks, feel free!
