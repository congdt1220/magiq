
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType

// ===== Gọi Test Case "XYZ" =====

// ===== Locator cho Email input và nút Reset Password =====
TestObject emailInput = new TestObject('obj_email_input')
emailInput.addProperty('xpath', ConditionType.EQUALS, "//input[@aria-label='Email']")

TestObject resetBtn = new TestObject('obj_reset_password_btn')
resetBtn.addProperty('xpath', ConditionType.EQUALS,
	"//button[normalize-space(.)='Reset Password'] | //span[normalize-space(.)='Reset Password']/ancestor::button"
)


TestObject divTitle = new TestObject('obj_div_title')
divTitle.addProperty('xpath', ConditionType.EQUALS, "//*[@class='div-title']")

// Chờ phần tử xuất hiện tối đa 30 giây




// ===== Thực thi =====
try {
	// Chờ input Email xuất hiện
	WebUI.waitForElementVisible(emailInput, 30, FailureHandling.STOP_ON_FAILURE)
	WebUI.setText(emailInput, 'valid@email.com')

	// Chờ nút Reset Password clickable
	WebUI.waitForElementClickable(resetBtn, 30, FailureHandling.STOP_ON_FAILURE)
	WebUI.click(resetBtn)
	boolean isVisible = WebUI.waitForElementVisible(divTitle, 30, FailureHandling.OPTIONAL)
	
	
	if (isVisible) {
		WebUI.verifyElementPresent(divTitle, 5, FailureHandling.STOP_ON_FAILURE)
		WebUI.comment("✅ Element với class='div-title' tồn tại.")
	} else {
		WebUI.takeScreenshot()
		WebUI.comment("❌ Không tìm thấy element với class='div-title'.")
		WebUI.verifyEqual(isVisible, true) // ép fail để có log/screenshot
	}
	
} catch (Exception e) {
	WebUI.takeScreenshot()
	WebUI.comment("❌ Lỗi khi thực hiện: ${e.message}")
	throw e
}
