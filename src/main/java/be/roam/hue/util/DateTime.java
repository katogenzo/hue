/*
 * Copyright (c) 2008 Kevin Wetzels
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package be.roam.hue.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Wrapper for {@link GregorianCalendar}.
 * <p>
 * Months are zero-based (just like the ones from {@link Calendar}). Uses 24
 * hours a day.
 * </p>
 * 
 * @author Kevin Wetzels
 * 
 */
public class DateTime implements Comparable<DateTime>, Cloneable {

	/**
	 * Enumeration of weekdays.
	 * 
	 * @author Kevin Wetzels
	 */
	public enum WeekDay {

		SUNDAY(Calendar.SUNDAY), MONDAY(Calendar.MONDAY), TUESDAY(Calendar.TUESDAY), WEDNESDAY(Calendar.WEDNESDAY), THURSDAY(Calendar.THURSDAY), FRIDAY(Calendar.FRIDAY), SATURDAY(Calendar.SATURDAY);

		public final int ordinal;

		WeekDay(int ordinal) {
			this.ordinal = ordinal;
		}

		/**
		 * Turns a {@link Calendar}'s value for a weekday into an instance of
		 * <code>WeekDay</code>.
		 * 
		 * @param ordinal
		 *            any of the <code>Calendar</code> values for weekdays such
		 *            as <code>Calendar.MONDAY</code>
		 * @return the corresponding instance of <code>WeekDay</code>
		 */
		public static WeekDay fromOrdinal(int ordinal) {
			return values()[ordinal - 1];
		}

	}

	/**
	 * Enumeration of months.
	 * 
	 * @author Kevin Wetzels
	 * 
	 */
	public enum Month {

		JANUARY(Calendar.JANUARY), FEBRUARY(Calendar.FEBRUARY), MARCH(Calendar.MARCH), APRIL(Calendar.APRIL), MAY(Calendar.MAY), JUNE(Calendar.JUNE), JULY(Calendar.JULY), AUGUST(Calendar.AUGUST), SEPTEMBER(Calendar.SEPTEMBER), OCTOBER(
				Calendar.OCTOBER), NOVEMBER(Calendar.NOVEMBER), DECEMBER(Calendar.DECEMBER);

		public final int ordinal;

		Month(int ordinal) {
			this.ordinal = ordinal;
		}

		/**
		 * Turns a {@link Calendar}'s value for a month into an instance of
		 * <code>Month</code>.
		 * 
		 * @param ordinal
		 *            any othe <code>Calendar</code> values for months such as
		 *            <code>Calendar.JANUARY</code>
		 * @return the corresponding instance of <code>Month</code>
		 */
		public static final Month fromOrdinal(int ordinal) {
			return values()[ordinal];
		}

	}

	private GregorianCalendar calendar;

	/**
	 * Constructs a <code>DateTime</code> object set to the current time.
	 */
	public DateTime() {
		calendar = new GregorianCalendar();
	}

	/**
	 * Constructs a <code>DateTime</code> object set to the given date.
	 * 
	 * @param date
	 *            date to set to
	 */
	public DateTime(Date date) {
		this();
		calendar.setTime(date);
	}

	/**
	 * Constructs a <code>DateTime</code> object set to the given calendar.
	 * 
	 * @param calendar
	 *            calendar to set to
	 */
	public DateTime(GregorianCalendar calendar) {
		this();
		calendar.setTimeInMillis(calendar.getTimeInMillis());
	}

	/**
	 * Constructs a copy of the given <code>DateTime</code> object.
	 * 
	 * @param dateTime
	 *            object to copy
	 */
	public DateTime(DateTime dateTime) {
		this(dateTime.getDate());
	}

	/**
	 * Constructs a <code>DateTime</code> object set to the given year, month
	 * and day.
	 * 
	 * @param year
	 *            year
	 * @param month
	 *            month (zero-based)
	 * @param day
	 *            day
	 */
	public DateTime(int year, int month, int day) {
		this(year, month, day, 0, 0, 0, 0);
	}

