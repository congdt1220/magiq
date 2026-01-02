
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


