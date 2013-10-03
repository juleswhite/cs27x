Dependency Injection
=======================

Overview: The goal of this exercise is to show you: 1) how to use
the Spring dependency injection framework, 2) and how Spring uses
the dependency injector to automatically introspect your code and
turn it into a web application, and 3) how aspect-oriented programming
is used by Spring to filter incoming/outgoing calls to your code.

Unlike Guice, Spring primarily relies on an XML file to specify how
the dependencies should be injected. For applications using Spring's
MVC web framework, this configuration file usually lives in 
war/WEB-INF/config/dispatcher-servlet.xml. Spring allows you to
expose arbitrary Java objects as services that can be accessed via
a variety of protocols, such as HTTP. In this exercise, the spring
dependency injector is automatically translating incoming HTTP requests
into invocations on the PhotoSvc class. It uses the @RequestMapping
attribute to determine which method to invoke based on the URL path.
Methods annotated with @ResponseBody have their return values automatically
transformed into JSON and returned. Methods without this annotation
have an appropriate view JSP retrieved from the war/WEB-INF/jsp folder
and used to render the response to the client.


Pair Programming
------------------------

For this exercise, you will be working in teams of two and pair programming. Only one person
should have their laptop open. You should take turns implementing your approach.


Exercise Steps
--------------

See the PhotoSvc class for the exercise steps.

Running the App
---------------
1. Right-lick on the RestServer class, run-as, Java Application
2. Open your browser to http://localhost:8080/cs27x/<some request mapping> (e.g., http://localhost:8080/cs27x/uploadForm)

