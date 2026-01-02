
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.util.KeywordUtil

// ---- Cấu hình ----
int LOCAL_PAGELOAD = 300                // Page load tối đa 5 phút
int WAIT_ELEMENT_BEFORE_RELOAD = 120    // Đợi element 2 phút trước khi reload
int MAX_RELOAD_ATTEMPTS = 5             // Số lần reload nếu chưa thấy element
int AFTER_LOGIN_WAIT = 120              // Tổng thời gian chờ sau login để thấy mat-app-bar
int SHORT = 5
int MEDIUM = 15
int LONG = 30


WebUI.openBrowser('')
WebUI.maximizeWindow()

WebUI.navigateToUrl('https://qa.engagement.magiqdev.cloud/login')
WebUI.waitForPageLoad(LOCAL_PAGELOAD)




// === Tạo TestObject động ===
TestObject spanAdmin = new TestObject('obj_span_admin_test_1')
spanAdmin.addProperty(
  'xpath',
  ConditionType.EQUALS,
  "//button[.//span[contains(normalize-space(), 'test')]]"
)

TestObject iExitToApp = new TestObject('obj_i_exit_to_app')
iExitToApp.addProperty(
  'xpath',
  ConditionType.EQUALS,
  "//i[normalize-space(text())='exit_to_app']"
)

TestObject userNameInput = new TestObject('obj_input_userName')
userNameInput.addProperty('id', ConditionType.EQUALS, 'userName')

// === Helper: hover góc trên bên phải (Robot) ===
void hoverTopRightWithRobot() {
  try {
	java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize()
	int x = screenSize.width - 5   // cách mép phải 5px
	int y = 5                      // cách mép trên 5px

	java.awt.Robot robot = new java.awt.Robot()
	robot.mouseMove(x, y)
	KeywordUtil.logInfo("🖱️ Hover đến góc trên bên phải (Robot) tại tọa độ: (${x}, ${y})")
	Thread.sleep(500) // nhỏ để đảm bảo hover có hiệu lực
  } catch (Exception e) {
	KeywordUtil.logWarning("Không thể hover bằng Robot: ${e.message}")
  }
}

String currentUrl = WebUI.getUrl()

if (currentUrl == 'https://qa.engagement.magiqdev.cloud/news') {
	try {
	  // (Tuỳ chọn) đảm bảo đang ở đúng trang trước khi click:
	  // WebUI.navigateToUrl('https://your-app-url')
	
	  // ⭐⭐ BƯỚC MỚI: Hover lên góc trên bên phải
	  hoverTopRightWithRobot()
	
	  // Click vào "Admin test 1"
	  WebUI.waitForElementVisible(spanAdmin, MEDIUM, FailureHandling.STOP_ON_FAILURE)
	  WebUI.click(spanAdmin)
	
	  // Click vào icon "exit_to_app"
	  WebUI.waitForElementVisible(iExitToApp, MEDIUM, FailureHandling.STOP_ON_FAILURE)
	  WebUI.click(iExitToApp)
	
	  // Chờ màn hình login có id="userName"
	  boolean present = WebUI.waitForElementPresent(userNameInput, LONG, FailureHandling.OPTIONAL)
	
	  if (present) {
		WebUI.verifyElementPresent(userNameInput, SHORT, FailureHandling.STOP_ON_FAILURE)
		KeywordUtil.logInfo('✅ Found element with id="userName" — Test PASSED')
	  } else {
		KeywordUtil.markFailed('❌ Element with id="userName" not found after actions')
	  }
	
	} catch (Exception e) {
	  KeywordUtil.markFailed("❌ Test failed with exception: ${e.message}")
	  throw e
	}

}



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

// Marker coi là đã login thành công: class 'mat-app-bar'
TestObject appBar = new TestObject('appBar')
appBar.addProperty('css', ConditionType.EQUALS, ".mat-app-bar")
appBar.addProperty('xpath', ConditionType.EQUALS, "//*[contains(@class,'mat-app-bar')]")

// ----- Đợi #userName tối đa 2 phút; nếu không thấy thì reload và thử lại -----
usernameVisible = false
int attempts = 0


usernameVisible = WebUI.waitForElementVisible(userNameField, 3, FailureHandling.OPTIONAL)


if (!usernameVisible) {
	TestObject okBtn = new TestObject('ok_button_in_modal')
	okBtn.addProperty('xpath', ConditionType.EQUALS,
	"//footer[contains(@class,'mdc-dialog__actions')]" +
	"//button[@aria-label='Ok' and .//span[@class='mdc-button__label' and normalize-space()='Ok']]")

	WebUI.waitForElementClickable(okBtn, 5)
	WebUI.enhancedClick(okBtn)
	usernameVisible = WebUI.waitForElementVisible(userNameField, WAIT_ELEMENT_BEFORE_RELOAD, FailureHandling.OPTIONAL)
}


if (!usernameVisible) {
	WebUI.takeScreenshot()
	WebUI.comment("Không thể hiển thị trường #userName sau ${attempts} lần reload.")
	WebUI.verifyEqual(usernameVisible, true) // ép fail để có log/screenshot
}

// ----- Khi đã thấy #userName: thực hiện login -----
WebUI.setText(userNameField, 'admin1test@gmail.com')

// Chờ ô password xuất hiện (có thể reload 1 lần nếu chưa thấy)
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

WebUI.setEncryptedText(passwordField, 'vckfKYHQ/X5a0LrHpsIEKA==')

// ----- Nút Login -----
boolean loginClickable = WebUI.waitForElementClickable(loginBtn, WAIT_ELEMENT_BEFORE_RELOAD, FailureHandling.OPTIONAL)
if (!loginClickable) {
	WebUI.comment("Nút Login chưa clickable sau ${WAIT_ELEMENT_BEFORE_RELOAD}s. Thử reload 1 lần.")
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

// ----- Sau khi click Login: coi là thành công nếu load được .mat-app-bar -----
boolean appBarVisible = false
int waited = 0
int step = 3 // polling mỗi 3s

while (waited < AFTER_LOGIN_WAIT || appBarVisible) {
	if (WebUI.verifyElementVisible(appBar, FailureHandling.OPTIONAL)) {
		appBarVisible = true
		break
	}
	WebUI.delay(step)
	waited += step
}

if (!appBarVisible) {
	// Có thể backend chậm -> thử refresh 1 lần và đợi lại
	WebUI.refresh()
	WebUI.waitForPageLoad(LOCAL_PAGELOAD)
	waited = 0
	while (waited < AFTER_LOGIN_WAIT) {
		if (WebUI.verifyElementVisible(appBar, FailureHandling.OPTIONAL)) {
			appBarVisible = true
			break
		}
		WebUI.delay(step)
		waited += step
	}
}

// Kết luận
if (appBarVisible) {
	WebUI.comment("Login thành công: thấy class 'mat-app-bar'.")
} else {
	WebUI.takeScreenshot()
	WebUI.comment("Không thấy 'mat-app-bar' sau khi login và một lần refresh.")
	WebUI.verifyEqual(appBarVisible, true) // ép fail để có log/screenshot
}

