# Popular Movies 2
Popular Movies 2 is an app that has been generated as part of the [Udacity Nanodegree](https://www.udacity.com/course/android-developer-nanodegree-by-google--nd801?v=ad1). Following the guides this App connects to TheMovieDB.org and downloads popular and top rated movies; the app displays posters, images, release date etc. When online the app will allow you to view the displayed trailers or movie reviews, using apps outside of this one.

If you want to download into Android Studio and run this App you will need an API key from [The Movie Database](https://www.themoviedb.org/documentation/api)

## Story So Far
This is my second attempt at the App, the first version did not pass review, various reasons, screen not restoring after rotation, fixed this by using Loaders, database calls on UI thread, fixed this by using [AsyncQueryHandlers] (https://developer.android.com/reference/android/content/AsyncQueryHandler.html) and crashed after the user rapidly pressed a movie image 10 times, got round this by storing the context in a variable in the [onAttach] (https://developer.android.com/reference/android/app/Fragment.html#onAttach(android.app.Activity) method.

## Screens

![screen](../master/app/screenshots/phone1.jpg)
![screen](../master/app/screenshots/phone2.jpg)

![screen](../master/app/screenshots/tablet1.jpg)

## Libraries

* [Retrofit](https://github.com/square/retrofit)
* [Glide](https://github.com/bumptech/glide)
* [Glide Transformations](https://github.com/wasabeef/glide-transformations)

## Credits

* [Error Icon by Elegant Themes](http://www.flaticon.com/authors/elegant-themes) [From] (http://www.flaticon.com) [Licensed Creative Commons BY 3.0] (http://creativecommons.org/licenses/by/3.0/)
* App Icon from http://www.iconarchive.com/show/button-ui-system-folders-drives-icons-by-blackvariant/Movies-icon.html
* [CursorRecyclerViewAdapter] (https://gist.github.com/skyfishjy/443b7448f59be978bc59)

## License

    Copyright 2016 Andrew Jackson

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
