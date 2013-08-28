JUnit Testing Exercise
=======================

This exercise will introduce you to unit testing with mock objects using JUnit and Mockito. 
The exercise is based on the code base for Asgn 2. You can use the tests that you create in
class as part of your solution to Asgn 2. 


Pair Programming
------------------------

For this exercise, you will be working in teams of two and pair programming. Only one person
should have their laptop open. You should take turns implementing the tests that you discuss.

Branching
------------
This exercise will be based on the code base for Asgn 2. I suggest that you come up with a branching
scheme for completing the exercise. When you are done, you should push your changes to both your
and your partner's repos.

Step 1
--------
Refactor the FileStates class to extract an interface. You should then refactor the DropboxCmdProcessor to
refer to this interface rather than the FileStates object. Once your FileStates interface is complete,
create stub objects for the FileStates and FileManager interfaces and use them to create a JUnit test
for the DropboxCmdProcessor.

If you aren't sure what the difference is between a mock and a stub, please raise your hand and ask!

Step 2
-------
Create a JUnit test for the DropboxCmdProcessor that uses mock objects for the FileStates and FileManager. 

Step 3
-------
Create a JUnit test for the DropboxProtocol. You can use either stubs or mocks for the test. Part of the
implementation of the DropboxProtocol is not ideal for testing. Figure out which part of the DropboxProtocol
object is not well-suited for testing and refactor it to make it easier to test. 

Step 4
-------
Create a JUnit test for the DropboxFileEventHandler.

Step 5
-------
Create a JUnit test for the DefaultFileManager. 



