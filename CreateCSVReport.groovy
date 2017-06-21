try {
	def groovyUtils = new com.eviware.soapui.support.GroovyUtils(context)
	def projectPath = groovyUtils.projectPath.replace("\\", "\\\\")
    
	//Check a project level property to see if the reports should be produced.
    if(context.expand('${#Project#ProduceReports}') == 'true') {
        //The path and file to persist results
    //    def resultDir = new File(projectPath + "\\SoapUI_CSV_Reports");
        def resultDir = new File("C:\\SoapUI Projects\\MyAPIproject\\SoapUI_CSV_Reports");
		
        if(!resultDir.exists()) {
            resultDir.mkdirs();
        }
        
		def currentDateStamp = new Date().format("yyyy-MM-dd");
		def resultsFile = new File(resultDir, currentDateStamp + " - " + testRunner.testCase.testSuite.project.name + "_TestReport.csv");

        //If the file does not already exist, we want to create it, otherwise we want to append
        if(!resultsFile.exists()) {
            resultsFile.createNewFile();
            //Header values
            resultsFile.write('"TEST_SUITE_NAME","TEST_CASE_NAME","RESULT","MESSAGES","DATE"');
        }

        //Write the result values you desire to capture
        resultsFile.append('\n');    //Newline
		resultsFile.append('"' + testRunner.testCase.testSuite.name + '",');    //Test Suite Name
        resultsFile.append('"' + testRunner.testCase.name + '",');    //Test Case Name
        resultsFile.append('"' + testRunner.status + '",');    //Overall Test Case result

        //There can be multiple messages, so set up a loop (log in the test case)
        resultsFile.append('"');    //Start of messages.
		for(result in testRunner.getResults()) {
			//There can be lots of messages, so limit amount recorded to avoid going over
			//the buffer size for a CSV field
			msgCount = 0;
			for(message in result.getMessages()) {
				msgCount++;
				if(msgCount < 10) {
					resultsFile.append(message + ';\n');
				}
			}
			
		}
		//This will not let the message cell blank in case there is no message
		if(testRunner.getReason() == null){
			resultsFile.append('-Success-')
		}


        resultsFile.append('",');    //End of messages.

        def currentDate = new Date().format("yyyy-MMM-dd hh:mm:ss a");
        resultsFile.append('"' + currentDate + '"'); //Date
    }
} catch(e) {
    log.error("An error occurred: " + e.toString());
}
