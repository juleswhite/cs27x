Metaprogramming & Automation Assignment 
=======================================
Overview: 
----------
This metaprogramming and automation exercise is based around a framework for enabling users to
create Java annotations that provide pre/post method execution logic. For
example, the framework has a simple implementation of an Echo annotation that
automatically logs entrance and execution to a method through System.out.println.
The usage for the Echo annotation is as follows:

```java
public class YourClass {

@Echo("a label for yourMethod()")
public void yourMethod(){
  //logic...
}

}
```

Then, when you create an instance of your class, you
do the following to enable annotation procesing:

```java
YourClass obj = new YourClass();
obj = PreProcessorWrapper.wrap(obj);
```

The PreProcessorWrapper.wrap(...) method creates a proxy to the original object
that will ensure that the appropriate pre/post method execution logic is applied to
the object. In this case, the proxy object will ensure that "a label for yourMethod()"
is echoed to System.out.println every time you call yourMethod().

The framework is designed to be extensible. You can create your own annotations and
pre/post execution logic as follows:

1. Create a new Java annotation to mark methods with your logic (an example is in
   org.cs27x.adv.java.directives.Echo
   
2. Create a new implementation of InvocationProcessor that is associated with your
   annotation (an example is in org.cs27x.adv.java.processors.EchoProcessor)
   
3. Register your annotation and processor with an InvocationProcessorFactory implementation
   that is passed to the wrap(...) method.

Another simple example annotation is the TimeMe annotation. The TimeMe annotation 
automatically prints method execution times to System.out.println.

Instructions:
----------------------------------------------------------------------------------

Read through the source code and look for the @AsgnStep("...") annotation. The
instructions for each step are directly embedded in the source code.

Testing:
----------------------------------------------------------------------------------
In order to test your code, right-click on the org.cs27x.adv.java.test.PreProcessorWrapperTest
and select "run as"->"JUnit Test" if you are asked to "select the preferred launcher," 
check "use configuration specific settings" and then select the "Eclipse JUnit Launcher."

If you have completed all steps correctly, all tests in the test case will pass. However,
if the tests pass, it does not *guarantee* that your code is correct.
