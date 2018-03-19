# TVinities

A simple Android app that makes use of the Movie Database API in order to retrieve and populate a list of popular TV shows, along with some detailed information regarding each of them.

Some notes of interest:
* The list of TV shows is displayed within a grid, which tries to fit as many columns as possible based on the screen width. For this purpose, I've added a customized Recycler View by Chiu-Ki Chan (https://github.com/chiuki/android-recyclerview).
* Errors are handled differently according to the view that raised the error:
  * In ListActivity, if the first page couldn't be loaded, the error will be displayed in the same layout. Otherwise, the error message will be shown in a dialog, and the retry button will be displayed after the last loaded item.
  * In DetailActivity, if the detailed information couldn't be loaded, the error will be displayed in the same layout. If the error tooks place when retrieveing similar shows, it will be reported within a dialog, no matter the page number.
* Activities won't directly call methods from the Retrofit service, but from a custom API class which will be responsible for adding the API, TVinitiesAPI.