	/**
	 * Constructs a <code>DateTime</code> object set to the given year, month,
	 * day,...
	 * 
	 * @param year
	 *            year
	 * @param month
	 *            month (zero-based)
	 * @param day
	 *            day
	 * @param hours
	 *            hours
	 * @param minutes
	 *            minutes
	 * @param seconds
	 *            seconds
	 */
	public DateTime(int year, int month, int day, int hours, int minutes, int seconds) {
		calendar = new GregorianCalendar(year, month, day, hours, minutes, seconds);
	}

	/**
	 * Constructs a <code>DateTime</code> object set to the given year, month,
	 * day,...
	 * 
	 * @param year
	 *            year
	 * @param month
	 *            month (zero-based)
	 * @param day
	 *            day
	 * @param hours
	 *            hours
	 * @param minutes
	 *            minutes
	 * @param seconds
	 *            seconds
	 * @param milliseconds
	 *            milliseconds
	 */
	public DateTime(int year, int month, int day, int hours, int minutes, int seconds, int milliseconds) {
		this(year, month, day, hours, minutes, seconds);
		setMilliseconds(milliseconds);
	}

	/**
	 * Returns a {@link Date} object.
	 * 
	 * @return {@link Date} object
	 */
	public Date getDate() {
		return calendar.getTime();
	}

	/**
	 * Returns a {@link GregorianCalendar} object.
	 * 
	 * @return {@link GregorianCalendar} object
	 */
	public GregorianCalendar getCalendar() {
		return calendar;
	}

	/**
	 * Returns the time since epoch in milliseconds.
	 * 
	 * @return the time since epoch in milliseconds
	 */
	public long getTimeInMilliseconds() {
		return calendar.getTimeInMillis();
	}

