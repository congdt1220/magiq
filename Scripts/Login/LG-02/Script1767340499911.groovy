
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType

// ---- Cấu hình ----
int LOCAL_PAGELOAD = 300                // Page load tối đa 5 phút
int WAIT_ELEMENT_BEFORE_RELOAD = 120    // Đợi element 2 phút trước khi reload
int MAX_RELOAD_ATTEMPTS = 2             // Số lần reload nếu chưa thấy element
int AFTER_LOGIN_WAIT = 120              // Tổng thời gian chờ sau login để thấy body-alert-icon
int POLL_STEP = 3                       // Polling mỗi 3 giây



// ----- Locator ổn định -----
TestObject userNameField = new TestObject('userName')
userNameField.addProperty('id', ConditionType.EQUALS, 'userName')

TestObject passwordField = new TestObject('password')
passwordField.addProperty('xpath', ConditionType.EQUALS,
	"//input[@type='password' or contains(@aria-label,'password') or contains(@placeholder,'Password')]")

// Nút Login: theo class 'btn-login' (ưu tiên CSS), có fallback theo text 'Login'
TestObject loginBtn = new TestObject('loginBtn')
loginBtn.addProperty('css', ConditionType.EQUALS, "button.btn-login")
loginBtn.addProperty('xpath', ConditionType.EQUALS,
	"//button[contains(@class,'btn-login') or normalize-space(.)='Login' or .//span[normalize-space(.)='Login']]")

// Marker TC thành công: class 'body-alert-icon'
TestObject successMarker = new TestObject('bodyAlertIcon')
successMarker.addProperty('css', ConditionType.EQUALS, ".body-alert-icon")
successMarker.addProperty('xpath', ConditionType.EQUALS, "//*[contains(@class,'body-alert-icon')]")

// ----- Đợi #userName tối đa 2 phút; nếu không thấy thì reload và thử lại -----
boolean usernameVisible = false
usernameVisible = WebUI.waitForElementVisible(userNameField, WAIT_ELEMENT_BEFORE_RELOAD, FailureHandling.OPTIONAL)
if (!usernameVisible) {
	WebUI.takeScreenshot()
	WebUI.comment("Không thể hiển thị trường #userName sau ${attempts} lần reload.")
	WebUI.verifyEqual(usernameVisible, true) // ép fail để có log/screenshot
}

// ----- Khi đã thấy #userName: thực hiện login -----
WebUI.setText(userNameField, 'admin000@gmail.com')

// Chờ ô password xuất hiện (không lặp login; chỉ reload nhẹ 1 lần nếu cần)
boolean passwordVisible = WebUI.waitForElementVisible(passwordField, WAIT_ELEMENT_BEFORE_RELOAD, FailureHandling.OPTIONAL)
if (!passwordVisible) {
	WebUI.comment("Không thấy ô password sau ${WAIT_ELEMENT_BEFORE_RELOAD}s. Reload nhẹ 1 lần.")
	WebUI.refresh()
	WebUI.waitForPageLoad(LOCAL_PAGELOAD)
	passwordVisible = WebUI.waitForElementVisible(passwordField, WAIT_ELEMENT_BEFORE_RELOAD, FailureHandling.OPTIONAL)
}
if (!passwordVisible) {
	WebUI.takeScreenshot()
	WebUI.comment("Không thể hiển thị trường password sau khi reload.")
	WebUI.verifyEqual(passwordVisible, true)
}

// ⚠️ Giữ nguyên encrypted password theo hệ thống của bạn.
// Nếu mật khẩu khác, thay giá trị dưới đây cho đúng.
WebUI.setEncryptedText(passwordField, 'vckfKYHQ/X5a0LrHpsIEKA==')

// ----- Nút Login -----
boolean loginClickable = WebUI.waitForElementClickable(loginBtn, WAIT_ELEMENT_BEFORE_RELOAD, FailureHandling.OPTIONAL)
if (!loginClickable) {
	WebUI.comment("Nút Login chưa clickable sau ${WAIT_ELEMENT_BEFORE_RELOAD}s. Reload nhẹ 1 lần.")
	WebUI.refresh()
	WebUI.waitForPageLoad(LOCAL_PAGELOAD)
	loginClickable = WebUI.waitForElementClickable(loginBtn, WAIT_ELEMENT_BEFORE_RELOAD, FailureHandling.OPTIONAL)
}
if (!loginClickable) {
	WebUI.takeScreenshot()
	WebUI.comment("Nút Login không clickable.")
	WebUI.verifyEqual(loginClickable, true)
}

WebUI.click(loginBtn)

// ----- Sau khi click Login: TC thành công nếu thấy 'body-alert-icon' -----
boolean successVisible = false
int waited = 0
while (waited < AFTER_LOGIN_WAIT) {
	if (WebUI.verifyElementVisible(successMarker, FailureHandling.OPTIONAL)) {
		successVisible = true
		break
	}
	WebUI.delay(POLL_STEP)
	waited += POLL_STEP
}

// Kết luận
if (successVisible) {
	WebUI.comment("TC thành công: xuất hiện class 'body-alert-icon'.")
	
	TestObject okBtn = new TestObject('dynamic_ok_btn')
	okBtn.addProperty('xpath', ConditionType.EQUALS,
			"//button[.//span[@class='mdc-button__label' and normalize-space()='Ok']]")
	
	WebUI.waitForElementClickable(okBtn, 10)
	WebUI.click(okBtn)

	
} else {
	WebUI.takeScreenshot()
	WebUI.comment("TC thất bại: không thấy 'body-alert-icon' trong ${AFTER_LOGIN_WAIT}s sau khi login.")
	WebUI.verifyEqual(successVisible, true) // ép fail để có log/screenshot
}
