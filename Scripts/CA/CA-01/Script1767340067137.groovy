
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.util.KeywordUtil

// ===== Cấu hình chung =====
int LOCAL_PAGELOAD = 300                // Page load tối đa 5 phút
int WAIT_ELEMENT_BEFORE_RELOAD = 120    // Đợi element tối đa 2 phút trước khi reload
int MAX_RELOAD_ATTEMPTS = 5             // Số lần reload nếu chưa thấy element
 WAIT_AFTER_CLICK = 60               // Chờ element sau khi click

WebUI.openBrowser('')
WebUI.maximizeWindow()

WebUI.navigateToUrl('https://qa.engagement.magiqdev.cloud/login')
WebUI.waitForPageLoad(LOCAL_PAGELOAD)

// ===== Locator: anchor có href="/accountregister" =====
TestObject aAccountRegister = new TestObject('a_account_register')
aAccountRegister.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//a[@href='/accountregister']"
)

// ===== Locator: class 'div-alignment-header' (xử lý multi-class an toàn) =====
TestObject divAlignmentHeader = new TestObject('div_alignment_header')
divAlignmentHeader.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//*[contains(concat(' ', normalize-space(@class), ' '), ' div-alignment-header ')]"
)

// ===== Chờ anchor xuất hiện; nếu chưa thấy thì reload theo chiến lược giống mẫu =====
boolean linkVisible = false
int attempts = 0

while (attempts <= MAX_RELOAD_ATTEMPTS) {
	linkVisible = WebUI.waitForElementVisible(aAccountRegister, WAIT_ELEMENT_BEFORE_RELOAD, FailureHandling.OPTIONAL)
	if (linkVisible) {
		break
	}
	WebUI.comment("Không thấy <a href=\"/accountregister\"> sau ${WAIT_ELEMENT_BEFORE_RELOAD}s. Reload lần ${attempts + 1}.")
	WebUI.refresh()
	WebUI.waitForPageLoad(LOCAL_PAGELOAD)
	attempts++
}

if (!linkVisible) {
	WebUI.takeScreenshot()
	WebUI.comment("Không thể hiển thị <a href=\"/accountregister\"> sau ${attempts} lần reload.")
	WebUI.verifyEqual(linkVisible, true) // ép fail để có log/screenshot
}

// ===== Click anchor khi đã thấy =====
boolean clickable = WebUI.waitForElementClickable(aAccountRegister, 20, FailureHandling.OPTIONAL)
if (!clickable) {
	WebUI.comment("Anchor /accountregister chưa clickable, thử delay nhẹ.")
	WebUI.delay(2)
	clickable = WebUI.waitForElementClickable(aAccountRegister, 20, FailureHandling.OPTIONAL)
}
if (!clickable) {
	WebUI.takeScreenshot()
	WebUI.comment("Anchor /accountregister không clickable.")
	WebUI.verifyEqual(clickable, true)
}

WebUI.click(aAccountRegister)
WebUI.comment("🖱️ Đã click /accountregister.")

// ===== Sau khi click: chờ load và kiểm tra class 'div-alignment-header' =====
boolean headerVisible = WebUI.waitForElementVisible(divAlignmentHeader, WAIT_AFTER_CLICK, FailureHandling.OPTIONAL)

// Nếu chưa thấy, trang SPA có thể chậm → thử refresh 1 lần và chờ lại
if (!headerVisible) {
	WebUI.comment("⌛ Chưa thấy element với class='div-alignment-header'. Thử refresh 1 lần và chờ lại.")
	WebUI.refresh()
	WebUI.waitForPageLoad(LOCAL_PAGELOAD)
	headerVisible = WebUI.waitForElementVisible(divAlignmentHeader, WAIT_AFTER_CLICK, FailureHandling.OPTIONAL)
}

if (headerVisible) {
	WebUI.verifyElementPresent(divAlignmentHeader, 10, FailureHandling.STOP_ON_FAILURE)
	WebUI.comment("✅ Thành công: tồn tại element với class='div-alignment-header' sau khi vào trang account register.")
} else {
	WebUI.takeScreenshot()
	WebUI.comment("❌ Không thấy element với class='div-alignment-header' sau khi click /accountregister.")
	WebUI.verifyEqual(headerVisible, true) // ép fail để có log/screenshot
}

// (Tuỳ bạn) WebUI.closeBrowser()
