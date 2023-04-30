# learningApp

# Introduction
Using git to track your commits and your IDE and test framework of choice, implement the following Android application requirements:

1. Parent can login themselves with username/password 
2. Parent can login their child without entering the child's username/password
3. Child can login themselves with username/password
4. Parent can logout
5. Child can logout, either leaving an active parent session or no sessions 

# Approach
- We first setup a POSTMAN collection with the given request and response to create the different api's for parentLogin, childLogin, logging in child through parent and logging in the child without username and password.
- The postman collection has exmaples of the request and response which the apis need to emulate.
- There are scripts written inside the postman collection for token storage
- We create an android application based on the apis created and build a basic UI to handle the different scenarios(parentLogin, childLogin etc)
- We use Jetpack Compose & MVVM Design pattern for the View Layer and Repository Pattern for the Data Layer 
- For only when a child is allowed login through the parent, i.e child logged in through parent we use an HTTP Authorization header e.g. `Authorization: Bearer <auth-token>` for the api call
- Given more time I would have covered few more scenarios and written down the unit tests


# Installation
Clone the repo and install the dependencies.

     https://github.com/maquadir/learningApp.git
  
# Architecture and Design
The application follows an MVVM architecture as given below

<img width="449" alt="Screen Shot 2019-12-25 at 8 05 55 AM" src="https://user-images.githubusercontent.com/19331629/71425127-6ca3cc00-26ed-11ea-98b5-a344b54b7050.png">

# Setup
# Manifest File
Since the app is going to fetch from json url .We have to add the following internet permissions to the manifest file.

    <uses-permission android:name="android.permission.INTERNET" />
  
# Data Layer
Created a Collection in Postman. We will have to create a mock server off of the collection and use the apis in our android project
We have declared a API interface to invoke the api using Retrofit.Builder()
A ApiService class to handle api requests and a repository takes care of how data will be fetched from the api.

          val apiService = ApiService()
          val repository = Repository(apiService)
          
Invoke api url using Retrofit.Builder()

          Retrofit.Builder()
                .baseUrl("https://18e2d77a-1039-4ecf-8f70-14bcbabf3e47.mock.pstmn.io")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
# Model
A Model contains all the data classes. 
     
      AuthData
      BodyRequest
      Parent
      Student

# View Model
We set up a view model factory which is responsible for creating view models.It contains the data required in the View and translates the data which is stored in Model which then can be present inside a View through the observable Livedata.

# Coroutines
Coroutines are a great way to write asynchronous code that is perfectly readable and maintainable. We use it to perform a job of requesting data from the api.

# Local Storage using SharedPreferences
We have used Sharedpreferences to locally store the tokens for parent and child

# View
It is the UI part that represents the current state of information that is visible to the user.We are setting up the UI using Jetpack Compose

# Getting the data from the below apis
  
    parentLogin(/login) - enable parent login using username and password
    childLogin( /login) - enable child login using username and password
    parent loggin in child(/login/student/) - enable parent to login child
    child login using parent token(/loginchildtoken/) - enable the logged in child(from parent) to login using id and token w/o using username and password


# Screenshots

- Sign in as Parent and Sign in as Child

<img width="249" alt="image" src="https://user-images.githubusercontent.com/19331629/235332814-3ff0a025-44b0-483b-a481-a65aaa51ed72.png">.  <img width="203" alt="Screenshot 2023-04-30 at 12 35 06 pm" src="https://user-images.githubusercontent.com/19331629/235332844-06d82e18-efef-4488-8a27-c18797f5c5da.png">


- Signing in as parent displays the student ids associated with the parent.
- Upon selecting one student - the student would be able to sign in without username and password.
- Upon logout both parent and child sessions will be logged out.

<img width="219" alt="Screenshot 2023-04-30 at 12 34 37 pm" src="https://user-images.githubusercontent.com/19331629/235332847-24c621ee-640b-4e34-b970-77f0512168bd.png">. <img width="198" alt="Screenshot 2023-04-30 at 12 35 44 pm" src="https://user-images.githubusercontent.com/19331629/235332871-8f03f1c5-e9a9-4258-988f-003ce267afa4.png">. <img width="170" alt="Screenshot 2023-04-30 at 12 35 51 pm" src="https://user-images.githubusercontent.com/19331629/235332885-fa518224-9aba-4f71-aaf8-47518ba914ec.png">


