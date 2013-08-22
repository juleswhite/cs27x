Assignment 1: Basic Java
=====
Due Date
--------
Initial Implementations - 8/25 at Midnight

Code Reviews - 8/27 before class

Overview
--------
This assignment will ensure that you understand core object-oriented and
Java programming concepts needed in this class. The assignment application is
a client/server program for forwarding SMS messages from an Android device to
a web browser. 

Assignment Steps
----------------
The assignment steps are embedded in the source files of the SodaCloudSMSExampleClient
project. Look for "Asgn Step X" in the comments. There should be 8 steps to complete.

A note on the requirements/spec: Real projects can have ambiguous or continually changing
requirements. One component of this class is learning to deal with very loose requirements
and gaining clarification. I highly recommend starting immediately and asking for
clarification on any 

Almost all code can be improved. You do not need to view the assignment base
code as untouchable. If you see areas for improvement, you can and should 
refactor the code to make it better. There are lots of areas related to
Intent data passing that you might think about improving... 

Using the Application
---------------------

The workflow for using the application is as follows:

1. The user launches their web browser to http://<their_ip_address>:8081/

2. An Android device launches the SodaCloudSMSExampleClient

3. The user either scans the QR code on the web page or manually types in the server
   IP address and ObjRef that are displayed.
   
4. When "connected" appears on the device, all of its SMS messages will begin being
   forwarded to the web page. Also, a user can click the "+" next to "conversations"
   and type in a phone number, such as "15555555555." Once a conversation is started,
   you can use the browser to send outgoing SMS messages as well.


Running the Application
-----------------------
The application consists of two parts: 1) a server and 2) an Android client for 
forwarding SMS messages to the server. To run the application:

1. Right-click on SodaCloudSMSExampleServer/org.magnum.sodacloud.example.sms.SMSBridgeServer
   and select Run-As->Java Application
   
2. Right-click on the SodaCloudSMSExampleClient project and select Run-As->Android Application

3. Follow the steps above to connect the client to the server

Sending Mock SMS Messages to the Android Emulator
-------------------------------------------------

1. In Eclipse, Window->Open Perspective->Other->DDMS

2. Click on your running emulator in the Devices listing

3. Select the Emulator Control tab

4. Type in a mock incoming phone number

5. Select SMS

6. Type in a mock message

7. Click call/send

