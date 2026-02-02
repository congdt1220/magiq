import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

WebUI.setText(findTestObject('OP/TO_full_name'), 'Admin test 2')

WebUI.setText(findTestObject('OP/TO_phone_number'), '123456789')

WebUI.setText(findTestObject('OP/TO_address'), 'address 123')

WebUI.scrollToElement(findTestObject('OP/TO_edit_details'), 5)

WebUI.click(findTestObject('OP/TO_edit_details'))

WebUI.callTestCase(findTestCase('OP/OP1.6'), [:], FailureHandling.STOP_ON_FAILURE)

WebUI.verifyElementAttributeValue(findTestObject('OP/TO_full_name'), 'value', 'Admin test 2', 5)

WebUI.verifyElementAttributeValue(findTestObject('OP/TO_phone_number'), 'value', '123456789', 5)

WebUI.verifyElementAttributeValue(findTestObject('OP/TO_address'), 'value', 'address 123', 5)

