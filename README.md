# Copilot
Application to detect road signs using an android phone.  

# Structure

copilot - root project  
copilot-android - the android application  
copilot-core - most of the code sits in this project

# Description
The android application uses opencv to access the phones camera video feed.  Each frame of the video feed (a Mat object) is passed into copilot.java which detects a sign within the image, keeps track of the sign (so it doesn't alert twice), attempts to identify the sign (currently using feature detection), highlights and labels the sign on the image and causes an audible alert on the device if it is a new sign.  The frame with augmented sign details is returned to the android activity where it is displayed on the phones display. 

# Setting up

It requires OpenCV computer vision library to be downloaded and OpenCV manager installed on your device (to obtain the correct
native libraries).  

In android studio the easiest way to get the java libraries is to import it as a module into the copilot-android project.  Currently
the application is using opencv 2.4.8 so if using another version then the code in milesb.copilot.android.CopilotMainActivity will
need to be changed accordingly

The android native libraries are present in the opencv download but at the time of writing the recommended approach was to install
opencv manager from google play onto your device.  Alternatively there are apk files in the opencv that can be installed directly
onto your device.

# Notes:

The local.properties file is not in github.  It contains the location of your own sdk for android studio and is created automatically.  However it needs to be moved to the root directory (under copilot) for it to work.
