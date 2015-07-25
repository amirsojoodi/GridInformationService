#### GridInformationService(GIS)

This project is a simple GIS for a local grid.

#####How to Build:
* Clone the repository
* Copy the contents of directory repo to your local maven repository. `cp repo/* ~/.m2/repository/ -r`
* Build with `mvn clean install`

#####How to run:
* Server: `java -cp target/lib/*:target/GridInformationService-0.0.1-SNAPSHOT.jar org.shirazu.gridcourse.gui.ServerFrame`
* Client: `java -cp target/lib/*:target/GridInformationService-0.0.1-SNAPSHOT.jar org.shirazu.gridcourse.gui.

When server starts it asks for the _port number_ you want to listen on, _simulation time_ and _timeintervals_.
