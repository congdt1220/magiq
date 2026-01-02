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
import com.kms.katalon.core.testobject.ConditionType



// ==== Cấu hình thời gian ====
int LOCAL_PAGELOAD = 300              // nếu có refresh lại trang
int WAIT_AFTER_CLICK = 60             // chờ các element sau khi click Forgot Password
int RETRY_ON_SLOW = 1                 // số lần thử refresh nhẹ nếu chậm (tuỳ trang)


// ==== Locator kết quả cần kiểm tra ====
// p có text "Cancel"
TestObject pCancel = new TestObject('obj_p_cancel')
pCancel.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//p[normalize-space(.)='Cancel']"
)

// span có text "Reset Password"
TestObject spanResetPassword = new TestObject('obj_span_reset_password')
spanResetPassword.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//span[normalize-space(.)='Reset Password']"
)


// ==== Chờ các element xuất hiện sau khi đã click Forgot Password ====
// Chờ p "Cancel"
boolean cancelVisible = WebUI.waitForElementVisible(pCancel, WAIT_AFTER_CLICK, FailureHandling.OPTIONAL)

// Chờ span "Reset Password"
boolean resetVisible = WebUI.waitForElementVisible(spanResetPassword, WAIT_AFTER_CLICK, FailureHandling.OPTIONAL)

// Nếu cả hai chưa thấy, thử refresh nhẹ một lần (tuỳ ứng dụng SPA chậm)
if (!cancelVisible || !resetVisible) {
	for (int i = 0; i < RETRY_ON_SLOW && (!cancelVisible || !resetVisible); i++) {
		WebUI.refresh()
		WebUI.waitForPageLoad(LOCAL_PAGELOAD)
		cancelVisible = WebUI.waitForElementVisible(pCancel, WAIT_AFTER_CLICK, FailureHandling.OPTIONAL)
		resetVisible = WebUI.waitForElementVisible(spanResetPassword, WAIT_AFTER_CLICK, FailureHandling.OPTIONAL)
	}
}

// ==== Kết luận/verify ====
// Cần cả 2: p "Cancel" và span "Reset Password"
if (cancelVisible && resetVisible) {
	WebUI.verifyElementPresent(pCancel, 5, FailureHandling.STOP_ON_FAILURE)
	WebUI.verifyElementPresent(spanResetPassword, 5, FailureHandling.STOP_ON_FAILURE)
	WebUI.comment("✅ Thành công: thấy p='Cancel' và span='Reset Password' sau khi click Forgot Password.")
} else {
	WebUI.takeScreenshot()
	if (!cancelVisible) WebUI.comment("❌ Không thấy thẻ p với text 'Cancel'.")
	if (!resetVisible) WebUI.comment("❌ Không thấy thẻ span với text 'Reset Password'.")
	// Ép fail để có log/screenshot đầy đủ
	WebUI.verifyEqual(cancelVisible && resetVisible, true)
}
