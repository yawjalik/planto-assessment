## Full-stack take-home assessment

### Intro

This task assesses the following:

1. architecture design (data flows, efficiency of algorithms for eg making sure rerenders on reactjs are optimised, db reads are optimised on the backend, db design)
2. code quality (well-written code, following best practices, well named variables, isolation of logic etc)
3. testing (handle edge cases, error handling and unit/integration tests)


The tech stack required to be used here is:
1. Java Spring Boot for backend (you can use Java 17 or higher)
2. NextJS with Typescript for the frontend 
3. Postgres for the database


### Getting Started

This folder contains skeletons of both frontend and backend to help you get started. 

### csv-editor-fe
* NextJs based frontend. 
* Styling through css-in-js using emotion's styled components.
* There is an example page to help you get started. The example page uses `fetch` for http API, you can use whatever library you prefer. We use axios at Planto.
* Can be started using `yarn run dev`
* Follow the mockup fe-design.png file for design guideline. Keep the design simple (similar to the mockup).

### csv-editor-be
* Spring Boot based backend.
* Example entity, service, and controller provided to help get started quickly.
* If opening using IntelliJ, you can go to FullstackAssessmentApplication file and start the app using the "play" button which shows up on the code sidepane.
* Requires startDb.sh to have been run once.

### startDb.sh
* Requires docker to be installed
* Starts a minimal postgres docker image on port 5432



### Requirements

You are required to create a csv editor web-app (similar to a spreadsheet).

Assumptions you can make:
* CSVs are restricted to a strict format with any 5 columns of your choice.

There should be two main screens:
1. A screen which shows a list of uploaded csvs (and an upload button allowing upload of files)
2. A screen which allows editing of a given uploaded csv in a spreadsheet-like editor which also has a 'save' button

The user flow should be:
1. upon opening the web-app, the user should be shown screen 1
2. if they want to upload a new csv, they can upload it on screen 1. 
3. once a file is uploaded, it should keep the original file name and show in the list of uploaded files on screen 1.
4. each item in the file list should have an edit button on it
5. clicking an edit button should open screen 2
6. screen 2 should allow users to make changes on a spreadsheet like interface 
7. changes should save with a save button
8. once saved, the user goes back to screen 1

Please share your frontend and backend code via a zip file uploaded to google drive or a publicly available storage provider.


Any questions can be sent to taha@planto.io or yousaf@planto.io.
