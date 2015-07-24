# Introduction #

HtmlUnit offers a lot of power and flexibility in testing webpages, but if you want to find the `span` with `class` `item` in the third column of the second row of the `table` inside the `div` with `id` `updates`, you're basically left with two choices:
  1. Use a hideous XPath expression
  1. Get the element with `id` `updates`, get the table, get the second row, the third column of that row, etc.

Doj offers features that make sure you never have to touch an XPath expression again when using HtmlUnit, while keeping your code concise and null pointer safe.

Below is the corresponding code. Remember that elements in HTML can have multiple classes, separated by whitespace.

```
// The XPath way...
DomNode spanNode = page.getFirstByXPath("//id('updates')//tr[2]/td[3]/span[contains(concat(' ', @class, ' '),concat(' ', 'item', ' '))]");

// The do-it-yourself way... If one of the elements is missing 
// you'll get a nice NullPointerException in your code
List<HtmlElement> spanList = page.getElementById("updates").getElementsByTagName("tr").get(1).getElementsByTagName("td").get(2).getElementsByTagName("span");
HtmlElement span = null;
for (HtmlElement element : spanList) {
	String classValue = element.getAttribute("class");
	classValue = (classValue == null ? "" : " " + classValue + " ");
	if (classValue.contains(" item ")) {
		span = element;
		break;
	}
}

// The Doj way...
Doj spanDoj = Doj.on(page).get("#updates tr", 1).get("td", 2).get("span.item");
```

The XPath expression quickly becomes illegible and to make the best of the DIY way, you'll have to include plenty of checks for null pointers - most of them have been left out of the above example.

Doj is inspired by jQuery and it shows. For one, spanDoj will never result to null, even if there is no table. Just like the jQuery object, Doj is a kind of array, that simply accepts any input you throw at it. If you want to know whether there is actually anything there, use the `size()` or `isEmpty()` methods.

Second, you can harness Doj's support for some simple CSS selectors, which is a closer match to HTML than XPath. Think of the trouble you get into with XPath when matching an element by class.

## Getting started ##

You can create a Doj object using one of the static `on()` methods on the Doj class. For example:
```
// Create a Doj instance with the entire page as context
Doj pageDoj = Doj.on(page);
// Create a Doj instance with two (or more) elements as context
Doj doj = Doj.on(page.getElementById("item-1"), page.getElementById("item-2"));
// Create a Doj instance with the elements from the list as context
Doj divDoj = Doj.on(page.getElementsByTagName("div"));
```

Now you can get started traversing the DOM:
```
Doj pageDoj = Doj.on(page);

// Get the p with class "article" inside the element with id "hello"
Doj article = pageDoj.get("#hello p.article");

// Get the inputs inside the span with class input inside the table
Doj input = pageDoj.get("table span.input input");

// Get the labels before the span of those inputs
Doj label = input.parent("span").previous("label");
```

How about entering some data into a form and submitting it?
```
// Get the form inputs
Doj inputs = Doj.on(page).get("#login input");

// Set the username
inputs.withName("username").value("myusername");

// Set the password
inputs.withName("password").value("mypassword");

// Get the submit button and click it
HtmlPage result = inputs.withType("submit").click();
```

Or looping over the current nodes?
```
// Get all p elements on the page
Doj paragraphs = Doc.on(page).get("p");
// Now examine each single paragraph
for (Doj p : paragraphs) {
	// Get the HtmlElement version of the current node
	HtmlElement element = p.getElement(0);
	// ...
	// Which is equivalent to...
	element = p.firstElement();
	// ...
	// Which, since we're looking at one p a time, is equivalent to...
	element = p.lastElement();
	// ...
	// And to...
	element = p.sliceElements(0, 1)[0];
	// ...
	// And to...
	element = p.sliceElements(-1, 1)[0];
}
```

Another common use case is looking up an anchor using the link text:
```
Doc.on(page).get("a").withTextContaining("Logout").click();
```

## Good to know ##
We've collected some information that's nice to know but not absolutely critical in using Doj below.

### Doj is immutable ###
Doj objects are **immutable**: once it's been created there is no way of adding or removing nodes. Sure, there's the `remove()` method, but what it actually does, like many of the other methods, is create a new Doj instance without the removed node.

### Why there is no plain constructor ###
You might be wondering why the Doj class is abstract. Well, in order to keep the code safe, every method would have to check whether the Doj instance it's working on is empty or not. To make sure the important code remains legible, we created two Doj subclasses: one for a non-empty Doj instance and one for an empty Doj instance. The static `on()` factory methods make sure the correct type of Doj instance is created.

### Why Doj accepts anything ###
A feature that some people might overlook when using jQuery, is the fact that it just keeps on working, even when the objects you are trying to manipulate or traverse don't exist.

Doj does the same thing. One might argue that tests should fail fast, including the code to traverse the DOM, whereas Doj acts like nothing strange is going on. Why? Because most of the time you're not looking to find an error in the path you're travelling; you're checking what the value of an attribute is, whether some text is present or whether clicking the button at the end of that path results in the page you expected.

When you absolutely need to verify that the Doj instance is not empty, use the `verifyNotEmpty()` method. When the Doj instance is not empty, it will simply return a reference to the instance, otherwise it will throw a `DojIsEmptyException`. In the following code, the exception will be thrown when the element with `id` `search` could not be found. When it does exist, the first button inside the element is clicked.
```
Doj.on(page).getById("search").verifyNotEmpty().get("button").click();
```