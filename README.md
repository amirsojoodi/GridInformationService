#### GridInformationService(GIS)

This project is a simple GIS for a local grid.

When server starts it asks for which _port number_ you want it to listen on, and also _simulation time_ and _timeintervals_ of the service. Now you can start the client. It requests IP and Port of the server then it starts with sending its static data to the server. Using _RMI_ it asks server the timeinterval and its given ID and after that in each time interval, it sends its dynamic data with the assigned ID to the server. You can query the server log and see how many clients have been connected and what is their statistics as well.

#####How to Build:
* Clone the repository
* Copy the contents of directory repo to your local maven repository. `cp repo/* ~/.m2/repository/ -r`
* Build with `mvn clean install`

#####How to run:
* Server: `java -cp target/lib/*:target/GridInformationService-0.0.1.jar org.shirazu.gridcourse.gui.ServerFrame`
* Client: `java -cp target/lib/*:target/GridInformationService-0.0.1.jar org.shirazu.gridcourse.gui.ClientFrame`

