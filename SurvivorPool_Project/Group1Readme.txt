
*************************************************************************************
Instruction Order and Notes
*************************************************************************************
1. Do the Blackberry Instructions first!
   It will set up the SDCard.  Windows relies on the existence of this.

2) In the Windows side, we have harded coded the path for the SDCard.

3) The instructions will tell you how to create the SDcard.

4)  If there are any IO errors, it is entirely due the SDcard card setup.
    



*************************************************************************************
Running the BlackBerry Code
*************************************************************************************
1. In Eclipse with the BlackBerry plug-in go to File>>Import

2. Select "Import existing project into workspace"

3. Browse to the "Group1BlackBerry.jar" file as an archive file to import

4. Select UWOSurvivorPool and hit Finish. This should create the project in Eclipse
   with all of the proper directory structure.

5. Go to Run>>Run Configurations

6. Create a BlackBerry Simulator run configuration for UWOSurvivorPool

7. Select BlackBerry JRE 6.0.0 as the JRE and the BlackBerry 9800 as the simulator

8. Under the Simulator>>Memory tab selet "PC filesystem" for the SD card and
   provide a directory to create the SD card.  Provide " C:\SDCard\" as the directory.

9. Run the simulator once and close it once it finishes loading. This will generate the
   necessary SD card directory structure

10. In windows explorer navigate to the SD card directory you created and copy all
    text files in the "res" directory of the project into the "C:\SDCard\BlackBerry\" directory

11. Run the simulator and the application should launch normally



*************************************************************************************
Running the Standalone Code
*************************************************************************************

First Try this.  If it does not work, try the other installation scheme.


---------------
First Try This
--------------

1. In Eclipse go to File>>Import

2. Select General>>Existing Projects Into Workspace

3. Import Group1Standalone.jar as an archive file. This
   should create the project in your workspace with all
   of the proper files and directory structure.

4. Run Login.java and rejoice.

5. We suggest downloading the following pictures and using them 
   as the Contestant Pictures.
    
   A very cute dog!
   http://en.wikipedia.org/wiki/File:YellowLabradorLooking_new.jpg

   A Monkey!
   http://ehdwallpapers.com/view-funny-chimp-monkey-1024x768.html
 

   Have fun!



---------------------------
Other Installation Scheme
---------------------------
1. In Eclipse (the normal one), go to File>>New Project

2. Create a New Java Project (name it whatever you want)

3) In the Package explorer, right click your New Java project.

4) Press "Import".

5) Choose General/Archive File

6) Browse and find our archive file
   -- please make sure (.classpath and .project)

7) Press Finish

8) When prompted with "Overwrite '.classpath', in folder...press "yes to all"

9)  Your "New Project" should now have all our .java files

10) Double Click on Login.java in the package explorer 

11)  Press Ctrl f11 (or whatever you do to "run" your java programs)

12) Now you should be able to test the program.




NOTE: You may need to change the java compiler version. If the project does not compile
right click on the project in the package explorer. Go to Properties>>Java Compiler and
change the compiler version to 1.4



