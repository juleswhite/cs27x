Dependency Injection
=======================

Overview: The goal of this exercise is to show you how to use
Google's Guice dependency injection. Initially, you will hand-code
the construction of the Dropbox object and its dependencies. Then,
you will refactor the implementation to use Google's Injector and
Modules to automatically construct a Dropbox object for you.

The Injector takes a class or interface as a parameter and will automatically
construct an instance of that class or interface for you. If the class
depends on one or more interfaces (e.g., they are taken as constructor
parameters), the Injector needs to know what concrete implementation of
that interface should be provided (e.g., use FileWatcherSource for FileEventSource).
In order to tell the Injector about these mappings, you create a class that
inherits from Guice's AbstractModule and specify these interface to class
mappings in the configure() method of your derived module.
 
Once you have created an Injector, Guice can automatically create objects
for you -- including any of their dependencies.


Pair Programming
------------------------

For this exercise, you will be working in teams of two and pair programming. Only one person
should have their laptop open. You should take turns implementing your approach.


Exercise Steps
--------------

See the Dropbox and FileManagerImpl classes for the exercise steps.

Running the App
---------------
1. Run the first Dropbox instance with:
-dir test-data/client 
2. Run the second Dropbox instance with:
-dir test-data/client -connectTo &lt;your_ip_address&gt;

