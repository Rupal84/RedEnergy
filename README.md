# RedEnergy
This application makes use of java streams iterator to read csv file line by line instead of loading all lines at once into collection. Using collections would have made processing the records much easier but it may cause out of memory errors while processing larger files.

## Setup instructions.
1. Import extracted zip folder as maven project into your IDE (e.g. eclipse)
2. Check if its using jdk1.8. If not please add jdk1.8 to this project.
3. Right click on RedEnery project folder and select RunAs -> maven install.
4. Create new Run as Java Application configuration to launch simplenem12/TestHarness with absolute path to SimpleNem12.csv
5. Run newly created java application.
6. It should give following output. <br>
>`Total volume for NMI 6123456789 is -36.840000` <br/>
  `Total volume for NMI 6987654321 is 14.330000` 
7. To run unit tests, right click project folder, then select Run As -> maven test
