# Stay Fit
## Description 

Stay Fit is an app for passionate in Gym. It provides users a variety of options to use while exercising such as muscle groups, equipment to use, exercises to do with the possibility to save data. This way users can create a weekly schedule plan for the workout. 

In addition, the progress of the workout can be saved so that users return to them during future sessions.

Also, with this app users can search for nearby gyms through Google Maps, instead of worrying to find one.

Data will be pulled from the Workout Manager API. (https://wger.de/en/software/api).

## Features

●	List of muscles to build, equipment to use and exercise to do.<br/>
●	Saves information of your workout plans<br/>
●	Search gyms through Google Maps based on device’s location<br/>
●	Generate workout plans given the parameters by the user<br/>
●	Widget that provides the chosen exercise<br/>

## Key Considerations

### How will your app handle data persistence? 

Data persistence will require a connection with SQLite database by extending with Content Provider.

### Describe any edge or corner cases in the UX.

App is navigating between each Activity- Fragment with Bottom Navigation Bar.
When user creates a workout, they will be able to follow the flow until the final screen to save the chosen exercise. 
User can search for a gym based on device’s location through navigation button that starts the activity for Find the nearest Gyms.
Also, by pressing the navigation button My workout plan, user starts the activity with the details of his saved exercises.

### Describe any libraries you’ll be using and share your reasoning for including them.

App uses Picasso.
App uses databinding.

### Describe how you will implement Google Play Services or other external services.

App uses Location to get device’s current location.
App uses Maps to get nearest gyms from the device’s Location
App uses AdMob for banner ads

### Next Steps: Required Tasks
#### Task 1: Project Setup

•	Update Android Studio to the latest Stable Version<br/>
•	Create project in Android Studio<br/>
•	Add a Bottom Navigation Activity for the MainActivity<br/>
•	Setup Gradle Dependencies<br/>
•	Configure libraries<br/>
•	Add permissions to manifest (such as Internet)<br/>

#### Task 2: Implement UI for Each Activity and Fragment

•	Build Fragments for each feature of the app (such as Muscles to build, Equipment to use)<br/>
•	Connect each Fragment to the MainActivity<br/>
•	App uses an AppBar and associated toolbars<br/>
•	App provides individual RecyclerView for each fragment<br/>
•	App provides Map placeholder for Gym Fragment<br/>
•	App provides a widget to provide relevant information to the user on the home screen<br/>

#### Task 3: Implement tasks to retrieve and load data from external API

•	Extend AsyncTask to fetch data from Workout Manager API<br/>
•	Create Network Utilities to build URLs and make API requests<br/>
•	Create Connection Utilities to check Internet Connectivity for every required action<br/>

#### Task 4: Implement Data Models and Content providers

•	Create Data models<br/>
•	Create SQLite db<br/>
•	Create Loaders and Adapters<br/>

#### Task 5: Impement Google Play Services

•	App uses Admob with placeholder views<br/>
•	App uses Location feature for getting device’s current location<br/>
•	App uses Maps feature with map placeholder to find nearby gyms (combo with Location)<br/>

#### Task 6: Designing for Tablets

•	Design app for tablets<br/>
•	Check compatibility<br/>

#### Task 7: Add Widget

•	Setup Widget to display given exercises<br/>

<a href="https://github.com/Al3xSav/Capstone-Project/blob/master/stayfit.apk">StayFit APK</a>
