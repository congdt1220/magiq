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
import java.awt.Robot as Robot
import java.awt.event.KeyEvent as KeyEvent

WebUI.openBrowser('')

WebUI.navigateToUrl('https://qa.engagement.magiqdev.cloud/organisation')

WebUI.waitForElementClickable(findTestObject('Location/btn_location'), 120)

WebUI.click(findTestObject('Location/btn_location'))

WebUI.waitForElementClickable(findTestObject('Location/btn_upload'), 120)

WebUI.click(findTestObject('Location/btn_upload'))

//String filePath = 'D:\\Upload\\MagiqTowns.json'
String filePath = 'D:\\Upload\\BullerAreas.json'


Robot robot = new Robot()

// Copy file path vào clipboard
java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(filePath), 
    null)

// Paste và Enter
robot.keyPress(KeyEvent.VK_CONTROL)

robot.keyPress(KeyEvent.VK_V)

robot.keyRelease(KeyEvent.VK_V)

robot.keyRelease(KeyEvent.VK_CONTROL)

robot.keyPress(KeyEvent.VK_ENTER)

robot.keyRelease(KeyEvent.VK_ENTER)

WebUI.delay(10)

WebUI.waitForElementPresent(findTestObject('Location/btn_update'), 120)

WebUI.click(findTestObject('Location/btn_update'))

WebUI.waitForElementPresent(findTestObject('Location/h4_message'), 120)

WebUI.waitForElementClickable(findTestObject('Location/btn_OK'), 120)

