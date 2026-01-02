import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory as CheckpointFactory
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testcase.TestCaseFactory as TestCaseFactory
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testdata.TestDataFactory as TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository as ObjectRepository
import com.kms.katalon.core.testobject.TestObject as TestObject

import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile

import internal.GlobalVariable as GlobalVariable

import com.kms.katalon.core.annotation.SetUp
import com.kms.katalon.core.annotation.SetupTestCase
import com.kms.katalon.core.annotation.TearDown
import com.kms.katalon.core.annotation.TearDownTestCase
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.webui.driver.DriverFactory
import org.openqa.selenium.Cookie
/**
 * Some methods below are samples for using SetUp/TearDown in a test suite.
 */

/**
 * Setup test suite environment.
 */
@SetUp(skipped = false) // Please change skipped to be false to activate this method.
def setUp() {
	// Put your code here.
	
	
	List<String> args = [
		"--user-data-dir=C:\\QA\\Profiles\\KatalonChrome",
		"--profile-directory=Default"
	]
	RunConfiguration.setWebDriverPreferencesProperty("args", args)
}

/**
 * Clean test suites environment.
 */
@TearDown(skipped = true) // Please change skipped to be false to activate this method.
def tearDown() {
	// Put your code here.
	
	String id =
	shouldRun = (id == 'Test Cases/LG-02')
	KeywordUtil.logInfo("Test Suite setUp → ID: " + id + ", shouldRun=" + shouldRun)

	if (!shouldRun) {
		KeywordUtil.markWarning("Stop sớm suite vì ID != 'x'")
		// Không có API “skip suite”, cách tốt nhất là return và không chạy logic nào khác ở phần thân
		return
	}
	
	WebUI.navigateToUrl('https://qa.engagement.magiqdev.cloud/login')  // đổi URL
	
	// Kiểm tra đăng nhập theo cookie hoặc theo element UI
	boolean loggedByCookie = hasLoginCookie('SESSIONID')   // đổi tên phù hợp
	boolean loggedByUI = WebUI.verifyElementPresent(
		findTestObject('Object Repository/Header/btnLogout'), 3,
		com.kms.katalon.core.model.FailureHandling.OPTIONAL
	)
	
	if (loggedByCookie || loggedByUI) {
		KeywordUtil.logInfo("User appears logged in. Proceed to delete login session.")
		// Xóa cookie đăng nhập
		deleteLoginCookie('SESSIONID')   // hoặc deleteAllCookiesForCurrentDomain()
		// Xóa storage nếu app dùng token ở storage
//		clearWebStorage()
//				WebUI.refresh()
	} else {
		KeywordUtil.logInfo("Not logged in. No cookies to delete.")
	}

}

/**
 * Run before each test case starts.
 */
@SetupTestCase(skipped = false) // Please change skipped to be false to activate this method.
def setupTestCase() {
	// Put your code here.
}

/**
 * Run after each test case ends.
 */
@TearDownTestCase(skipped = true) // Please change skipped to be false to activate this method.
def tearDownTestCase() {
	// Put your code here.
}

static boolean hasLoginCookie(String cookieName) {
	def driver = DriverFactory.getWebDriver()
	if (driver == null) return false
	Cookie c = driver.manage().getCookieNamed(cookieName)
	return c != null && c.getValue() != null && c.getValue().trim().length() > 0
}
/**
 * References:
 * Groovy tutorial page: http://docs.groovy-lang.org/next/html/documentation/
 */