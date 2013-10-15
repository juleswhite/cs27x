Refactoring Exercise
====================

Overview 
--------
This exercise will test your ability to refactor a poor implementation of an
app for taunting phone thieves. The app allows users to send secret command msgs
to it via SMS. The app automatically hides the incoming SMS msgs so that they do
not appear in the SMS log/app. If the user's phone is stolen, they can use this
secret SMS backdoor to their phone in order to take pictures of the thief, taunt
them, or find the location of the phone.

Unfortunately, the developer has produced a brittle and non-extensible design for
this app. Your assignment is to refactor the app so that the following requirements
are met:

1. It should be easy to add new commands
2. Command implementations should be able to choose their own message format
   after the "cmd:" part of the msg
3. Other poor design decisions that you come across are fixed

Instructions
------------
1. Refactor the code to improve its design
2. Exchange your refactored code with another group 
3. Perform a code review of the other group's refactored design 

Running the App
---------------
1. Run the project as an Android Application
2. Use the Eclipse emulator controls to send a text msg with a command to the app (e.g., cmd: taunt "you will rot in jail")

