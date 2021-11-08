# Super*Duper*Drive Cloud Storage
You have been hired by Super*Duper*Drive, which is a brand new company aiming to make a dent in the Cloud Storage market and is already facing stiff competition from rivals like Google Drive and Dropbox. That hasn't dampened their spirits at all, however. They want to include personal information management features in their application to differentiate them from the competition, and the minimum viable product includes three user-facing features:

1. **Simple File Storage:** Upload/download/remove files
2. **Note Management:** Add/update/remove text notes
3. **Password Management:** Save, edit, and delete website credentials.  

Super*Duper*Drive wants you to focus on building the web application with the skills you acquired in this course. That means you are responsible for developing the server, website, and tests, but other tasks like deployment belong to other teams at the company. 

## Starter Project
A senior developer is assigned to be your tech lead and mentor, and they put together a starter project for you. It's a Maven project configured for all the dependencies the project requires.

Your tech lead already designed a database schema for the project and has added it to the `src/main/resources` directory. That means I didn't have to design the database, only develop the Java code to interact with it. 

Your tech lead also created some HTML templates from the design team's website mockups, and they placed them in the `src/main/resources/templates` folder. These are static pages right now, and I had to configure them with Thymeleaf to add functionality and real data from the server I developed. I also had to change them to support testing the application.

## Requirements and Roadmap
Your tech lead was excited to work with me and laid out a development roadmap with requirements and milestones. They told me that there are three layers of the application I needed to implement:

1. The back-end with Spring Boot
2. The front-end with Thymeleaf
3. Application tests with Selenium

### The Back-End
The back-end is all about security and connecting the front-end to database data and actions. 

1. Managing user access with Spring Security
 - Restrict unauthorized users from accessing pages other than the login and signup pages. Created a security configuration class that extends the `WebSecurityConfigurerAdapter` class from Spring. This class is in a package reserved for security and configuration. This package is called `security`.
 - Spring Boot has built-in support for handling calls to the `/login` and `/logout` endpoints. I used the security configuration to override the default login page with one of my own, discussed in the front-end section.
 - I implemented a custom `AuthenticationProvider` which authorizes user logins by matching their credentials against those stored in the database.  


2. Handling front-end calls with controllers
 - I wrote controllers for the application that bind application data and functionality to the front-end. That meant using Spring MVC's application model to identify the templates served for different requests and populating the view model with data needed by the template. 
 - The controllers I wrote are also responsible for determining what, if any, error messages the application displays to the user. When a controller processes front-end requests, it delegates the individual steps and logic of those requests to other services in the application, but it interprets the results to ensure a smooth user experience.
 - My controllers are kept in a single package to isolate the controller layer. I simply call this package `controller`!
 - Simple, repetitive tasks are abstracted and reused as services.


3. Making calls to the database with MyBatis mappers
 - Since I was provided with a database schema to work with, I designed Java classes to match the data in the database. These are POJOs (Plain Old Java Objects) with fields that match the names and data types in the schema, and I created one class per database table. These classes are placed in the `model` package.
 - To connect these model classes with database data, implemented MyBatis mapper interfaces for each of the model types. These mappers have methods that represent specific SQL queries and statements required by the functionality of the application. They support the basic CRUD (Create, Read, Update, Delete) operations for their respective models at the very least. I placed these classes in (you guessed it!) the `mapper` package.


### The Front-End
My tech lead did a thorough job developing HTML templates for the required application pages. They have included fields, modal forms, success and error message elements, as well as styling and functional components using Bootstrap as a framework. I edited these templates and inserted Thymeleaf attributes to supply the back-end data and functionality described by the following individual page requirements:

1. Login page
 - Everyone is allowed access to this page, and users can use this page to login to the application. 
 - Show login errors, like invalid username/password, on this page. 


2. Sign Up page
 - Everyone is allowed access to this page, and potential users can use this page to sign up for a new account. 
 - Validate that the username supplied does not already exist in the application, and show such signup errors on the page when they arise.
 - Remember to store the user's password securely!


3. Home page
The home page is the center of the application and hosts the three required pieces of functionality. The existing template presents them as three tabs that can be clicked through by the user:


 i. Files
  - The user should be able to upload files and see any files they previously uploaded. 

  - The user should be able to view/download or delete previously-uploaded files.
  - Any errors related to file actions should be displayed. For example, a user should not be able to upload two files with the same name, but they'll never know unless you tell them!


 ii. Notes
  - The user should be able to create notes and see a list of the notes they have previously created.
  - The user should be able to edit or delete previously-created notes.

 iii. Credentials
 - The user should be able to store credentials for specific websites and see a list of the credentials they've previously stored. If you display passwords in this list, make sure they're encrypted!
 - The user should be able to view/edit or delete individual credentials. When the user views the credential, they should be able to see the unencrypted password.

The home page has a logout button that allows the user to logout of the application and keep their data private.

### Testing
Your tech lead trusts you to do a good job, but testing is important whether you're an excel number-cruncher or a full-stack coding superstar! The QA team at Super*Duper*Drive carries out extensive user testing. Still, your tech lead wanted me to write some simple Selenium tests to verify user-facing functionality and prove that your code is feature-complete before the testers get their hands on it.

1. Wrote tests for user signup, login, and unauthorized access restrictions.
 - Wrote a test that verifies that an unauthorized user can only access the login and signup pages.
 - Wrote a test that signs up a new user, logs in, verifies that the home page is accessible, logs out, and verifies that the home page is no longer accessible. 


2. Wrote tests for note creation, viewing, editing, and deletion.
 - Wrote a test that creates a note, and verifies it is displayed.
 - Wrote a test that edits an existing note and verifies that the changes are displayed.
 - Wrote a test that deletes a note and verifies that the note is no longer displayed.


3. Wrote tests for credential creation, viewing, editing, and deletion.
 - Wrote a test that creates a set of credentials, verifies that they are displayed, and verifies that the displayed password is encrypted.
 - Wrote a test that views an existing set of credentials, verifies that the viewable password is unencrypted, edits the credentials, and verifies that the changes are displayed.
 - Wrote a test that deletes an existing set of credentials and verifies that the credentials are no longer displayed.
