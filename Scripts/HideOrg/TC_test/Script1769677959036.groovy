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

WebUI.openBrowser('')

WebUI.navigateToUrl('https://qa.engagement.magiqdev.cloud/login')

WebUI.click(findTestObject('Object Repository/test/Page_Community Engagement/input_person_userName'))

WebUI.doubleClick(findTestObject('Object Repository/test/Page_Community Engagement/input_person_userName'))

WebUI.setText(findTestObject('Object Repository/test/Page_Community Engagement/input_person_userName'), 'admin1test@gmail.com')

WebUI.setEncryptedText(findTestObject('Object Repository/test/Page_Community Engagement/input_visibility_matBlazor_id_17461327-49e3_a620fb'), 
    'vckfKYHQ/X5a0LrHpsIEKA==')

WebUI.click(findTestObject('Object Repository/test/Page_Community Engagement/div_Continue as Guest_mdc-button__ripple'))

WebUI.click(findTestObject('Object Repository/test/Page_Community Engagement/div_Introduce Settings_mdc-button__ripple'))

WebUI.click(findTestObject('Object Repository/test/Page_Community Engagement/input_Hide Organisation Menu Items_multisel_a39ec4'))

WebUI.click(findTestObject('Object Repository/test/Page_Community Engagement/li_News'))

WebUI.click(findTestObject('Object Repository/test/Page_Community Engagement/input_Hide Organisation Menu Items_multisel_a39ec4'))

WebUI.click(findTestObject('Object Repository/test/Page_Community Engagement/li_Connect'))

WebUI.click(findTestObject('Object Repository/test/Page_Community Engagement/input_Hide Organisation Menu Items_multisel_a39ec4'))

WebUI.click(findTestObject('Object Repository/test/Page_Community Engagement/li_Notices'))

