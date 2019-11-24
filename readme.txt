Instructions:

A) use of the file -./settings.txt-:
	- contains the predefined SYSTEM (already parsed -> SAVE files created) to load on startup
	- 1st line -> SYSTEM_NAME
				EX.:	#SYSTEM_NAME_SAVED:#
						Dnsjava
	- 2nd line -> COMMIT_ID(last -> most recent commit)
				EX.:	#COMMIT_ID_SAVED:#
						d97b6a0685d59143372bb392ab591dd8dd840b61
B) ANALYZED SYSTEM SAVE files FOLDER:
	- It's created inside the folder -./saves/-
	- a SUBFOLDER with the SYSTEM NAME
	- and inside that folder there is another SUBFOLDER named accordingly to the COMMIT_ID(last -> most recent commit)
C) SYSTEMs to be analyzed:
	- inside the folder -./files/-
	- create a FOLDER with the SYSTEM NAME
	- and inside that folder another SUBFOLDER named accordingly to the COMMIT_ID(last -> most recent commit)
	- these FOLDERs presence will be noted during startup
	C.1) necessary files:
		1- commit_list.txt -> produced by SOURCETREE-GIT -> contains the LOG with the COMMIT HISTORY (most recent -> oldest commit)
			git log -p --date=iso --full-index --pretty=format:"###COMMIT:%n%H%n%cn%n%ce%n%cd%n%P%n%s" >commit_list.txt
		2- xxx-clones.xml -> produced by NiCad -> contains the CLONES found by NiCad inside the SYSTEM to be imported
			./nicad5 functions java systems/Dnsjava default-report
		3- xxx-clones-classes.xml -> produced by NiCad -> contains the CLONE CLASSES found by NiCad inside the SYSTEM to be imported
			./nicad5 functions java systems/Dnsjava default-report
		4- xxx-clones-classes-withsource.html -> produced by NiCad -> contains the LINES of CODE of the CLONES found by NiCad inside the SYSTEM to be imported
			./nicad5 functions java systems/Dnsjava default-report
			(NOTES: it's available a similar .xml file too)
			(NOTES: clone lines can be read DIRECTLY from the files analyzed by NiCad -> EX.: Report_NiCad5_Builder.getCloneLinesFromOriginFileAnalyzedByNiCad())
		5- settings-nicad.txt -> contains all the NiCad necessary parsing settings:
			C.1.5.1) #NICAD_ROOT_FULLPATH:#
					EX.:	C:/cygwin64/home/<YOURPCUSERNAME>/NiCad-5.0/
			C.1.5.2) #NICAD_ANALYZEDSYSTEMS_ROOT_FOLDER_NAME:#
					EX.:	systems
			C.1.5.3) #NICAD_SYSTEM_VERSION_COMMIT:#
					EX.:	d97b6a0685d59143372bb392ab591dd8dd840b61
			C.1.5.4) #NICAD_CLONE_CLASSES_FILE:#
					EX.: Dnsjava_functions-blind-clones-0.30-classes.xml
			C.1.5.5) #NICAD_CLONE_LINES_FILE:#
					EX.:	Dnsjava_functions-blind-clones-0.30-classes-withsource.html
			C.1.5.6) #NICAD_CLONE_PAIRS_FILE:#
					EX.:	Dnsjava_functions-blind-clones-0.30.xml
		6- settings-git.txt -> contains all the GIT REPO necessary parsing settings:
			C.1.6.1) #REPO_PATH_URI:#
					EX.:	https://github.com/dnsjava/dnsjava.git
			C.1.6.2) #REPO_LOCAL_PATH:#
					EX.:	./repo_4G1/
			C.1.6.3) #SYSTEM_NAME:#
					EX.:	Dnsjava
