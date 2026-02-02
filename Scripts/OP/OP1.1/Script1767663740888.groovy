import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

WebUI.callTestCase(findTestCase('Login/LG-01'), [:], FailureHandling.STOP_ON_FAILURE)


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

