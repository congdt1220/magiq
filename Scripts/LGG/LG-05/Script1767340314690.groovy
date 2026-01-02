
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.util.KeywordUtil

// =================== Cấu hình ===================
final int PAGELOAD_TIMEOUT = 120
final int WAIT_TIMEOUT = 129   // thời gian chờ nhanh cho present/visible/clickable

// =================== 1) Gọi Test Case khởi tạo trang (nếu cần) ===================


// =================== 2) Locator cho <a> 'West Coast Council' ===================
// XPath: đúng thẻ <a> có text hiển thị là "West Coast Council"
TestObject aWestCoast = new TestObject('a_west_coast_council')
aWestCoast.addProperty('xpath', ConditionType.EQUALS,
	"//h6[normalize-space(.)='a-West Coast Council']"
)

// =================== 3) KHÔNG SCROLL – chỉ chờ và click trực tiếp ===================
boolean isPresent  = WebUI.waitForElementPresent(aWestCoast, WAIT_TIMEOUT, FailureHandling.OPTIONAL)
boolean isVisible  = WebUI.waitForElementVisible(aWestCoast, WAIT_TIMEOUT, FailureHandling.OPTIONAL)
boolean isClickable = WebUI.waitForElementClickable(aWestCoast, WAIT_TIMEOUT, FailureHandling.OPTIONAL)

if (!(isPresent && isVisible)) {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ Không tìm thấy/không hiển thị '<a> West Coast Council' trong ${WAIT_TIMEOUT}s.")
}

// Thực hiện click trực tiếp bằng Katalon (Selenium) — KHÔNG JS
if (isClickable) {
	WebUI.click(aWestCoast)
	WebUI.comment("✅ Đã click '<a> West Coast Council' bằng WebUI.click (không dùng JS, không scroll).")
} else {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ Element '<a> West Coast Council' hiện hữu nhưng không clickable trong ${WAIT_TIMEOUT}s.")
}




// =================== 4) Kiểm tra tồn tại thẻ https://www.westcoast.tas.gov.au/ ===================
String targetUrl = "https://www.westcoast.tas.gov.au/"

// Tạo TestObject cho thẻ <a>
TestObject aTasGov = new TestObject('a_westcoast_tas_gov')
aTasGov.addProperty('xpath', ConditionType.EQUALS,
	"//a[@target='_blank' and @href='" + targetUrl + "']"
)

// Chỉ kiểm tra tồn tại (present)
boolean linkExists = WebUI.waitForElementPresent(aTasGov, WAIT_TIMEOUT, FailureHandling.OPTIONAL)

if (linkExists) {
	WebUI.comment("✅ Tìm thấy thẻ <a> với href='${targetUrl}' và target='_blank'.")
} else {
	WebUI.takeScreenshot()
	KeywordUtil.markFailed("❌ Không tìm thấy thẻ ${targetUrl} trong ${WAIT_TIMEOUT}s.")
}

