Eat with Me Hackathon
======================

Do this first!
--------------

Follow these instructions EXACTLY or the project will not be imported correctly:

1. Install AppEngine
2. Clone the repo (done or you wouldn't be reading this)
3. File->Import->General->Existing Projects Into Workspace
4. Choose this project on disk (should appear as FeedMeNow)
5. After the import is complete, right-click on the project, Google->AppEngine Settings
6. Check "use Google AppEngine" and make sure that 1.8.7 or higher is the version of
   the SDK listed (if not, select use specific SDK and select it)
7. Ensure that "Use DataNucleus..." is checked
8. Ensure that DataNucleus JDO/JPA version is set to v1
9. The project should rebuild and compile correctly

Do not check-in your .settings folder or changes to your .project folder or you
will break your other team member's Eclipse setups... 

Overview
---------
The goals of this exercise are to gain familiarity with AppEngine and practice 
team software development. For this exercise, the class will split into two teams. 
Each team is responsible for implementing the EatWithMe application described below.

Eat With Me
-----------
For this exercise, you will be building the "Eat with Me" server. The requirements
are as follows:

1. A user can create a list of their friends.
2. A user can create an "eat with me" post where they specify the restaurant that they
   are going to and when they are going.
3. A user can respond to an eat with me post and be added to the list of friends going
   to that restaurant.
4. A user can see a list of all people that are responding to a specific eat with me
   post.
5. A user can see a list of all posts by their friends  
6. A user can see a map listing all of the eat with me posts.

You should create a minimum viable product for the server-side of this application.
Except for the map, the interface should simple GET requests that you manually type 
into the address bar and JSON results printed in the browser. 

This project is more work than one person can accomplish in the class period. The
only way to complete the project is to divide up the work among the team members.

When your application is complete, put your &lt;app_id&gt;.appspot.com ID up on the board.

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

1. Attach some data to a specific lat/lon coordinate:
   http://localhost:8888/data/create?lat=37.215&lon=-80.40980000000002&name=foo&description=cs278
   
2. View a map of the geo indexed data:
   http://localhost:8888/index.html
   
   Note: if you leave the map open a long time, you will hit the free request limit for
   AppEngine and your app will be shut down. The map has a simple dumb polling mechaism.

3. Add to your list of friends:
   http://localhost:8888/data/friends/add?user=foo&friend=bar2
   
4. See a list of your friends:
   http://localhost:8888/data/friends/find?user=foo


The programmatic building blocks are as follows:

1. This AppEngine project has been setup to use Spring and return JSON. You can add
   request paths that the app will respond to and methods to handle them by adding them
   to a Controller object in org.clear.server.controllers. Any method can be annotated 
   to add a handled request as follows:

```java 
   @RequestMapping(
   			// The url path that the method handles, http://localhost:8888/data/friends/find
   			value = "/data/friends/find", 
   			// The HTTP methods that the method supports
   			method = RequestMethod.GET)
   public String getDataInProximityTo(
            // Parameters are bound to request parameters with the @RequestParam annotation,
            // example: http://localhost:8888/data/friends/find?someVal=1.23
            @RequestParam("someVal") double lat, 
            // This is an extra object passed to you by Spring (for this particular configuration)
            // that you store the data you wanted returned to the client in. Spring automatically
            // converts it to JSON
            Model model) {

		...
		// Store data to return to the client as json
		model.addAttribute("data", "abc");

		// Tell Spring to render the returned data as JSON
		return JSON_VIEW;
	}
```
	
2. AppEngine uses the JDO Java standard for automatically storing entire objects in a database
   using Object Relational Mapping (e.g., automatically map your object instances that you store
   to rows in the database and turn queried rows into objects). Some basic persistent objects
   are provided for you in the Friends and LocatableData classes. LocatableData uses a specialized
   geohashing scheme to support proximity queries on AppEngine's key/value (e.g., NoSQL) datastore.
   The basics of persisting data are to create a new class as follows:

```java 
   // Mark the class with @PersistenceCapable to indicate that it is going to be stored in the
   // datastore
   @PersistenceCapable
   public class Foo {
   
   		// Add a primary key
   		@PrimaryKey
		@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
		private Key key;

		// Create some properties that you want to persist
		@Persistent
		private String name;
		
		//Add getters/setters
	}
```	

To save an instance of an object, see the example in Friends.save(). For querying, see the
examples in Friends.byKey(...) and Friends.byUser(...).
	
AppEngine provides a management interface to view all persisted data in:
http://localhost:8888/_ah/admin (local debugging)
	
or
	
http://appspot.com (apps that have been deployed to AppEngine)
	
3. A basic geo-querying service is implemented in GeoDataController.
 
4. A basic map display of all stored geo data is provided in map.js. You can view the map at
   http://localhost:8888/index.html

5. FriendsController provides a basic friend list management service

6. AppEngine provides tools to deploy your application to their servers. To deploy an app
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
   
 7. Dependency Injection - this app uses Spring, you can modify the dependency injection
    by editing war/WEB-INF/dispatcher-servlet.xml
