
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.util.KeywordUtil

// ===== Cấu hình thời gian =====
int PAGELOAD_TIMEOUT = 300            // tối đa 5 phút khi refresh
int WAIT_FIELD = 60                   // chờ mỗi field hiển thị
int MAX_RELOAD_ATTEMPTS = 1           // số lần refresh nhẹ nếu chậm (tuỳ UI)
int WAIT_AFTER_CLICK = 30             // chờ h3 'Select Organisation' sau khi submit


// ===== Locators cho các input theo aria-label =====
TestObject inputEmail = new TestObject('obj_input_email')
inputEmail.addProperty('xpath', ConditionType.EQUALS,
	"//*[@aria-label and normalize-space(@aria-label)='Email']"
)

TestObject inputPhone = new TestObject('obj_input_phone')
inputPhone.addProperty('xpath', ConditionType.EQUALS,
	"//*[@aria-label and normalize-space(@aria-label)='Phone Number']"
)

TestObject inputNewPassword = new TestObject('obj_input_new_password')
inputNewPassword.addProperty('xpath', ConditionType.EQUALS,
	"//*[@aria-label and normalize-space(@aria-label)='New Password']"
)

TestObject inputReEnterNewPassword = new TestObject('obj_input_reenter_new_password')
inputReEnterNewPassword.addProperty('xpath', ConditionType.EQUALS,
	"//*[@aria-label and normalize-space(@aria-label)='Re-Enter New Password']"
)

// Nút Create Account theo aria-label
TestObject btnCreateAccount = new TestObject('obj_btn_create_account')
btnCreateAccount.addProperty('xpath', ConditionType.EQUALS,
	"//button[@aria-label='Create Account']"
)

// ===== Locator cho h3 'Select Organisation' =====
TestObject h3SelectOrganisation = new TestObject('obj_h3_select_organisation')
h3SelectOrganisation.addProperty('xpath', ConditionType.EQUALS,
	"//h3[normalize-space(.)='Select Organisation']"
)

// ===== Chờ các input xuất hiện =====
boolean emailVisible   = WebUI.waitForElementVisible(inputEmail, WAIT_FIELD, FailureHandling.OPTIONAL)
boolean phoneVisible   = WebUI.waitForElementVisible(inputPhone, WAIT_FIELD, FailureHandling.OPTIONAL)
boolean newPwVisible   = WebUI.waitForElementVisible(inputNewPassword, WAIT_FIELD, FailureHandling.OPTIONAL)
boolean reNewPwVisible = WebUI.waitForElementVisible(inputReEnterNewPassword, WAIT_FIELD, FailureHandling.OPTIONAL)

if (!(emailVisible && phoneVisible && newPwVisible && reNewPwVisible)) {
	for (int i = 0; i < MAX_RELOAD_ATTEMPTS; i++) {
		WebUI.comment("Một hoặc nhiều trường chưa load. Refresh lần ${i + 1}.")
		WebUI.refresh()
		WebUI.waitForPageLoad(PAGELOAD_TIMEOUT)

		if (!emailVisible)   emailVisible   = WebUI.waitForElementVisible(inputEmail, WAIT_FIELD, FailureHandling.OPTIONAL)
		if (!phoneVisible)   phoneVisible   = WebUI.waitForElementVisible(inputPhone, WAIT_FIELD, FailureHandling.OPTIONAL)
		if (!newPwVisible)   newPwVisible   = WebUI.waitForElementVisible(inputNewPassword, WAIT_FIELD, FailureHandling.OPTIONAL)
		if (!reNewPwVisible) reNewPwVisible = WebUI.waitForElementVisible(inputReEnterNewPassword, WAIT_FIELD, FailureHandling.OPTIONAL)

		if (emailVisible && phoneVisible && newPwVisible && reNewPwVisible) break
	}
}

if (!(emailVisible && phoneVisible && newPwVisible && reNewPwVisible)) {
	WebUI.takeScreenshot()
	if (!emailVisible)   WebUI.comment("❌ Chưa thấy input aria-label='Email'")
	if (!phoneVisible)   WebUI.comment("❌ Chưa thấy input aria-label='Phone Number'")
	if (!newPwVisible)   WebUI.comment("❌ Chưa thấy input aria-label='New Password'")
	if (!reNewPwVisible) WebUI.comment("❌ Chưa thấy input aria-label='Re-Enter New Password'")
	WebUI.verifyEqual(false, true) // ép fail để có log/screenshot
}

// ===== Phát sinh email ngẫu nhiên mỗi lần =====
String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
String randomEmail = "test" + timestamp + "@gmail.com"
KeywordUtil.logInfo("📧 Email sẽ nhập: " + randomEmail)

// ===== Fill dữ liệu =====
WebUI.setText(inputEmail, randomEmail)
WebUI.setText(inputPhone, "123456789")
WebUI.setText(inputNewPassword, "Test@12345678")
WebUI.setText(inputReEnterNewPassword, "Test@12345678")

// ===== Click nút Create Account =====
boolean clickable = WebUI.waitForElementClickable(btnCreateAccount, 30, FailureHandling.OPTIONAL)
if (!clickable) {
	WebUI.delay(2)
	clickable = WebUI.waitForElementClickable(btnCreateAccount, 30, FailureHandling.OPTIONAL)
}
if (!clickable) {
	WebUI.takeScreenshot()
	WebUI.comment("❌ Nút 'Create Account' không clickable.")
	WebUI.verifyEqual(clickable, true)
}
WebUI.click(btnCreateAccount)
WebUI.click(btnCreateAccount)
WebUI.comment("✅ Đã click button aria-label='Create Account'.")

// ===== Sau click: chờ load và kiểm tra h3 'Select Organisation' =====
boolean h3Visible = WebUI.waitForElementVisible(h3SelectOrganisation, WAIT_AFTER_CLICK, FailureHandling.OPTIONAL)

if (!h3Visible) {
	// SPA có thể chậm → thử refresh 1 lần
	WebUI.comment("⌛ Chưa thấy h3 'Select Organisation'. Thử refresh 1 lần và chờ lại.")
	WebUI.click(btnCreateAccount)
	h3Visible = WebUI.waitForElementVisible(h3SelectOrganisation, WAIT_AFTER_CLICK, FailureHandling.OPTIONAL)
}

if (h3Visible) {
	WebUI.verifyElementPresent(h3SelectOrganisation, 10, FailureHandling.STOP_ON_FAILURE)
	WebUI.comment("✅ Thành công: tồn tại h3 với text 'Select Organisation'.")
} else {
	WebUI.takeScreenshot()
	WebUI.comment("❌ Không thấy h3 'Select Organisation' sau khi tạo tài khoản.")
	WebUI.verifyEqual(h3Visible, true) // ép fail để có log/screenshot
}

// (Tuỳ bạn) WebUI.closeBrowser()
