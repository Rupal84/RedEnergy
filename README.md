# RedEnergy
This application makes use of java streams iterator to read csv file line by line instead of loading all lines at once into collection. Using collections would have made processing the records much easier but it may cause out of memory errors while processing larger files.

## Setup instructions.
1. Import extracted zip folder as maven project into your IDE (e.g. eclipse)
2. Check if its using jdk1.8. If not please add jdk1.8 to this project.
3. Right click on RedEnery project folder and select RunAs -> Maven install.

## How to run Application
1. Open terminal and execute `mvn exec:java`
2. It will prompt to enter absolute file path. Provide the input and hit enter.
3. It should give following output. <br>
>`Total volume for NMI 6123456789 is -36.840000` <br/>
>  `Total volume for NMI 6987654321 is 14.330000` 

## Run unit tests
1. Open terminal window and execute `mvn test`
