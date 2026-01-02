
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.util.KeywordUtil

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

import com.kms.katalon.core.util.KeywordUtil

// ===== Cấu hình thời gian =====
int PAGELOAD_TIMEOUT = 300            // tối đa 5 phút khi refresh
int WAIT_FIELD = 60                   // chờ mỗi field hiển thị
int MAX_RELOAD_ATTEMPTS = 1           // số lần refresh nhẹ nếu chậm (tuỳ UI)
int WAIT_AFTER_CLICK = 90             // chờ h3 'Select Organisation' sau khi submit
final int WAIT_TIMEOUT = 120


int MEDIUM = 15
int SHORT = 5
int LONG = 30
// ===== Gọi Test Case "CA-01" =====
try {
	java.awt.Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize()
	int x = 5                      // lệch mép trái 5px để tránh chạm sát mép/taskbar
	int y = screen.height - 5      // lệch mép dưới 5px

	java.awt.Robot robot = new java.awt.Robot()
	robot.mouseMove(x, y)
	WebUI.comment("🖱️ Đã di chuyển trỏ chuột tới góc trái dưới: (${x}, ${y})")
	Thread.sleep(400)              // độ trễ nhỏ để đảm bảo hover có hiệu lực
} catch (Exception e) {
	KeywordUtil.logWarning("⚠️ Không thể di chuyển bằng Robot: ${e.message}")
}

// =================== (Tuỳ chọn) Di chuyển theo offset trong vùng trang bằng Actions ===================
// Nếu bạn thực sự cần move theo offset tương đối với 'body' (ví dụ để click ở mép trái giữa),
// thì dùng Actions. Còn không, phần này có thể bỏ.
WebDriver driver = DriverFactory.getWebDriver()
WebElement bodyEl = driver.findElement(By.tagName("body"))
Actions actions = new Actions(driver)

// targetX/Y là offset tương đối so với phần tử body: x từ trái qua, y từ trên xuống.
// Ở đây đặt ví dụ: mép trái (x=0) và giữa chiều cao viewport (y ≈ bodyEl.height/2).
// Vì Selenium không cho đọc trực tiếp height của element qua WebElement API,
// ta dùng 0, 300 làm ví dụ an toàn. Bạn chỉnh theo UI của mình.
int targetX = 0
int targetY = 300

actions.moveToElement(bodyEl, targetX, targetY).perform()
WebUI.comment("🖱️ Đã move Actions tới body offset (x=${targetX}, y=${targetY}).")

// Nếu cần click một phát tại vị trí đó:
actions.click().perform()
WebUI.comment("✅ Đã click bằng Actions tại mép trái (x≈0), giữa màn hình (y≈${targetY}).")

// =================== 3) Click vào account ===================
TestObject aAccount = new TestObject('a_account')
aAccount.addProperty('xpath', ConditionType.EQUALS, "//a[@href='account']")

boolean present   = WebUI.waitForElementPresent(aAccount, WAIT_TIMEOUT, FailureHandling.OPTIONAL)
boolean visible   = WebUI.waitForElementVisible(aAccount, WAIT_TIMEOUT, FailureHandling.OPTIONAL)
boolean clickable = WebUI.waitForElementClickable(aAccount, WAIT_TIMEOUT, FailureHandling.OPTIONAL)

if (!(present && visible)) {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ Không tìm thấy/không hiển thị thẻ account trong ${WAIT_TIMEOUT}s.")
}

if (clickable) {
	WebUI.click(aAccount)
	WebUI.comment("✅ Đã click vào liên kết account.")
} else {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ Thẻ account hiện hữu nhưng không clickable trong ${WAIT_TIMEOUT}s.")
}




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
   present = WebUI.waitForElementPresent(userNameInput, LONG, FailureHandling.OPTIONAL)

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