	/**
	 * Returns the year.
	 * 
	 * @return the year
	 */
	public int getYear() {
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * Sets the year.
	 * 
	 * @param year
	 *            year to set
	 * @return current object instance (for chaining)
	 */
	public DateTime setYear(int year) {
		return set(Calendar.YEAR, year);
	}

	/**
	 * Returns the month as the value used by <code>GregorianCalendar</code>.
	 * 
	 * @return the month as the value used by <code>GregorianCalendar</code>.
	 */
	public int getMonthOrdinal() {
		return calendar.get(Calendar.MONTH);
	}

	/**
	 * Sets the month as the value used by <code>GregorianCalendar</code>.
	 * 
	 * @param month
	 *            month to set to
	 * @return current object instance (for chaining)
	 */
	public DateTime setMonthOrdinal(int month) {
		return set(Calendar.MONTH, month);
	}

	/**
	 * Returns the month
	 * 
	 * @return {@link Month}
	 */
	public Month getMonth() {
		return Month.fromOrdinal(getMonthOrdinal());
	}

	/**
	 * Sets the month.
	 * 
	 * @param month
	 *            month to set to
	 * @return current object instance (for chaining)
	 */
	public DateTime setMonth(Month month) {
		return setMonthOrdinal(month.ordinal);
	}

	/**
	 * Returns the day of the month.
	 * 
	 * @return the day of the month
	 */
	public int getDayOfMonth() {
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Sets the day of the month.
	 * 
	 * @param dayOfMonth
	 *            day of the month to set to
	 * @return current object instance (for chaining)
	 */
	public DateTime setDayOfMonth(int dayOfMonth) {
		return set(Calendar.DAY_OF_MONTH, dayOfMonth);
	}

	/**
	 * Returns the hour of the day (24 hours).
	 * 
	 * @return the hour of the day (24 hours).
	 */
	public int getHours() {
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * Sets the hour of the day (24 hours).
	 * 
	 * @param hours
	 *            hour to set to
	 * @return current object instance (for chaining)
	 */
	public DateTime setHours(int hours) {
		return set(Calendar.HOUR_OF_DAY, hours);
	}

	/**
	 * Returns the minute of the hour.
	 * 
	 * @return the minute of the hour
	 */
	public int getMinutes() {
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * Sets the minute of the hour
	 * 
	 * @param minutes
	 *            minute to set to
	 * @return current object instance (for chaining)
	 */
	public DateTime setMinutes(int minutes) {
		return set(Calendar.MINUTE, minutes);
	}

	/**
	 * Returns the second of the minute.
	 * 
	 * @return the second of the minute
	 */
	public int getSeconds() {
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * Sets the second of the minute.
	 * 
	 * @param seconds
	 *            second to set to
	 * @return current object instance (for chaining)
	 */
	public DateTime setSeconds(int seconds) {
		return set(Calendar.SECOND, seconds);
	}

	/**
	 * Returns the millisecond of the second.
	 * 
	 * @return the millisecond of the second
	 */
	public int getMilliseconds() {
		return calendar.get(Calendar.MILLISECOND);
	}

	/**
	 * Sets the millisecond of the second.
	 * 
	 * @param milliseconds
	 *            millisecond to set to
	 * @return current object instance (for chaining)
	 */
	public DateTime setMilliseconds(int milliseconds) {
		return set(Calendar.MILLISECOND, milliseconds);
	}

	/**
	 * Returns an array composed of (0) hours, (1) minutes, (2) second and (3)
	 * milliseconds.
	 * 
	 * @return an array composed of (0) hours, (1) minutes, (2) second and (3)
	 *         milliseconds.
	 */
	public int[] getTime() {
		return new int[] { getHours(), getMinutes(), getSeconds(), getMilliseconds() };
	}

	/**
	 * Sets the time.
	 * 
	 * @param hours
	 *            hour to set to
	 * @param minutes
	 *            minute to set to
	 * @param seconds
	 *            second to set to
	 * @param milliseconds
	 *            millisecond to set to
	 * @return current object instance (for chaining)
	 */
	public DateTime setTime(int hours, int minutes, int seconds, int milliseconds) {
		return setMilliseconds(milliseconds).setSeconds(seconds).setMinutes(minutes).setHours(hours);
	}

	/**
	 * Returns an array composed of (0) year, (1) month and (3) day of month.
	 * 
	 * @return an array composed of (0) year, (1) month and (3) day of month.
	 */
	public int[] getDay() {
		return new int[] { getYear(), getMonthOrdinal(), getDayOfMonth() };
	}

	/**
	 * Sets the day.
	 * 
	 * @param year
	 *            year
	 * @param month
	 *            month
	 * @param dayOfMonth
	 *            day of month
	 * @return current object instance (for chaining)
	 */
	public DateTime setDay(int year, int month, int dayOfMonth) {
		return setYear(year).setMonthOrdinal(month).setDayOfMonth(dayOfMonth);
	}

	/**
	 * Returns the day of the week as used by {@link GregorianCalendar}.
	 * 
	 * @return the day of the week as used by {@link GregorianCalendar}
	 */
	public int getDayOfWeekOrdinal() {
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Returns the day of the week.
	 * 
	 * @return the day of the week
	 */
	public WeekDay getDayOfWeek() {
		return WeekDay.fromOrdinal(getDayOfWeekOrdinal());
	}

	/**
	 * Adds the years.
	 * 
	 * @param years
	 *            years to add
	 * @return current object instance (for chaining)
	 */
	public DateTime addYears(int years) {
		return add(Calendar.YEAR, years);
	}

	/**
	 * Adds the months.
	 * 
	 * @param months
	 *            months to add
	 * @return current object instance (for chaining)
	 */
	public DateTime addMonths(int months) {
		return add(Calendar.MONTH, months);
	}

	/**
	 * Adds the days.
	 * 
	 * @param days
	 *            days to add
	 * @return current object instance (for chaining)
	 */
	public DateTime addDays(int days) {
		return add(Calendar.DAY_OF_MONTH, days);
	}

	/**
	 * Adds the hours.
	 * 
	 * @param hours
	 *            hours to add
	 * @return current object instance (for chaining)
	 */
	public DateTime addHours(int hours) {
		return add(Calendar.HOUR_OF_DAY, hours);
	}

	/**
	 * Adds the minutes.
	 * 
	 * @param minutes
	 *            minutes to add
	 * @return current object instance (for chaining)
	 */
	public DateTime addMinutes(int minutes) {
		return add(Calendar.MINUTE, minutes);
	}

	/**
	 * Adds the seconds.
	 * 
	 * @param seconds
	 *            seconds to add
	 * @return current object instance (for chaining)
	 */
	public DateTime addSeconds(int seconds) {
		return add(Calendar.SECOND, seconds);
	}

	/**
	 * Adds the milliseconds.
	 * 
	 * @param milliseconds
	 *            milliseconds to add
	 * @return current object instance (for chaining)
	 */
	public DateTime addMilliseconds(int milliseconds) {
		return add(Calendar.MILLISECOND, milliseconds);
	}

	/**
	 * Sets the hour, minute, second and millisecond fields to 0.
	 * 
	 * @return current object instance (for chaining)
	 */
	public DateTime clearTime() {
		return setTime(0, 0, 0, 0);
	}

	/**
	 * Checks if the given date is in the same year.
	 * 
	 * @param dateTime
	 *            object to compare to
	 * @return <code>true</code> when both are in the same year
	 */
	public boolean isSameYear(DateTime dateTime) {
		return (dateTime != null && getYear() == dateTime.getYear());
	}

	/**
	 * Checks if the given date is in the same month of the same year.
	 * 
	 * @param dateTime
	 *            object to compare to
	 * @return <code>true</code> when both are in the same month and the same
	 *         year
	 */
	public boolean isSameMonth(DateTime dateTime) {
		return (isSameYear(dateTime) && getMonthOrdinal() == dateTime.getMonthOrdinal());
	}

	/**
	 * Checks if the given date is in the same day of the same month of the same
	 * year.
	 * 
	 * @param dateTime
	 *            object to compare to
	 * @return <code>true</code> when both are in the same day of the month,
	 *         month and year
	 */
	public boolean isSameDay(DateTime dateTime) {
		return (isSameMonth(dateTime) && getDayOfMonth() == dateTime.getDayOfMonth());
	}

	/**
	 * Checks if the given date is in the same hour of the same day, month and
	 * year
	 * 
	 * @param dateTime
	 *            object to compare to
	 * @return <code>true</code> when both are in the same hour of the same day,
	 *         month and year
	 */
	public boolean isSameHour(DateTime dateTime) {
		return (isSameDay(dateTime) && getHours() == dateTime.getHours());
	}

	/**
	 * Checks if the given date is in the same minute of the same hour, day,
	 * month and year.
	 * 
	 * @param dateTime
	 *            object to compare to
	 * @return <code>true</code> when both are in the same minute of the same
	 *         hour, day, month and year
	 */
	public boolean isSameMinute(DateTime dateTime) {
		return (isSameHour(dateTime) && getMinutes() == dateTime.getMinutes());
	}

	/**
	 * Checks if the given date is in the same second of the same minute, hour,
	 * day, month and year.
	 * 
	 * @param dateTime
	 *            object to compare to
	 * @return <code>true</code> when both are in the same second of the same
	 *         minute, hour, day, month and year
	 */
	public boolean isSameSecond(DateTime dateTime) {
		return (isSameMinute(dateTime) && getSeconds() == dateTime.getSeconds());
	}
	
	public int compareTo(DateTime other) {
		if (other == null) {
			return 1;
		}
		long result = getTimeInMilliseconds() - other.getTimeInMilliseconds();
		if (result == 0L) {
			return 0;
		}
		return result > 0L ? 1 : -1;
	}

	@Override
	public int hashCode() {
		return calendar.hashCode();
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || !(object instanceof DateTime)) {
			return false;
		}
		return compareTo((DateTime) object) == 0;
	}

	@Override
	public DateTime clone() {
		return new DateTime(this);
	}

	private DateTime set(int field, int value) {
		calendar.set(field, value);
		return this;
	}

	private DateTime add(int field, int offset) {
		calendar.add(field, offset);
		return this;
	}

	@Override
	public String toString() {
		return getYear() + "-" + (getMonthOrdinal() + 1) + "-" + getDayOfMonth() + " " + getHours() + ":" + getMinutes() + ":" + getSeconds() + "." + getMilliseconds();
	}

}
