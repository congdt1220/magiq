
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType

// ===== Cấu hình chung =====
int LOCAL_PAGELOAD = 300                // Page load tối đa 5 phút
int WAIT_ELEMENT_BEFORE_RELOAD = 120    // Đợi element tối đa 2 phút trước khi reload
int MAX_RELOAD_ATTEMPTS = 2             // Số lần reload nếu chưa thấy element

WebUI.openBrowser('')
WebUI.maximizeWindow()

WebUI.navigateToUrl('https://qa.engagement.magiqdev.cloud/login')
WebUI.waitForPageLoad(LOCAL_PAGELOAD)

// ===== Locator ổn định =====

// Link/btn 'Forgot Password?' — ưu tiên text chính xác, có fallback cho span/text bên trong
TestObject forgotPasswordLink = new TestObject('forgotPasswordLink')
forgotPasswordLink.addProperty('xpath', ConditionType.EQUALS,
	"//a[normalize-space(.)='Forgot Password?'] | //button[normalize-space(.)='Forgot Password?'] | //a[.//span[normalize-space(.)='Forgot Password?']]"
)

// Tiêu đề H2 'Forgot Password' — normalize-space để loại khoảng trắng thừa
TestObject h2ForgotPassword = new TestObject('h2ForgotPassword')
h2ForgotPassword.addProperty('xpath', ConditionType.EQUALS,
	"//h2[normalize-space(.)='Forgot Password']"
)

// ===== Chờ link 'Forgot Password?' xuất hiện; nếu chưa thấy thì reload theo chiến lược giống mẫu =====
boolean forgotVisible = false
int attempts = 0

while (attempts <= MAX_RELOAD_ATTEMPTS) {
	forgotVisible = WebUI.waitForElementVisible(forgotPasswordLink, WAIT_ELEMENT_BEFORE_RELOAD, FailureHandling.OPTIONAL)
	if (forgotVisible) {
		break
	}
	WebUI.comment("Không thấy 'Forgot Password?' sau ${WAIT_ELEMENT_BEFORE_RELOAD}s. Reload lần ${attempts + 1}.")
	WebUI.refresh()
	WebUI.waitForPageLoad(LOCAL_PAGELOAD)
	attempts++
}

if (!forgotVisible) {
	WebUI.takeScreenshot()
	WebUI.comment("Không thể hiển thị link 'Forgot Password?' sau ${attempts} lần reload.")
	WebUI.verifyEqual(forgotVisible, true) // ép fail để có log/screenshot
}

// ===== Click 'Forgot Password?' khi đã thấy =====
boolean clickable = WebUI.waitForElementClickable(forgotPasswordLink, 20, FailureHandling.OPTIONAL)
if (!clickable) {
	WebUI.comment("Link 'Forgot Password?' chưa clickable, thử delay nhẹ.")
	WebUI.delay(2)
	clickable = WebUI.waitForElementClickable(forgotPasswordLink, 20, FailureHandling.OPTIONAL)
}
if (!clickable) {
	WebUI.takeScreenshot()
	WebUI.comment("Link 'Forgot Password?' không clickable.")
	WebUI.verifyEqual(clickable, true)
}

WebUI.click(forgotPasswordLink)

// ===== Sau khi click: verify H2 'Forgot Password' xuất hiện =====
boolean h2Visible = WebUI.waitForElementVisible(h2ForgotPassword, 60, FailureHandling.OPTIONAL)

if (!h2Visible) {
	// Trong trường hợp điều hướng chậm, thử refresh 1 lần rồi đợi lại
	WebUI.comment("Chưa thấy H2 'Forgot Password'. Thử refresh 1 lần và chờ lại.")
	WebUI.refresh()
	WebUI.waitForPageLoad(LOCAL_PAGELOAD)
	h2Visible = WebUI.waitForElementVisible(h2ForgotPassword, 60, FailureHandling.OPTIONAL)
}

if (h2Visible) {
	WebUI.verifyElementPresent(h2ForgotPassword, 10, FailureHandling.STOP_ON_FAILURE)
	WebUI.comment("✅ Thành công: H2 'Forgot Password' đã xuất hiện.")
} else {
	WebUI.takeScreenshot()
	WebUI.comment("❌ Không thấy H2 'Forgot Password' sau khi click và thử refresh.")
	WebUI.verifyEqual(h2Visible, true) // ép fail để có log/screenshot
}

// (Tuỳ bạn) Đóng trình duyệt sau khi xong
// WebUI.closeBrowser()
