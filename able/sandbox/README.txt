Build instructions
==================

First of all make sure you have installed Maven 2.0.4 or later.

The maven-apt-plugin is not yet released at the time of writing so build that first as

	cd maven-apt-plugin
	mvn install
	cd ..
	
Then build the example module (able-jpa) via

	mvn install
	
To run the example

	cd able-jpa
	mvn jetty:run
	
