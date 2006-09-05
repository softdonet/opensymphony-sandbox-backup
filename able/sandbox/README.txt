Build instructions
==================

First of all make sure you have installed Maven 2.0.4 or later.

The maven-apt-plugin is not yet released at the time of writing so build that first as follows.
(As soon as this has been released via mojo.codehaus.org or maven.apache.org then we will zap it from here)

	cd maven-apt-plugin
	mvn install
	cd ..
	
Then build the example web app module (able-web) via

	mvn install
	
To run the example

	cd able-web
	mvn jetty:run

Other modules
=============

able-apt = an APT processing tool for processing the @Entity objects (maybe ActionBeans too)
able-core = a core library of useful code such as mail & JPA helper classes
	
