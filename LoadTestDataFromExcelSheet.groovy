import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.ss.util.*;


//Extra Code to get file from project path
		
class ExcelReader {
		
  def readData(def projectLocation) {

		def path = projectLocation + "\\TestData.xlsx";

  	//	def path = "C:\\soapui-project\\TestData.xlsx";
		InputStream inputStream = new FileInputStream(path);
		Workbook workbook = WorkbookFactory.create(inputStream);
		Sheet sheet = workbook.getSheet("ExcelSheetName");
                      
		Iterator rowIterator = sheet.rowIterator();
		rowIterator.next()
		Row row;                       
		def rowsData = []
		while(rowIterator.hasNext()) {
			row = rowIterator.next()
			def rowIndex = row.getRowNum()
			def colIndex;
			def rowData = []
			for (Cell cell : row) {
				colIndex = cell.getColumnIndex()
				 
				//you can add more cases if you like
				switch (cell.getCellType()) {
					//Cell type is string 
				 	case Cell.CELL_TYPE_STRING:
					 	rowData[colIndex] = cell.getStringCellValue();
						break;
					//Cell type is Numeric
					case Cell.CELL_TYPE_NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							rowData[colIndex] = cell.getDateCellValue().toString();
						}
						else {
                        	rowData[colIndex] = cell.getNumericCellValue().toLong().toString();
						}
						break;
					//Cell type is Formula and is able to handle both Numeric and non-numeric formulas
					case Cell.CELL_TYPE_FORMULA:
						if (cell.getCachedFormulaResultType() == Cell.CELL_TYPE_NUMERIC){
							rowData[colIndex] = cell.getNumericCellValue().toLong().toString();
						}
						else{
							rowData[colIndex] = cell.getStringCellValue();
						}
						break;
					//Cell is left blank
					case Cell.CELL_TYPE_BLANK:
						rowData[colIndex] = "";
						break;				
					//Cell type is Boolean
					case Cell.CELL_TYPE_BOOLEAN:
						rowData[colIndex] = cell.getBooleanCellValue().toString();
						break;
					default:
						rowData[colIndex] = cell.getStringCellValue();
				}
			}                    
			rowsData << rowData
		}
		rowsData
	}
}

def groovyUtils = new com.eviware.soapui.support.GroovyUtils(context)


def projectPath = groovyUtils.projectPath.replace("\\", "\\\\")
//log.info("The Location is : " + projectPath )
def myTestCase = context.testCase

ExcelReader excelReader = new ExcelReader();
List rows = excelReader.readData(projectPath);
def d = []
Iterator i = rows.iterator();
	while( i.hasNext()){
         d = i.next();
         	
       	context["SN"] = d[0]
      	context["Scenario"] = d[1]
      	context["Expected_Output"] = d[2]
      	context["ExpectedStatusCode"] = d[3]
		//Add as many as context variables you have to use to store data from excel 
		
      testRunner.runTestStepByName("teststepname")
      //To log any info you like
		  log.info("The Serial Number is : " + d[0])
          
		  //Only added to overcome NOT RESPONDING situation of SoapUI
		  sleep(1000)
		  
	}
 
