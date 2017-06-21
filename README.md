# SOAPUI Groovy Scripts Snippets

## For Snippet LoadTestDataFromExcelSheet.groovy
* Used jars:
1.	dom4j-1.6.1.jar
2.	xmlbeans-2.3.0.jar
3.	poi-3.8-20120326.jar
4.	poi-excelant-3.8-20120326.jar
5.	poi-ooxml-3.8-20120326.jar
6.	poi-ooxml-schemas-3.8-20120326.jar
7.	poi-scratchpad-3.8-20120326.jar

--	Copy and paste the required jar (mentioned above) files to the \lib folder of SoapUI. Versions of these jars can be of your desire

--	Add Groovy Script Step in test case before the step where the values are to be passed

--------------
--------------

## For CreateCSVReport.groovy
* Can be used at following places
1. As TearDown Script at Project/TestSuite/Testcase level
2. As groovy step in test case 

* Do not miss setting Project level custom property ProduceReports to 'true' else you can remove this line from snippet

** This will not work for each data row that will be passed using LoadTestDataFromExcelSheet.groovy **

--------------
--------------
