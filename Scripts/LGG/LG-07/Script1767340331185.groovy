
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


// =================== 2) Di chuyển con trỏ tới mép trái dưới (Robot) ===================
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



// =================== 4) Verify h4 "Guest Account!" tồn tại ===================
TestObject h4Guest = new TestObject('h4_guest_account')
h4Guest.addProperty('xpath', ConditionType.EQUALS,
	"//h4[@class='mat-h4' and normalize-space(.)='Guest Account!']"
)

// 4.1) Có trong DOM?
boolean h4Present = WebUI.waitForElementPresent(h4Guest, 30, FailureHandling.OPTIONAL)
if (!h4Present) {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ Không thấy <h4 class='mat-h4'>Guest Account!</h4> trong vòng 30s.")
}

// 4.2) Hiển thị trên màn hình?
boolean h4Visible = WebUI.waitForElementVisible(h4Guest, 30, FailureHandling.OPTIONAL)
if (!h4Visible) {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ <h4> 'Guest Account!' có trong DOM nhưng chưa hiển thị trong vòng 30s.")
}

// 4.3) (Tuỳ chọn) Xác nhận đúng text — đề phòng UI có biến thể
String h4Text = WebUI.getText(h4Guest)
if (h4Text?.trim() != "Guest Account!") {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ Text h4 không khớp. Expected: 'Guest Account!', Actual: '${h4Text}'.")
}

WebUI.comment("✅ Xác nhận: <h4 class='mat-h4'>Guest Account!</h4> tồn tại và hiển thị đúng.")


