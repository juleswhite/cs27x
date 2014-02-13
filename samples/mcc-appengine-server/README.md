MCC Sample Server Implementation
======================

Do this first!
--------------

Follow these instructions EXACTLY or the project will not be imported correctly:

1. Install AppEngine
2. Clone the repo (done or you wouldn't be reading this)
3. File->Import->General->Existing Projects Into Workspace
4. Choose this project on disk (should appear as mcc-appengine-server)
5. After the import is complete, right-click on the project, Google->AppEngine Settings
6. Uncheck "use Google AppEngine" 
7. Uncheck "use JDO..."
8. Hit OK
9. Re-open the AppEngine Settings, Check "use Google App Engine" and make sure that 1.8.7 or 
   higher is the version of
   the SDK listed (if not, select use specific SDK and select it)
7. Ensure that "Use DataNucleus..." is checked
8. Ensure that DataNucleus JDO/JPA version is set to v2
9. Hit OK
10. (if the project has an exclamation point) Right-click on the project, go to properties, 
    Java Build Path, choose the Libraries tab, scroll the the bottom and select the 
    App Engine SDK library that is listed as "unbound" and then hit remove and OK
11. The project should rebuild and compile correctly

Do not check-in your .settings folder or changes to your .project folder or you
will break your other team member's Eclipse setups... 

Overview
---------
This is a basic implementation of a possible server for the MCC project. The server
provides the following capabilities:

1. Storage of floorplans in JSON format in AppEngine's database
2. Pathfinding on floorplans using Dijkstra + Fibonacci Heap
3. An HTTP interface via Spring to obtain floorplan json and retrieve shortest paths
4. An initial implementation of Austin's guesser framework with simple implementations
   of guesser strategies that rely on user input

Installing AppEngine
---------------------
1. (Mac) Help->Install New Software, click add and use "http://dl.google.com/eclipse/plugin/4.3" for
   both the name and location
2. Choose the Google Plugin for Eclipse (if you don't have it) and the SDKs/App Engine SDK
3. ...install it
4. You will have to restart Eclipse
5. After restarting Eclipse, if you see a red x on the project, you may need to right-click on the
   project root-> Google -> App Engine Settings -> check use Google App Engine and make sure that
   the SDK is 1.8.7

Running the App
----------------
1. Right-click on the root of the project -> Run as -> Web Application

Building Blocks
----------------
A variety of simple services are already available:

1. Add a new floorplan:

   (manual interface)
   http://localhost:8888/floorplan.editor.html
   
   (programmatic interface), POST to:
   http://localhost:8888/mcc/{floorplanId}
   
   Path: replace {floorplanId} with the desired identifier for the floorplan. 
   Params: floorplan - a json string representing the floorplan. See the example below for the format.
   
   

2. Obtain an existing floorplan via a GET request:
   http://localhost:8888/mcc/floorplan/{floorplanId}

3. Get the shortest path between two locations (e.g., between the upstairsHallway and bathroom1):
   http://localhost:8888/mcc/path/mcc/upstairsHallway/bathroom1


The programmatic building blocks are as follows:

1. This AppEngine project has been setup to use Spring and return JSON. You can add
   request paths that the app will respond to and methods to handle them by adding them
   to the Controller object in org.magnum.mcc.controllers.NavController:

```java 
   @RequestMapping(
   			// The url path that the method handles, http://localhost:8888/data/friends/find
   			value = "/data/friends/find", 
   			// The HTTP methods that the method supports
   			method = RequestMethod.GET)
   public @ResponseBody YourObject doSomething(
            // Parameters are bound to request parameters with the @RequestParam annotation,
            // example: http://localhost:8888/data/friends/find?someVal=1.23
            @RequestParam("someVal") double lat) {

		...
		//do stuff

		YourObject obj = new YourObject();
		...
		
		// Spring is setup to automatically convert your object to JSON
		return obj;
	}
```
	
2. AppEngine uses the JDO Java standard for automatically storing entire objects in a database
   using Object Relational Mapping (e.g., automatically map your object instances that you store
   to rows in the database and turn queried rows into objects). A basic persistent object
   to store floorplans is provided for in the org.magnum.mcc.building.loader.StoredFloorplan
   class. You can use it as a template for creating other persistent objects or see the
   Google tutorial: https://developers.google.com/appengine/docs/java/datastore/jdo/dataclasses

```java 
   // Mark the class with @PersistenceCapable to indicate that it is going to be stored in the
   // datastore
   @PersistenceCapable
   public class Foo {
   
   		// Add a primary key
   		@PrimaryKey
		private String key;

		// Create some properties that you want to persist
		@Persistent
		private String name;
		
		//Add getters/setters
	}
```	

AppEngine provides a management interface to view all persisted data in:
http://localhost:8888/_ah/admin (local debugging)
	
or
	
http://appspot.com (apps that have been deployed to AppEngine)
	
3. Most of the client-facing changes that affect the HTTP interface will need to go into the
   org.magnum.mcc.controllers.NavController class.

4. AppEngine provides tools to deploy your application to their servers. To deploy an app
   to AppEngine, first create a new app at:
   
   https://appengine.google.com/start/createapp?
   
   Then, right-click on the root of your project -> Google -> Deploy to AppEngine. Click the
   link to AppEngine settings to set the application ID that you set in the previous step
   (if needed). Your app will be packaged, deployed, and launched in Google's cloud in a
   few seconds. You can view all of your running versions at:
   
   https://appengine.google.com/instances?&app_id=&lt;your_app_id&gt;
   
   Notice that the AppEngine console has management tools for a variety of aspects of our
   app, ranging from logs to previously deployed versions. If you want to deploy multiple
   versions of your app, you need to change the version id in appengine-web.xml.
   
 5. Dependency Injection - this app uses Spring, you can modify the dependency injection
    by editing war/WEB-INF/conf/dispatcher-servlet.xml. The app is also using Google
    Guice for dependency injection in the NavController. This is not really ideal and
    should probably be changed at some point. The dependencies for Guice are bound in the
    org.magnum.mcc.modules.StandaloneServerModule class.
   
   
 Sample JSON Floorplan 
 ----------------
 ```	
 {
   "edges":[
      {
         "start":"bathroom1",
         "length":1.0,
         "end":"bedroom1"
      },
      {
         "start":"bedroom1",
         "length":1.0,
         "end":"bathroom1"
      },
      {
         "start":"bedroom1",
         "length":1.0,
         "end":"upstairsHallway"
      },
      {
         "start":"bedroom2",
         "length":1.0,
         "end":"upstairsHallway"
      },
      {
         "start":"upstairsHallway",
         "length":1.0,
         "end":"bedroom1"
      },
      {
         "start":"upstairsHallway",
         "length":1.0,
         "end":"bedroom2"
      },
      {
         "start":"upstairsHallway",
         "length":1.0,
         "end":"bedroom3"
      },
      {
         "start":"bedroom3",
         "length":1.0,
         "end":"upstairsHallway"
      }
   ],
   "locations":[
      {
         "id":"downstairsHallway",
         "type":"hallway"
      },
      {
         "id":"bathroom1",
         "type":"bathroom"
      },
      {
         "id":"bedroom1",
         "type":"bedroom"
      },
      {
         "id":"bathroom2",
         "type":"bathroom"
      },
      {
         "id":"bedroom2",
         "type":"bedroom"
      },
      {
         "id":"bathroom3",
         "type":"bathroom"
      },
      {
         "id":"kitchen",
         "type":"kitchen"
      },
      {
         "id":"bedroom4",
         "type":"bedroom"
      },
      {
         "id":"upstairsHallway",
         "type":"hallway"
      },
      {
         "id":"bedroom3",
         "type":"bedroom"
      }
   ],
   "rootType":{
      "name":"root",
      "children":[
         {
            "name":"room",
            "children":[
               {
                  "name":"kitchen",
                  "children":[

                  ],
                  "parent":"room"
               },
               {
                  "name":"bathroom",
                  "children":[

                  ],
                  "parent":"room"
               },
               {
                  "name":"bedroom",
                  "children":[

                  ],
                  "parent":"room"
               }
            ],
            "parent":"root"
         },
         {
            "name":"hallway",
            "children":[

            ],
            "parent":"root"
         }
      ],
      "parent":null
   }
}
```	
