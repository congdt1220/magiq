
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
	"//h3[@class='blazored-modal-title' and normalize-space()='Select Organisation']"
)


// ===== (Tuỳ chọn) Verify đã vào chế độ guest =====
boolean guestLoaded = WebUI.waitForElementVisible(guestMarker, 60, FailureHandling.OPTIONAL)

if (guestLoaded) {
	WebUI.comment("✅ Thành công: Đã thấy marker giao diện sau khi vào chế độ Guest.")
} else {
	WebUI.takeScreenshot()
	WebUI.comment("❌ Không thấy marker sau khi click 'Continue as Guest'.")
	WebUI.verifyEqual(guestLoaded, true) // ép fail để có log/screenshot
}

