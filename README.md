# 7digital SDK for Android #

The 7digital Android SDK enables you to you add functionality such as preview streams, music charts and metadata, search and purchasing to your app.

Full SDK documentation is available at http://7digital.github.io/7digital-Android-SDK/apidocs/

## Getting Started ##

1. Register as a developer to get a key and secret at http://developer.7digital.com

2. Clone the repository, which includes a sample app

3. To run the sample app, change the _appKey_ and _appSecret_ in the Constants.java file to your own key and secret.

4. To use the SDK in your app, include the cloned repository as a module in Android Studio. Make sure to compile the 'sdk' module as a dependency.

		dependencies {
			compile project(':sdk')
		}

5. Alternatively, a compiled .aar or .jar is available in the Releases tab.

 
## Running the sample ##

To run the sample, or any of the sdk tests, you must add your test credentials to the projects gradle.properties file.

A sample gradle.properties file (**gradle.properties.sample**)
 is included.
 
 Copy these varibles to your gradle.properties file and fill in your 7digital credentials.
 
## Adding Purchasing to Your App ##

To add purchasing simply integrate the 7digital Buy Button. This takes care of the checkout and music download process without sending users away to another site. For an example app, see the [sample 7digital buy button app.](https://github.com/7digital/7digital-android-buy-button-demo)








