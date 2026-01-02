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


import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.util.KeywordUtil



// ====== Expected text ======
String EXPECTED_TEXT = "If the email you entered is associated with an account, you will receive a password reset email shortly."

// ====== Locator cho class 'div-title' ======
// Dùng contains theo chuẩn xử lý multi-class: contains(concat(' ', normalize-space(@class), ' '), ' div-title ')
TestObject divTitle = new TestObject('obj_div_title')
divTitle.addProperty('xpath', ConditionType.EQUALS, "//*[@class='div-title']")

// ====== Chờ element xuất hiện và verify text ======
try {
	// Đợi element visible
	boolean visible = WebUI.waitForElementVisible(divTitle, 60, FailureHandling.OPTIONAL)
	if (!visible) {
		WebUI.takeScreenshot()
		KeywordUtil.markFailed("❌ Không thấy element với class='div-title' sau 60s.")
	}

	// Lấy text thực tế và so sánh
	String actual = WebUI.getText(divTitle).trim()
	// So khớp chính xác (case-sensitive), có thể dùng verifyMatch nếu muốn linh hoạt hơn
	boolean matched = (actual == EXPECTED_TEXT)

	if (matched) {
		WebUI.comment("✅ Text khớp chính xác: '${EXPECTED_TEXT}'")
	} else {
		WebUI.takeScreenshot()
		WebUI.comment("❌ Text không khớp.\n- Actual:   '${actual}'\n- Expected: '${EXPECTED_TEXT}'")
		WebUI.verifyEqual(actual, EXPECTED_TEXT) // Ép fail để có log/screenshot
	}

} catch (Exception e) {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ Lỗi khi verify div-title: ${e.message}")
	throw e
}

