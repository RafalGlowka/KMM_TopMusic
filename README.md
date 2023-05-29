# Top 100 Music
Example application build on https://rss.applemarketingtools.com/ public API as an architecture test case of Kotlin Multiplatform Mobile. 

!! Project is still in alpha stage and will be improved in the future !!

The purpose of this application is to show usage of some basic libraries and architecture concepts to create one code base application for both Android and iOS. 
* Clean architecture - Currently I resigned from UseCases (quite important part of clean architecture). All of them were 1:1 calls to repositories and do not provide any additional value. By doing so I would like to emphasize the need to rethink the elements of architecture in the context of a particular project. In bigger project use of them may provide additional value in with testing or code reusability. I would use cold-code solution like coroutine Flow in that case.
* MVVM + BusinessFlow
* Compose Multiplatform
* coroutines, flows
* Ktor
* Lottie
* Libres
* Kotest, Turbine
* Detekt

One of the goals was to test the possibility of mixing natively written screens with shares ones. Therefore, each screen has been designed as a separate entity, 
which can be using Compose Multiplatform (As default approach) or be overridden by the native screen. It was achieved by platform specific navigators, using 
under the hood Fragments on Android and UIViewControllers on iOS. 

I could not find Lottie multiplatform implementation (or other library for showing animations from json files). It was an occasion to test native approach.
Intro screen was implemented in the commonModule and then it's View layer was overwritten on platforms, providing platform specific screens that are using 
Lottie library implementations from specific platforms. 

Project was build with AndroidStudio Hedgehog. To build iOS app MacBook with newest XCode is required.

## Screen shots
<table>
        <tr>
            <th>
                <h3>Android</h3>
            </th>
            <th>
                <h3>iOs</h3>
            </th>
        </tr>
        <tr>
            <td colspan="2" align="center">
               Splash
            </td>
        </tr>
        <tr>
            <td> <img src="/documentation/android1.png" width="300"> </td>
            <td> <img src="/documentation/ios1.png" width="300"> </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                Splash with connection error
            </td>
        </tr>
        <tr>
            <td><img src="/documentation/android2.png" width="300"> </td>
            <td><img src="/documentation/ios2.png" width="300"> </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                List of albums - loading thumbnails
            </td>
        </tr>
        <tr>
            <td><img src="/documentation/android3.png" width="300"> </td>
            <td><img src="/documentation/ios3.png" width="300"> </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                List of albums - loading thumbnails error
            </td>
        </tr>
        <tr>
            <td><img src="/documentation/android4.png" width="300"> </td>
            <td><img src="/documentation/ios4.png" width="300"> </td>
        </tr>
        <tr>
           <td colspan="2" align="center">
                List of albums
            </td>
        </tr>
        <tr>
            <td><img src="/documentation/android5.png" width="300"> </td>
            <td><img src="/documentation/ios5.png" width="300"> </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                Album details
            </td>
        </tr>
        <tr>
            <td><img src="/documentation/android6.png" width="300"> </td>
            <td><img src="/documentation/ios6.png" width="300"> </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                Changing list
            </td>
        </tr>
        <tr>
            <td><img src="/documentation/android7.png" width="300"> </td>
            <td><img src="/documentation/ios7.png" width="300"> </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                List of albums in Landscape
            </td>
        </tr>
        <tr>
            <td><img src="/documentation/android8.png" width="300"> </td>
            <td><img src="/documentation/ios8.png" width="300"> </td>
        </tr>
        <tr>
            <td colspan="2" align="center">
                Details in Landscape
            </td>
        </tr>
        <tr>
            <td><img src="/documentation/android9.png" width="300"> </td>
            <td><img src="/documentation/ios9.png" width="300"> </td>
        </tr>

</table>

## Thinks are missing:
* Kotest - Tests are not working yet, there is some link stage problem that blocks running tests of common module.
* Compose preview in common module
* Address problem of app restoration after process was killed.
* Testing interaction with more advanced compose components or user interactions (e.g onScreenKeyboard, some platform specific services)
* Testing different navigator types (e.g. screen with tabs, inner-screen navigation) 


## Problems with libraries I've noticed during work or preparation to this project:
* dev.icerock.mobile.multiplatform-resources - Was not generating resources from png files
* sqlDelight - Generate some strange problems with compose compiler. 
