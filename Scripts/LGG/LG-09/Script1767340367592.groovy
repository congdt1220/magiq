
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.webui.driver.DriverFactory
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions

// =================== Cấu hình ===================
final int PAGELOAD_TIMEOUT = 120
final int WAIT_TIMEOUT     = 120   // thời gian chờ cho present/visible/clickable


// =================== 6) Click vào button aria-label="Ok" (không reload/JS, KHÔNG dùng role) ===================
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.util.KeywordUtil

TestObject btnOk = new TestObject('btn_ok')
btnOk.addProperty('xpath', ConditionType.EQUALS,
	"//button[@aria-label='Ok']"
)

// 6.1) Chờ có trong DOM (không reload)
boolean okPresent = WebUI.waitForElementPresent(btnOk, 20, FailureHandling.OPTIONAL)
if (!okPresent) {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ Không thấy <button aria-label='Ok'> trong vòng 20s (không reload).")
}

// 6.2) Chờ hiển thị
boolean okVisible = WebUI.waitForElementVisible(btnOk, 15, FailureHandling.OPTIONAL)
if (!okVisible) {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ <button aria-label='Ok'> có trong DOM nhưng chưa hiển thị trong vòng 15s.")
}

// 6.3) Chờ clickable rồi click trực tiếp (không JS)
boolean okClickable = WebUI.waitForElementClickable(btnOk, 10, FailureHandling.OPTIONAL)
if (!okClickable) {
	WebUI.comment("🟡 Nút 'Ok' chưa clickable → delay nhẹ rồi thử lại (không reload).")
	WebUI.delay(2)
	okClickable = WebUI.waitForElementClickable(btnOk, 8, FailureHandling.OPTIONAL)
}
if (!okClickable) {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ <button aria-label='Ok'> không clickable trong thời gian cho phép.")
}

WebUI.click(btnOk)
WebUI.comment("✅ Đã click <button aria-label='Ok'> bằng WebUI.click (không reload/JS, không dùng role).")




// ===== Tạo TestObject theo text (an toàn khi class/id thay đổi) =====
TestObject h4CreateAcc_Safe = new TestObject('h4_create_account_safe')
h4CreateAcc_Safe.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//h4[normalize-space(.)='Create New Account']"
)

// ===== 1) Present? =====
boolean presentSafe = WebUI.waitForElementPresent(h4CreateAcc_Safe, 20, FailureHandling.OPTIONAL)
if (!presentSafe) {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ Không thấy <h4> 'Create New Account' (safe) trong 20s.")
}

// ===== 2) Visible? =====
boolean visibleSafe = WebUI.waitForElementVisible(h4CreateAcc_Safe, 20, FailureHandling.OPTIONAL)
if (!visibleSafe) {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ <h4> 'Create New Account' (safe) có trong DOM nhưng chưa hiển thị.")
}

// ===== 3) (Tuỳ chọn) Xác nhận text =====
String safeText = WebUI.getText(h4CreateAcc_Safe)?.trim()
if (safeText != "Create New Account") {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ Text không khớp (safe). Expected: 'Create New Account', Actual: '${safeText}'.")
}

WebUI.comment("✅ Xác nhận: <h4> 'Create New Account' (safe) tồn tại và hiển thị.")


