
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.util.KeywordUtil



import com.kms.katalon.core.configuration.RunConfiguration



// ===== Cấu hình chung =====
int LOCAL_PAGELOAD = 300                // Page load tối đa 5 phút
int WAIT_ELEMENT_BEFORE_RELOAD = 120    // Đợi element tối đa 2 phút trước khi reload
int MAX_RELOAD_ATTEMPTS = 5             // Số lần reload nếu chưa thấy element

WebUI.openBrowser('')
WebUI.maximizeWindow()

WebUI.navigateToUrl('https://qa.engagement.magiqdev.cloud/login')
WebUI.waitForPageLoad(LOCAL_PAGELOAD)

// ===== Locator: <span class="mdc-button__label">Continue as Guest</span> =====
// Dùng cả CSS và XPath để tăng độ bền
TestObject spanContinueAsGuest = new TestObject('span_continue_as_guest')
// CSS: khớp class
spanContinueAsGuest.addProperty('css', ConditionType.EQUALS, "span.mdc-button__label")
// XPath: khớp class & text (normalize-space)
spanContinueAsGuest.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//*[self::span and contains(concat(' ', normalize-space(@class), ' '), ' mdc-button__label ') and normalize-space(text())='Continue as Guest']"
)

// ===== Đợi span xuất hiện; nếu chưa thấy thì reload theo chiến lược =====
boolean targetVisible = false
int attempts = 0

while (attempts <= MAX_RELOAD_ATTEMPTS) {
	targetVisible = WebUI.waitForElementVisible(spanContinueAsGuest, WAIT_ELEMENT_BEFORE_RELOAD, FailureHandling.OPTIONAL)
	if (targetVisible) {
		break
	}
	WebUI.comment("Không thấy <span class='mdc-button__label'>Continue as Guest</span> sau ${WAIT_ELEMENT_BEFORE_RELOAD}s. Reload lần ${attempts + 1}.")
	WebUI.refresh()
	WebUI.waitForPageLoad(LOCAL_PAGELOAD)
	attempts++
}

// ===== Kết luận =====
if (targetVisible) {
	// Xác nhận tồn tại thêm lần nữa với verifyElementPresent (timeout ngắn)
	WebUI.verifyElementPresent(spanContinueAsGuest, 10, FailureHandling.STOP_ON_FAILURE)
	WebUI.comment("✅ Tồn tại: <span class='mdc-button__label'>Continue as Guest</span> trên trang login.")
} else {
	WebUI.takeScreenshot()
	WebUI.comment("❌ Không thấy <span class='mdc-button__label'>Continue as Guest</span> sau ${attempts} lần reload.")
	WebUI.verifyEqual(targetVisible, true) // ép fail để có log/screenshot
}
