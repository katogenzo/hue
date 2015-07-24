Hue provides Java components to speed up writing tests using [HtmlUnit](http://htmlunit.sourceforge.net/), such as jQuery style DOM traversal and manipulation and common data generation tools (postal codes, telephone numbers, etc.).

# Hue Doj: Dom like jQuery #
The 1.0 release of Hue focused on the creation of [Doj](http://code.google.com/p/hue/wiki/Doj), Hue's jQuery emulator for HtmlUnit.

Why would you want to use that when you're using HtmlUnit? Because you can do stuff like this:
```
// Get the inputs in the login form through simple CSS selectors
Doj input = Doj.on(page).get("#login input");

// Set the username
input.withName("username").value("myusername");

// Set the password
input.withName("password").value("mypassword");

// Get the submit button and click it
Page result = inputs.withType("submit").click();
```
And since the 1.1 release, you can do this:
```
Doj formElements = Doj.on(page).get("select, input.textfield, textarea, button#save-button");
```

## Get started with Maven ##
If you're using Maven, getting started using Hue is as simple as plugging the following into your pom:

```
<dependency>
    <groupId>be.roam.hue</groupId>
    <artifactId>hue</artifactId>
    <version>1.1</version>
    <scope>test</scope>
</dependency>
```

Have a look at the docs at http://hue.googlecode.com/svn/api/1.1-SNAPSHOT/index.html. The snapshot releases are available at http://hue.googlecode.com/svn/maven2-snapshots/be/roam/hue/hue/.