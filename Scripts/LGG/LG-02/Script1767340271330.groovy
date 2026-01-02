
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
int MAX_RELOAD_ATTEMPTS = 2             // Số lần reload nếu chưa thấy element



// ===== Locator: Button "Continue as Guest" theo aria-label =====
TestObject btnContinueAsGuest = new TestObject('btn_continue_as_guest')
btnContinueAsGuest.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//button[@aria-label='Continue as Guest']"
)
// Nếu UI có thể đổi tag/class, có thể thêm fallback:
// btnContinueAsGuest.addProperty('xpath', ConditionType.EQUALS,
//     "//*[@role='button' and @aria-label='Continue as Guest']"
// )

// (Tuỳ chọn) Marker để xác nhận đã vào chế độ Guest
TestObject guestMarker = new TestObject('guestMarker')
guestMarker.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//*[contains(@class,'list-organisation')]"
)

// ===== Chờ button xuất hiện; nếu chưa thấy thì reload theo chiến lược giống mẫu =====
boolean btnVisible = false
int attempts = 0
int MAX_WAIT_SECONDS   = 60
int POLL_INTERVAL      = 10

int waited   = 0

btnVisible = WebUI.waitForElementVisible(btnContinueAsGuest, POLL_INTERVAL, FailureHandling.OPTIONAL)

if (!btnVisible) {
	
	// Đợi modal mở
// 1) Đợi modal hiển thị
//	TestObject modal = new TestObject('blazored_modal')
//	modal.addProperty('xpath', ConditionType.EQUALS,
//    "//div[@id='_62af2bc98c414efa8caaee9602390f6c' and @role='dialog' and @aria-modal='true']")
//	WebUI.waitForElementVisible(modal, 5)

	// 2) Nút Ok trong modal
	TestObject okBtn = new TestObject('ok_button_in_modal')
	okBtn.addProperty('xpath', ConditionType.EQUALS,
	"//footer[contains(@class,'mdc-dialog__actions')]" +
	"//button[@aria-label='Ok' and .//span[@class='mdc-button__label' and normalize-space()='Ok']]")

	WebUI.waitForElementClickable(okBtn, 5)
	WebUI.enhancedClick(okBtn)     // dùng enhancedClick để auto scroll/visibl
	btnVisible = WebUI.waitForElementVisible(btnContinueAsGuest, POLL_INTERVAL, FailureHandling.OPTIONAL)
}

if (!btnVisible) {
	WebUI.takeScreenshot()
	WebUI.comment("Không thể hiển thị button 'Continue as Guest' sau ${attempts} lần reload.")
	WebUI.verifyEqual(btnVisible, true) // ép fail để có log/screenshot
}

// ===== Click button khi đã thấy =====
boolean clickable = WebUI.waitForElementClickable(btnContinueAsGuest, 20, FailureHandling.OPTIONAL)
if (!clickable) {
	WebUI.comment("Button 'Continue as Guest' chưa clickable, thử delay nhẹ.")
	WebUI.delay(2)
	clickable = WebUI.waitForElementClickable(btnContinueAsGuest, 20, FailureHandling.OPTIONAL)
}
if (!clickable) {
	WebUI.takeScreenshot()
	WebUI.comment("Button 'Continue as Guest' không clickable.")
	WebUI.verifyEqual(clickable, true)
}

WebUI.click(btnContinueAsGuest)
WebUI.comment("🖱️ Đã click button aria-label='Continue as Guest'.")

// ===== (Tuỳ chọn) Verify đã vào chế độ guest =====
boolean guestLoaded = WebUI.waitForElementVisible(guestMarker, 60, FailureHandling.OPTIONAL)




if (guestLoaded) {
	WebUI.comment("✅ Thành công: Đã thấy marker giao diện sau khi vào chế độ Guest.")
} else {
	WebUI.takeScreenshot()
	WebUI.comment("❌ Không thấy marker sau khi click 'Continue as Guest'.")
	WebUI.verifyEqual(guestLoaded, true) // ép fail để có log/screenshot
}

