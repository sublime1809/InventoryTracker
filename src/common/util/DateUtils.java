package common.util;

import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Utility class containing date/time functionality
 */
public final class DateUtils {
	
	/**
	 * Private Constructor.
	 */
	private DateUtils() {
		assert false;
	}

	/**
	 * Regular format string for dates.
	 */
	public static final String DATE_FORMAT = "MM/dd/yyyy";
	
	/**
	 * Short format string for dates.
	 */
	public static final String SHORT_DATE_FORMAT = "MM/dd/yy";
	
	/**
	 * Format string for date/time.
	 */
	public static final String DATE_TIME_FORMAT = "MM/dd/yyyy hh:mm a";
	
	private static SimpleDateFormat dateFormatter;
	private static SimpleDateFormat shortDateFormatter;
	private static SimpleDateFormat dateTimeFormatter;
	
	static {
		dateFormatter = new SimpleDateFormat(DATE_FORMAT);
		shortDateFormatter = new SimpleDateFormat(SHORT_DATE_FORMAT);
		dateTimeFormatter = new SimpleDateFormat(DATE_TIME_FORMAT);
	}
	
	/**
	 * Formats a Date object as a string of the form "MM/dd/yyyy".
	 * 
	 * @param d Date to be formatted.
	 * 
	 * {@pre d != null}
	 * 
	 * {@post Returns a date string of the form "MM/dd/yyyy".}
	 */
	public static String formatDate(Date d) {
		return dateFormatter.format(d);
	}
	
	/**
	 * Formats a Date object as a string of the form "MM/dd/yy"
	 * 
	 * @param d Date to be formatted.
	 * 
	 * {@pre d != null}
	 * 
	 * {@post Returns a date string of the form "MM/dd/yy".}
	 */
	public static String formatShortDate(Date d) {
		return shortDateFormatter.format(d);
	}
	
	/**
	 * Parses a date string of the form "MM/dd/yyyy" and converts it to a Date object
	 * 
	 * @param text String to be parsed.
	 * 
	 * @throws ParseException if text is not of the form "MM/dd/yyyy".
	 * 
	 * {@pre text contains a string of the form "MM/dd/yyyy"}
	 * 
	 * {@post Returns a Date object containing the parsed date.}
	 */
	public static Date parseDate(String text) throws ParseException {
		Date result = dateFormatter.parse(text);
		String resultText = formatDate(result);
		if (!resultText.equals(text)) {
			throw new ParseException("Invalid date", 0);
		}
		return result;
	}

	/**
	 * Formats a Date object as a string of the form "MM/dd/yyyy hh:mm a"
	 * 
	 * @param d Date object to be formatted.
	 * 
	 * {@pre d != null}
	 * 
	 * {@post Returns a string of the form "MM/dd/yyyy hh:mm a".}
	 */
	public static String formatDateTime(Date d) {
		return dateTimeFormatter.format(d);
	}
	
	/**
	 * Parses a string of the form "MM/dd/yyyy hh:mm a" and converts it to a Date object.
	 * 
	 * @param text String to be parsed.
	 * 
	 * @throws ParseException if text is not of the form "MM/dd/yyyy hh:mm a"
	 * 
	 * {@pre text contains a string of the form "MM/dd/yyyy hh:mm a"}
	 * 
	 * {@post Returns a Date object containing the parsed date/time.}
	 */
	public static Date parseDateTime(String text) throws ParseException {
		Date result = dateTimeFormatter.parse(text);
		String resultText = formatDateTime(result);
		if (!resultText.equals(text)) {
			throw new ParseException("Invalid date/time", 0);
		}
		return result;
	}
	
	/**
	 * Normalizes the time portion of a Date object to midnight.
	 * 
	 * @param date The Date object to be normalized.
	 * 
	 * {@pre date != null}
	 * 
	 * {@post The time portion of date has been set to midnight.}
	 */
	public static Date removeTimeFromDate(Date date) {
		GregorianCalendar calendar = new GregorianCalendar();
		
		calendar.setTime(date);
		
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime();
	}

	/**
	 * Returns a Date object for the current date with the time portion
	 * set to 12AM.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns a Date object for the current date with the time portion
	 * set to 12AM.}
	 */
	public static Date currentDate() {
		return removeTimeFromDate(new Date());
	}

	/**
	 * Returns the earliest date supported by the product.
	 * 
	 * {@pre None}
	 * 
	 * {@post Returns the earliest date supported by the product.}
	 */
	public static Date earliestDate() {
		GregorianCalendar calendar = new GregorianCalendar();
		
		calendar.set(Calendar.MONTH, Calendar.JANUARY);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.YEAR, 2000);		
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime();
	}

	public static Date addMonths(Date date, int months)
	{
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(GregorianCalendar.MONTH, cal.get(GregorianCalendar.MONTH) + months);
		
		return cal.getTime();
	}
	
	public static void main(String[] args)
	{
		System.out.println(addMonths(new Date(), 4));
	}
}

