
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.util.KeywordUtil

// ===== Cấu hình thời gian =====
int PAGELOAD_TIMEOUT = 300            // tối đa 5 phút khi refresh
int WAIT_EACH_FIELD = 60              // chờ mỗi field sau khi gọi TC
int MAX_RELOAD_ATTEMPTS = 1           // số lần refresh nhẹ nếu chậm (tùy UI)

// ===== Tạo locators cho các input theo aria-label =====
// Sử dụng XPath theo aria-label, hỗ trợ normalize-space để tránh khoảng trắng thừa

TestObject inputEmail = new TestObject('obj_input_email')
inputEmail.addProperty('xpath', ConditionType.EQUALS,
	"//input[@aria-label and normalize-space(@aria-label)='Email']"
)

TestObject inputPhone = new TestObject('obj_input_phone')
inputPhone.addProperty('xpath', ConditionType.EQUALS,
	"//input[@aria-label and normalize-space(@aria-label)='Phone Number']"
)

TestObject inputNewPassword = new TestObject('obj_input_new_password')
inputNewPassword.addProperty('xpath', ConditionType.EQUALS,
	"//input[@aria-label and normalize-space(@aria-label)='New Password']"
)

TestObject inputReEnterNewPassword = new TestObject('obj_input_reenter_new_password')
inputReEnterNewPassword.addProperty('xpath', ConditionType.EQUALS,
	"//input[@aria-label and normalize-space(@aria-label)='Re-Enter New Password']"
)

// ===== Chờ các input xuất hiện =====
boolean emailVisible = WebUI.waitForElementVisible(inputEmail, WAIT_EACH_FIELD, FailureHandling.OPTIONAL)
boolean phoneVisible = WebUI.waitForElementVisible(inputPhone, WAIT_EACH_FIELD, FailureHandling.OPTIONAL)
boolean newPwVisible = WebUI.waitForElementVisible(inputNewPassword, WAIT_EACH_FIELD, FailureHandling.OPTIONAL)
boolean reNewPwVisible = WebUI.waitForElementVisible(inputReEnterNewPassword, WAIT_EACH_FIELD, FailureHandling.OPTIONAL)

// Nếu một hoặc nhiều field chưa thấy, thử refresh nhẹ rồi chờ lại (SPA có thể chậm)
if (!(emailVisible && phoneVisible && newPwVisible && reNewPwVisible)) {
	for (int i = 0; i < MAX_RELOAD_ATTEMPTS; i++) {
		WebUI.comment("Một số input chưa xuất hiện. Thử refresh lần ${i + 1}.")
		WebUI.refresh()
		WebUI.waitForPageLoad(PAGELOAD_TIMEOUT)

		emailVisible   = emailVisible   ?: WebUI.waitForElementVisible(inputEmail, WAIT_EACH_FIELD, FailureHandling.OPTIONAL)
		phoneVisible   = phoneVisible   ?: WebUI.waitForElementVisible(inputPhone, WAIT_EACH_FIELD, FailureHandling.OPTIONAL)
		newPwVisible   = newPwVisible   ?: WebUI.waitForElementVisible(inputNewPassword, WAIT_EACH_FIELD, FailureHandling.OPTIONAL)
		reNewPwVisible = reNewPwVisible ?: WebUI.waitForElementVisible(inputReEnterNewPassword, WAIT_EACH_FIELD, FailureHandling.OPTIONAL)

		if (emailVisible && phoneVisible && newPwVisible && reNewPwVisible) break
	}
}

// ===== Kết luận & verify =====
if (emailVisible && phoneVisible && newPwVisible && reNewPwVisible) {
	WebUI.verifyElementPresent(inputEmail, 5, FailureHandling.STOP_ON_FAILURE)
	WebUI.verifyElementPresent(inputPhone, 5, FailureHandling.STOP_ON_FAILURE)
	WebUI.verifyElementPresent(inputNewPassword, 5, FailureHandling.STOP_ON_FAILURE)
	WebUI.verifyElementPresent(inputReEnterNewPassword, 5, FailureHandling.STOP_ON_FAILURE)
	WebUI.comment("✅ Thành công: tất cả input (Email, Phone Number, New Password, Re-Enter New Password) đã load.")
} else {
	WebUI.takeScreenshot()
	if (!emailVisible)   WebUI.comment("❌ Chưa thấy input aria-label='Email'")
	if (!phoneVisible)   WebUI.comment("❌ Chưa thấy input aria-label='Phone Number'")
	if (!newPwVisible)   WebUI.comment("❌ Chưa thấy input aria-label='New Password'")
	if (!reNewPwVisible) WebUI.comment("❌ Chưa thấy input aria-label='Re-Enter New Password'")
	// Ép fail để có log/screenshot đầy đủ
	WebUI.verifyEqual(emailVisible && phoneVisible && newPwVisible && reNewPwVisible, true)
}
