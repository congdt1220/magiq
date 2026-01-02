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






import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.util.KeywordUtil

// ===== Cấu hình thời gian =====
int PAGELOAD_TIMEOUT = 300          // tối đa 5 phút nếu có reload
int WAIT_BEFORE_CLICK = 60          // chờ /login hiển thị
int WAIT_AFTER_CLICK = 60           // chờ #userName sau khi click
int RETRY_ON_SLOW = 1               // thử refresh nhẹ 1 lần nếu chậm




// ===== Locator =====
// a có href="/login" (có thể kèm text "Back to Login" nếu cần chặt chẽ hơn)
TestObject aBackToLogin = new TestObject('obj_a_back_to_login')
aBackToLogin.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//a[@href='/login']"
	// Nếu muốn chắc là đúng link với text:
	// "//a[@href='/login' and normalize-space(.)='Back to Login']"
)

// input có id="userName"
TestObject userNameInput = new TestObject('obj_input_userName')
userNameInput.addProperty('id', ConditionType.EQUALS, 'userName')

// ===== Chờ và click anchor Back to Login =====
try {
	boolean aVisible = WebUI.waitForElementVisible(aBackToLogin, WAIT_BEFORE_CLICK, FailureHandling.OPTIONAL)
	if (!aVisible) {
		// thử refresh nhẹ 1 lần nếu chưa thấy
		for (int i = 0; i < RETRY_ON_SLOW && !aVisible; i++) {
			WebUI.refresh()
			WebUI.waitForPageLoad(PAGELOAD_TIMEOUT)
			aVisible = WebUI.waitForElementVisible(aBackToLogin, WAIT_BEFORE_CLICK, FailureHandling.OPTIONAL)
		}
	}

	if (!aVisible) {
		WebUI.takeScreenshot()
		KeywordUtil.markFailed("❌ Không thấy thẻ <a href=\"/login\"> sau khi gọi TC 'XYZ'.")
	}

	// đảm bảo clickable rồi click
	boolean clickable = WebUI.waitForElementClickable(aBackToLogin, 20, FailureHandling.OPTIONAL)
	if (!clickable) {
		WebUI.delay(1)
		clickable = WebUI.waitForElementClickable(aBackToLogin, 20, FailureHandling.OPTIONAL)
	}
	if (!clickable) {
		WebUI.takeScreenshot()
		KeywordUtil.markFailed("❌ Thẻ <a href=\"/login\"> không clickable.")
	}

	WebUI.click(aBackToLogin)
	WebUI.comment("🖱️ Đã click anchor href='/login' (Back to Login).")

	// ===== Chờ xem có load #userName (đã quay về trang login) =====
	boolean userNameVisible = WebUI.waitForElementVisible(userNameInput, WAIT_AFTER_CLICK, FailureHandling.OPTIONAL)
	if (!userNameVisible) {
		// Trường hợp điều hướng chậm: thử refresh 1 lần
		WebUI.comment("⌛ Chưa thấy #userName. Thử refresh một lần và chờ lại.")
		WebUI.refresh()
		WebUI.waitForPageLoad(PAGELOAD_TIMEOUT)
		userNameVisible = WebUI.waitForElementVisible(userNameInput, WAIT_AFTER_CLICK, FailureHandling.OPTIONAL)
	}

	if (userNameVisible) {
		WebUI.verifyElementPresent(userNameInput, 10, FailureHandling.STOP_ON_FAILURE)
		WebUI.comment("✅ Thành công: thấy input id='userName' (đã quay về trang login).")
	} else {
		WebUI.takeScreenshot()
		KeywordUtil.markFailed("❌ Không thấy input id='userName' sau khi click anchor /login.")
	}

} catch (Exception e) {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ Lỗi khi chạy flow Back to Login (anchor /login): ${e.message}")
	throw e
}
