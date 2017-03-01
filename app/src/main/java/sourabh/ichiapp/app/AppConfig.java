package sourabh.ichiapp.app;

public class AppConfig {
	// Server user login url
//	public static String URL_LOGIN = "http://192.168.0.102/android_login_api/login.php";

	// Server user register url
//	public static String BASE = "http://mindwingstech.com/LifeLine/services/v1/";
	public static String BASE = "http://192.168.0.101/ichi/v1/";

	public static String URL_GET_CITIES = BASE + "get_cities/";
	public static String URL_REGISTER = BASE + "register";
	public static String URL_LOGIN = BASE + "login";
	public static String URL_GET_ADSLIDERS = BASE + "get_adsliders";
	public static String URL_GET_SERVICE_CATEGORIES = BASE + "get_service_categories";
	public static String URL_GET_OFFER_CATEGORIES = BASE + "get_offer_categories";
	public static String URL_GET_SHOPPING_CATEGORIES = BASE + "get_shopping_categories";

	public static String URL_GET_SERVICEPROVIDERS = BASE + "get_service_providers/";
	public static String URL_GET_RETAILERS = BASE + "get_retailers/";
	public static String URL_CREATE_COUPON_REQUEST = BASE + "create_coupon_request";
	public static String URL_VALIDATE_COUPON = BASE + "validate_coupon/";



	public static String URL_REQUEST_BLOOD = BASE + "request_blood";
	public static String SMS_ORIGIN = "LYFLYN";
	public static String URL_VERIFY_OTP = BASE + "activate_user_status";
	public static final String OTP_DELIMITER = ":";
	public static String URL_REQUEST_OTP = BASE + "request_OTP";

	public static String URL_UPDATE_USER = BASE + "update_user";
	public static String URL_UPDATE_PASSWORD = BASE + "update_password";
	public static String URL_UPDATE_PASSWORD_BY_PHN = BASE + "update_password_by_phn";


	public static String URL_REQUEST_OTP_TO_CHANGE_PHN = BASE + "request_OTP_to_update_phn";


	public static String URL_GET_NEWS = BASE + "get_news";

	public static String URL_GET_BLOOD_REQUESTS = BASE + "get_blood_requests/";
	public static String URL_CLOSE_REQUEST = BASE + "close_request/";

	// Google project id
//	public static final String SENDER_ID = "mindwings-lifeline";
	public static final String SENDER_ID = "182991462265";

	public static final String API_KEY_GUEST= "guest";


	public static final String TAG = "LifeLine";
	public static final String DISPLAY_MESSAGE_ACTION =
			"lifeline.mindwings.lifeline.DISPLAY_MESSAGE";
	public static final String EXTRA_MESSAGE = "message";

	public static int TIMER = 8000;

}
