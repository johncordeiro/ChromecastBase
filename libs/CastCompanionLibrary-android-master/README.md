# CastCompanionLibrary-android

CastCompanionLibrary-android is a library project to enable developers integrate Cast capabilities into their applications faster and easier.

## Dependencies
* google-play-services_lib library from the Android SDK (at least version 7.5+)
* android-support-v7-appcompat (version 21 or above)
* android-support-v7-mediarouter (version 21 or above)

## Setup Instructions
* Set up the project dependencies

## Documentation
See the "CastCompanionLibrary.pdf" inside the project for a more extensive documentation.

## References and How to report bugs
* [Cast Developer Documentation](http://developers.google.com/cast/)
* [Design Checklist](http://developers.google.com/cast/docs/design_checklist)
* If you find any issues with this library, please open a bug here on GitHub
* Question are answered on [StackOverflow](http://stackoverflow.com/questions/tagged/google-cast)

## How to make contributions?
Please read and follow the steps in the CONTRIBUTING.md

## License
See LICENSE

## Google+
Google Cast Developers Community on Google+ [http://goo.gl/TPLDxj](http://goo.gl/TPLDxj)

## Change List

2.1.1

 * Now th MediaRouter support library added back the support foer the volume on the cast dialog, so CCL is hiding that again.
 * Some typo fixes.

2.1

 * Added Queue related APIs for handling autoplay and queue
 * Added "stop" button to notification and lockscreen for live streams in Lollipop and above
 * Expanded callbacks in VideoCastConsumer interface to provide feedback on success of queue related API calls
 * Extended the full-screen VideoCastControllerActivity to include next/previous for navigation through queues.
  The visibility of these new buttons can be set through VideoCastManager.setNextPreviousVisibilityPolicy(policy)
 * The MiniController now has a modified UI with an additional item for showing an upcoming media item from the queue.
 * Addressed some issues

2.0.2

 * Addressing issues #171, #174
 * DataCastConsumer.onApplicationConnectionFailed() now returns void

2.0.1

 * Improving the management of MediaRouteButton
 * Working around a bug in Play Services, see issue #170
 * Fixing a typo

2.0
#### Notice: this release introduces a restructured and updated code base that has backward-incompatible changes. Most of these changes can be handled by a simple search-and-replace action but there are some changes to the API names and signatures that may need more manual handling. Below you can find a list of changes.

 * Change in the package name: CCL now has a new package name "com.google.android.libraries.cast.companionlibrary.cast"
 * All string, dimension and color resources now have "ccl_" as prefix. This allows developers to
 work with these resources without any collision with their own apps or other libraries. In addition, some
 unused resources have been removed from the "res/*" directories.
 * CCL no longer needs a reference to your "Activity" context. Instead, only an Application Context
 is adequate when you initialize it. Any API that may need an Activity Context (for example opening the
 VideoCastControllerActivity) will ask for such context as an argument. As a result, it is recommended
 to initialize the library in your Application's onCreate() and access the VideoCastManager singleton
 instance by VideoCastManager.getInstance(). Same applies to DataCastManager.
 * Most interface names have changed:
    * IMediaAuthListener -> MediaAuthListener
    * IMediaAuthService -> MediaAuthService
    * IBaseCastConsumer -> BaseCastConsumer
    * IDataCastConsumer -> DataCastConsumer
    * IVideoCastConsumer -> VideoCastConsumer
 * Some methods have been renamed:
    * IVideoVideoCastContoller#setLine1() -> VideoCastController#setTitle()
    * IVideoVideoCastContoller#setLine2() -> VideoCastController#setSubTitle()
    * IVideoVideoCastContoller#updateClosedCaption() -> VideoCastController#setClosedCaptionState()
    * VideoCastManager#getRemoteMovieUrl() -> getRemoteMediaUrl()
    * VideoCastManager#isRemoteMoviePlaying() -> isRemoteMediaPlaying()
    * VideoCastManager#isRemoteMoviePaused() -> isRemoteMediaPaused()
    * VideoCastManager#startCastControllerActivity() -> startVideoCastControllerActivity()
    * BaseCastManager#incremenetDeviceVolume() -> adjustDeviceVolume()
    * TracksPreferenceManager#setupPreferences() -> setUpPreferences()
    * VideoCastConsumer#onRemovedNamespace() -> onNamespaceRemoved()
    * MediaAuthService#start() -> startAuthorization()
    * MediaAuthService#setOnResult() -> setMediaAuthListener()
    * MediaAuthService#abort() -> abortAuthorization()
    * MediaAuthStatus#RESULT_AUTHORIZED -> AUTHORIZED
    * MediaAuthStatus#RESULT_NOT_AUTHORIZED -> NOT_AUTHORIZED
    * MediaAuthStatus#ABORT_TIMEOUT -> TIMED_OUT
    * MediaAuthStatus#ABORT_USER_CANCELLED -> CANCELED_BY_USER
    * VideoCastController#updateClosedCaption() -> setClosedCaptionStatus()
    * Utils#fromMediaInfo() -> mediaInfoToBundle()
    * Utils#toMediaInfo() -> bundleToMediaInfo()
    * Utils#scaleCenterCrop -> scaleAndCenterCropBitmap()
    * IMiniController.setSubTitle() -> setSubtitle()
    * MediaAuthListener#onResult() -> onAuthResult()
    * MediaAuthListener#onFailure() -> onAuthFailure()
    * BaseCastManager.clearContext() has been removed (see earlier comments)
 * All the "consumer" callbacks used to be wrapped inside a try-catch block inside the library. We have
 now removed this and expect the "consumers" to handle that in the client code; the previous approach was masking
 client issues in the library while they needed to be addressed inside the client itself.
 * BaseCastManager#addMediaRouterButton(MediaRouteButton button) now has no return value (it was redundant)
 * VideoCastConsumer#onApplicationConnectionFailed() no longer returns any value.
 * BaseCastConsumer#onConnectionFailed(() no longer returns any value.
 * [New] There is a new callback "void onMediaLoadResult(int statusCode)" in VideoCastConsumer to
 inform the consumers when a load operation is finished.
 * Updated the build to use the latest gradle binaries.
 * Updated to use the latest versions of Play Services and support libraries.

