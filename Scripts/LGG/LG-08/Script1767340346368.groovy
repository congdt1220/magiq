
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

// =================== 1) Gọi Test Case 'LGG-05' ===================




// =================== 5) Verify h4 modal-message tồn tại ===================
// Target HTML:
// <h4 class="modal-message" style="text-align: center;margin: 15px; white-space: break-spaces;">
//     Please create a user account before accessing further information.
// </h4>

TestObject h4ModalMsg = new TestObject('h4_modal_message')
h4ModalMsg.addProperty(
	'xpath',
	ConditionType.EQUALS,
	"//h4[@class='modal-message' and normalize-space(.)='Please create a user account before accessing further information.']"
)

// 5.1) Có trong DOM?
boolean modalPresent = WebUI.waitForElementPresent(h4ModalMsg, 30, FailureHandling.OPTIONAL)
if (!modalPresent) {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ Không thấy <h4 class='modal-message'> với nội dung yêu cầu trong vòng 30s.")
}

// 5.2) Hiển thị trên màn hình?
boolean modalVisible = WebUI.waitForElementVisible(h4ModalMsg, 30, FailureHandling.OPTIONAL)
if (!modalVisible) {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ <h4 class='modal-message'> có trong DOM nhưng chưa hiển thị trong vòng 30s.")
}

// 5.3) (Tuỳ chọn) Xác nhận đúng text — phòng ngừa biến thể dấu/cách
String modalText = WebUI.getText(h4ModalMsg)
String expectedText = "Please create a user account before accessing further information."
if (modalText?.trim() != expectedText) {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ Text modal-message không khớp.\nExpected: '${expectedText}'\nActual  : '${modalText}'")
}

WebUI.comment("✅ Xác nhận: <h4 class='modal-message'> tồn tại, hiển thị, và đúng nội dung.")
