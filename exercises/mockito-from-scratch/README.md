Mock Objects Deep Dive
=======================

This exercise will introduce you to the advanced Java features that are used for mock
object frameworks, such as Mockito. Many of these same techniques are used to create
mock object libraries for other languages as well.


Pair Programming
------------------------

For this exercise, you will be working in teams of two and pair programming. Only one person
should have their laptop open. You should take turns implementing your approach.


Exercise Steps
--------------

Complete Steps 1-3 in org.cs27x.mock.Mock.

Then, complete one of the following two options below:


Option 1: Argument Capture
--------------------------

Implement an equivalent to Mockito's ArgCaptor. Example:

foo.bar("asdf");

ArgCaptor<String> argval = ArgCaptor.create(String.class);
verify(foo).bar(argval.capture());

assertEquals("asdf", argval.get());


Option 2: Behavioral Templates
----------------------------------
Extend this simplified mock object framework to allow more sophisticated verification of behavior. 
One feature that we would like to be able to provide is to ensure that when one method is called, 
a sequence of methods are always called after it. For example:

Resource resource = resourceManager.acquireResource("someId");
...
resourceManager.release(resource);

We might want to check that release is always called and passed the Resource object returned
from an acquireResource() call. Design and implement an extension to the framework that can
support these types of multi-call checks.


