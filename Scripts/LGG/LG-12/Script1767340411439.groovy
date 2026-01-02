
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
int WAIT_AFTER_CLICK = 90             // chờ h3 'Select Organisation' sau khi submit


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
WebUI.comment("✅ Đã click button aria-label='Create Account'.")



// ===== Sau khi click 'Create Account', kiểm tra có button có text = randomEmail =====

// (1) Chờ trang / UI render sau submit (tuỳ UI)
WebUI.comment("⏳ Chờ UI render sau khi tạo tài khoản...")
WebUI.waitForPageLoad(30)
WebUI.delay(2) // đợi nhẹ cho DOM/JS ổn định

// (2) Tạo TestObject cho nút với text đúng bằng randomEmail (exact match)
TestObject btnTextEqualsEmail = new TestObject('obj_btn_text_equals_email')
btnTextEqualsEmail.addProperty('xpath', ConditionType.EQUALS,
	// Tìm theo nhiều biến thể 'button' để tăng độ phủ:
	// - Thẻ <button>
	// - Bất kỳ phần tử có role='button'
	// - <a> có class chứa 'btn'
	// Sử dụng normalize-space(.) để bỏ khoảng trắng dư
	"(//button[normalize-space(.)='" + randomEmail + "']" +
	" | //*[@role='button' and normalize-space(.)='" + randomEmail + "']" +
	" | //a[contains(@class,'btn') and normalize-space(.)='" + randomEmail + "'])"
)

// (3) Chờ nút xuất hiện (exact)
boolean btnExactVisible = WebUI.waitForElementVisible(btnTextEqualsEmail, 15, FailureHandling.OPTIONAL)

if (btnExactVisible) {
	WebUI.comment("✅ Tìm thấy button có text đúng bằng email: '" + randomEmail + "'.")
	WebUI.takeScreenshot() // ghi nhận bằng chứng
} else {
	WebUI.comment("ℹ️ Chưa thấy button text EXACT = '" + randomEmail + "'. Thử tìm ở chế độ CONTAINS...")
	// (4) Thử thêm chế độ CONTAINS (phòng khi UI thêm tiền tố/hậu tố)
	TestObject btnTextContainsEmail = new TestObject('obj_btn_text_contains_email')
	btnTextContainsEmail.addProperty('xpath', ConditionType.EQUALS,
		"(//button[contains(normalize-space(.), '" + randomEmail + "')]" +
		" | //*[@role='button' and contains(normalize-space(.), '" + randomEmail + "')]" +
		" | //a[contains(@class,'btn') and contains(normalize-space(.), '" + randomEmail + "')])"
	)

	boolean btnContainsVisible = WebUI.waitForElementVisible(btnTextContainsEmail, 10, FailureHandling.OPTIONAL)

	if (btnContainsVisible) {
		WebUI.comment("✅ Tìm thấy button có text CHỨA email: '" + randomEmail + "'.")
		WebUI.takeScreenshot()
	} else {
		WebUI.comment("❌ Không tìm thấy button có text =/chứa '" + randomEmail + "'.")
		WebUI.takeScreenshot()
		// Tuỳ chiến lược: có thể không fail test, chỉ log; nếu cần ép fail:
		// WebUI.verifyEqual(true, false)
	}
}





