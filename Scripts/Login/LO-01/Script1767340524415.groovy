
// Test Cases/Master_Run_TC_After_TC1.groovy
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.util.KeywordUtil

import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType

// === Config timeouts ===
int SHORT = 5
int MEDIUM = 15
int LONG = 30


// === Tạo TestObject động ===
TestObject spanAdmin = new TestObject('obj_span_admin_test_1')
spanAdmin.addProperty(
  'xpath',
  ConditionType.EQUALS,
  "//button[.//span[normalize-space()='Admin test 1']]"
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
