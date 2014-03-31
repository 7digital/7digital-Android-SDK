# 7digital SDK for Android #

The 7digital Android SDK enables you to you add functionality such as preview streams, music charts and metadata, search and purchasing to your app.

Full API documentation is available at: <insert link to javadocs>

## Getting Started ##

1. Register as a developer to get a key and secret at http://access.7digital.com/partnerprogram

2. Clone the repository, which includes a sample app

3. To run the sample app, change the _appKey_ and _appSecret_ in the Constants.java file to your own key and secret.

4. To use the SDK in your app, include the cloned repository as a module in Android Studio. Make sure to compile the 'sdk' module as a dependency.

		dependencies {
			compile project(':sdk')
		}

5. Alternatively, a compiled .aar is available in the Releases tab.


Note: The 7Digital Android SDK depends on Volley. Volley will also need to be added as a dependency (a module) in your app.








