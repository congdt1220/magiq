
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import org.openqa.selenium.WebElement

// ===== Cấu hình thời gian =====
int PAGELOAD_TIMEOUT    = 300   // tối đa 5 phút khi refresh
int MAX_RELOAD_ATTEMPTS = 1     // số lần refresh nhẹ nếu chậm
int WAIT_AFTER_CLICK    = 60    // chờ validation hiển thị sau khi click

// ===== (Tuỳ chọn) Gọi Test Case "CA-01" nếu cần pre-conditions =====

// ===== Locator nút Create Account theo aria-label =====
TestObject btnCreateAccount = new TestObject('obj_btn_create_account')
btnCreateAccount.addProperty('xpath', ConditionType.EQUALS,
	"//button[@aria-label='Create Account']"
)

// ===== Click nút Create Account =====
boolean clickable = WebUI.waitForElementClickable(btnCreateAccount, 30, FailureHandling.OPTIONAL)
if (!clickable) {
	WebUI.comment("⌛ Nút chưa clickable. Đợi thêm 2s và thử lại.")
	WebUI.delay(2)
	clickable = WebUI.waitForElementClickable(btnCreateAccount, 30, FailureHandling.OPTIONAL)
}
if (!clickable) {
	WebUI.takeScreenshot()
	WebUI.comment("❌ Nút 'Create Account' không clickable.")
	WebUI.verifyEqual(clickable, true) // ép fail để có log/screenshot
}
WebUI.click(btnCreateAccount)
WebUI.comment("✅ Đã click button aria-label='Create Account'.")

// ===== Locator chung cho tất cả div.validation-message =====
TestObject allValidationDivs = new TestObject('obj_all_validation_divs')
allValidationDivs.addProperty('xpath', ConditionType.EQUALS,
	"//div[contains(@class,'validation-message')]"
)

// ===== Chờ validation xuất hiện (ít nhất 1 cái) =====
boolean anyValidationVisible = WebUI.waitForElementVisible(allValidationDivs, WAIT_AFTER_CLICK, FailureHandling.OPTIONAL)

// Nếu chưa thấy, thử refresh 1 lần (SPA có thể chậm)
if (!anyValidationVisible) {
	WebUI.comment("⌛ Chưa thấy validation-message. Thử refresh 1 lần và chờ lại.")
	WebUI.refresh()
	WebUI.waitForPageLoad(PAGELOAD_TIMEOUT)
	anyValidationVisible = WebUI.waitForElementVisible(allValidationDivs, WAIT_AFTER_CLICK, FailureHandling.OPTIONAL)
}

// ===== Lấy tất cả validation div và kiểm tra số lượng =====
List<WebElement> validationDivs = WebUI.findWebElements(allValidationDivs, 10)

WebUI.comment("🔎 Số div.validation-message tìm thấy: ${validationDivs.size()}")

if (validationDivs.size() != 3) {
	WebUI.takeScreenshot()
	WebUI.comment("❌ Kỳ vọng 3 validation-message, thực tế: ${validationDivs.size()}.")
	WebUI.verifyEqual(validationDivs.size(), 3) // ép fail để có log/screenshot
}

// ===== Tạo locator theo nội dung kỳ vọng (so khớp text chính xác) =====
TestObject msgEmail = new TestObject('obj_msg_email')
msgEmail.addProperty('xpath', ConditionType.EQUALS,
	"//div[contains(@class,'validation-message') and normalize-space(.)='Please enter a valid Email address.']"
)

TestObject msgNewPw = new TestObject('obj_msg_new_password')
msgNewPw.addProperty('xpath', ConditionType.EQUALS,
	"//div[contains(@class,'validation-message') and normalize-space(.)='Please enter your new password!']"
)

TestObject msgConfirmPw = new TestObject('obj_msg_confirm_password')
msgConfirmPw.addProperty('xpath', ConditionType.EQUALS,
	"//div[contains(@class,'validation-message') and normalize-space(.)='Please enter your new password confirm!']"
)

// ===== Xác nhận từng thông điệp tồn tại =====
boolean emailMsgPresent   = WebUI.waitForElementPresent(msgEmail, 10, FailureHandling.OPTIONAL)
boolean newPwMsgPresent   = WebUI.waitForElementPresent(msgNewPw, 10, FailureHandling.OPTIONAL)
boolean confirmPwMsgPresent = WebUI.waitForElementPresent(msgConfirmPw, 10, FailureHandling.OPTIONAL)

if (!(emailMsgPresent && newPwMsgPresent && confirmPwMsgPresent)) {
	WebUI.takeScreenshot()
	if (!emailMsgPresent)     WebUI.comment("❌ Thiếu message: 'Please enter a valid Email address.'")
	if (!newPwMsgPresent)     WebUI.comment("❌ Thiếu message: 'Please enter your new password!'")
	if (!confirmPwMsgPresent) WebUI.comment("❌ Thiếu message: 'Please enter your new password confirm!'")
	WebUI.verifyEqual(emailMsgPresent && newPwMsgPresent && confirmPwMsgPresent, true) // ép fail để có log/screenshot
}

// (Tuỳ bạn) WebUI.closeBrowser()
