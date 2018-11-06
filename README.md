# copilot
Application to detect road signs using an android phone.  

Structure
copilot - root project
  copilot-android - the android application
  copilot-core - most of the code sits in this project

Setting up
It requires OpenCV computer vision library to be downloaded and OpenCV manager installed on your device (to obtain the correct
native libraries).  

In android studio the easiest way to get the java libraries is to import it as a module into the copilot-android project.  Currently
the application is using opencv 2.4.8 so if using another version then the code in milesb.copilot.android.CopilotMainActivity will
need to be changed accordingly

The android native libraries are present in the opencv download but at the time of writing the recommended approach was to install
opencv manager from google play onto your device.  Aleternatively there are apk files in the opencv that can be installed directly
onto your device.