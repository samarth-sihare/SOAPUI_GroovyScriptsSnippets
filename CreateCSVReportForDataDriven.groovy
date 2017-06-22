try {
	def groovyUtils = new com.eviware.soapui.support.GroovyUtils(context)
	def projectPath = groovyUtils.projectPath.replace("\\", "\\\\")
	def testStep = testRunner.testCase.testSteps["myTestStepWithAssertion"]
    
	//Check a project level property to see if the reports should be produced.
	if(context.expand('${#Project#ProduceReports}') == 'true') {
        //The path and file to persist results
	   def resultDir = new File(projectPath + "\\SoapUI_CSV_Reports");
       //def resultDir = new File("C:\\SoapUI Projects\\MyAPIproject\\SoapUI_CSV_Reports");
		
        if(!resultDir.exists()) {
            resultDir.mkdirs();
        }
        
		def currentDateStamp = new Date().format("yyyy-MM-dd");
		def resultsFile = new File(resultDir, currentDateStamp + " - " + testRunner.testCase.testSuite.project.name + "_TestReport.csv");
		
		def Assertion1 = testStep.getAssertionByName("ValidateResponseHTTP_StatusCodes").status.toString()
		def Assertion2 = testStep.getAssertionByName("ScriptAssertion-ValidateResponseResult").status.toString()
		def overAllStatus = 'FAIL'

		if(Assertion1 == 'VALID' && Assertion2 == 'VALID'){
			overAllStatus = 'PASS'
		}

		
        //If the file does not already exist, we want to create it, otherwise we want to append
        if(!resultsFile.exists()) {
            resultsFile.createNewFile();
            //Header values
            resultsFile.write('"DATE","TEST_SUITE_NAME","TEST_CASE_NAME","SCENARIO","ASSERT_RESPONSE_CODE","ASSERT_RES_RESULT","TEST_RESULT","MESSAGES"');
        }

        //Write the result values you desire to capture
		resultsFile.append('\n');    //Newline
		
		def currentDate = new Date().format("yyyy-MMM-dd hh:mm:ss a");
		resultsFile.append('"' + currentDate + '",'); //Date
		
		resultsFile.append('"' + testRunner.testCase.testSuite.name + '",');    //Test Suite Name
		resultsFile.append('"' + testRunner.testCase.name + '",');    //Test Case Name
		resultsFile.append('"' + context.expand('${#Scenario}') + '",');    //Test Scenario
		resultsFile.append('"' + Assertion1 + '",');    //Assertion result
		resultsFile.append('"' + Assertion2 + '",');    //Assertion result
		resultsFile.append('"' + overAllStatus + '",');    //Overall Test Case result

        //There can be multiple messages, so set up a loop (log in the test case)
		resultsFile.append('"');    //Start of messages.

		for(assertion in testStep.assertionList) {
			//There can be lots of messages, so limit amount recorded to avoid going over the buffer size for a CSV field
			msgCount = 0;
			for(message in assertion.errors){
				msgCount++;
				if(msgCount < 10) {
					resultsFile.append(message.toString() + ';\n');
				}
			}
		}
		//This will not let the message cell blank in case there is no message
/*		if(testStep.assertionList.errors){
			resultsFile.append('-NA-')
		}
*/
        resultsFile.append('"');    //End of messages.
        
    }
}catch(e) {
    log.error("An error occurred: " + e.toString());
}