D) NiCad SETTINGS:
	- If you run under Windows O.S.
	- you probably MUST use CYGWIN to RUN NiCad.
	- So, just CREATE inside the CYGWIN folder
	- (preferably inside its -/home/<YOURPCUSERNAME>/- SUBFOLDER)
	- a SUBFOLDER for NiCad.
		EX.:	C:/cygwin64/home/<YOURPCUSERNAME>/NiCad-5.0/
	- Inside this NiCad FOLDER, you MUST create a SUBFOLDER (<ANALYZED_SYSTEMS>)
	- where to put the SYSTEMS you want to be analyzed by NiCad.
		EX.:	C:/cygwin64/home/<YOURPCUSERNAME>/NiCad-5.0/systems/
	D.1) For analyzing more than one version of the same SYSTEM,
	this kind of FOLDER GERARCHY is used:
		1- -/SYSTEM_NAME/- SUBFOLDER inside -/<ANALYZED_SYSTEMS>/- to collect ALL VERSIONS of the SYSTEM analyzed;
		2- -/SYSTEM_VERSION/- SUBFOLDER inside -/SYSTEM_NAME/-
		3- -/SYSTEM_NAME/- SUBFOLDER inside -/SYSTEM_VERSION/- because NiCad SET THIS NAME <SYSTEM_NAME> inside its REPORT FILES as SYSTEM NAME.
			EX.:	C:/cygwin64/home/<YOURPCUSERNAME>/NiCad-5.0/systems/Dnsjava/d97b6a0685d59143372bb392ab591dd8dd840b61/Dnsjava/
		NOTE: this FULL GERARCHY is NEEDED only if you want to use some of the Report_NiCad BUILDER METHODS like:
			- getCloneLinesFromOriginFileAnalyzedByNiCad();
			- getCloneLinesFromOriginFileAnalyzedByNiCadWithoutComments();
			It's not strictly necessary if you use, for example, this other METHOD:
			- getCloneLinesFromOriginFileDLDByCheckoutCommand();
	D.2) RUN NiCad following these steps (if under Windows O.S., RUN through CYGWIN):
		1- go inside NiCad FOLDER
			EX.:	cd NiCad-5.0
		2- RUN NiCad with your chosen SETTINGS:
			D.2.2.1) <NICAD COMMAND>: nicad5
			D.2.2.2) <NICAD GRANULARITY>: functions/blocks
			D.2.2.3) <PROGRAMMING LANGUAGE ANALYZED SYSTEM>: java/c/py etc..
			D.2.2.4) <ANALYZED SYSTEM SUBFOLDER PATH>: systems/Dnsjava/d97b6a0685d59143372bb392ab591dd8dd840b61/Dnsjava
			D.2.2.5) <ANALYSIS SETTINGS FILE>: default-report
						where to SET (mainly):
						- minlinenumber -> clone MIN LINE NUMBER
						- maxlinenumber -> clone MAX LINE NUMBER
						- threshold -> clone SIMILARITY-DIFFERENCY (from 0.0->Type1 CLONES to 1.0)
				EX.:	./nicad5 functions java systems/Dnsjava/d97b6a0685d59143372bb392ab591dd8dd840b61/Dnsjava default-report
		3- NOTE: FOR MORE INFO on the AVAILABLE OPTIONS, check NiCad README files.
E) DEPENDENCIES:
	- This projects was developped with Eclipse IDE, using JDK 1.8.0_202
	- ADDITIONAL LIBRARIES:
		This software needs the following libraries linked to compile/run:
			1- JGit: org.eclipse.jgit-5.3.0.201903130848-r.jar
			2- log4j: log4j-1.2.17.jar
			3- slf4j: slf4j-api-1.7.26.jar
			4- slf4j-log4j12: slf4j-log4j12-1.7.26.jar
			5- jsch: jsch-0.1.55.jar
		For example, you can create a folder /libraries/ where to put those .jar files and link them to your classpath.
		Or you can use Maven Repository, as well.
F) RUN:
	- MAIN CLASS GUI STARTER:
		view.Splash_Screen (PACKAGE view, class Splash_Screen)
	NOTE: if you create a RUNNABLE JAR, you need to have the following files in the same directory as the .jar file, if you want to load previous analysis:
		- settings.txt FILE (LAST SAVED SYSTEM ANALYSIS)
		- /saves/ FOLDER (SAVED SYSTEM ANALYSIS)
		- /files/ FOLDER (SAVED SYSTEM ANALYSIS SETTINGS, at least settings_git.txt and settings_nicad.txt inside a /files/system_name/system_version/ FOLDER, SEE EXAMPLES)
		- (OPTIONAL) /libraries/ FOLDER (DEPENDENCIES, SEE ABOVE, if you choose to create the .jar FILE without packaging the required libraries into generated JAR)

ENJOY IT and SPREAD the KNOWLEDGE!	