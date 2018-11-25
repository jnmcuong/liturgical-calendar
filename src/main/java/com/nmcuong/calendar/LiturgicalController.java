package com.nmcuong.calendar;

import java.time.LocalDate;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.nmcuong.model.LiturgicalCalendar;

@Path("/liturgical")
public class LiturgicalController {

	private LocalDate toDate;
	
	private LocalDate christmast;
	
	private LocalDate epiphany;
	
	private LocalDate beginAdvent;
	
	private LocalDate easterDate;
	
	private LocalDate beginLent;
	
	private int season;
	
	@GET
	public Response fromGregorianDate(@QueryParam("date") String date) {
		try {
			toDate = LocalDate.parse(date);
		} catch (Exception e) {
			toDate = LocalDate.now();
		}

		LiturgicalCalendar liturgicalCalendar = new LiturgicalCalendar();
		liturgicalCalendar.setWeekday(toDate.getDayOfWeek().getValue());
		liturgicalCalendar.setYear(toDate.getYear() % 2);
		liturgicalCalendar.setCycle(getCycle(toDate.getYear()));
		season = getSeason(); 
		liturgicalCalendar.setSeason(season);
		liturgicalCalendar.setWeek(getWeek());
		return Response.ok(liturgicalCalendar).build();
	}

	private int getWeek() {
		int week = 0;
		if (season == 1) {
			week = getWeekFromTwoDate(beginAdvent, toDate);
		}
		
		if (season == 2) {
			week = getWeekFromTwoDate(christmast, toDate);
		}
		
		if (season == 3) {
			week = getWeekFromTwoDate(beginLent, toDate);
		}
		
		if (season == 4) {
			week = getWeekFromTwoDate(easterDate, toDate);
		}
		
		if (season == 0) {
			if (toDate.isBefore(beginLent)) {
				week = getWeekFromTwoDate(epiphany, toDate);
			} else {
				week = getWeekFromTwoDate(easterDate.plusDays(50), toDate) + 6;
			}
		}
		
		return week;
	}
	
	static private int getWeekFromTwoDate(LocalDate beginDate, LocalDate endDate) {
		return (endDate.getDayOfYear() - beginDate.getDayOfYear()) / 7 + 1;
	}
	
	private LocalDate getEpiphanyDate() {
		LocalDate epiphany = christmast.plusWeeks(1);
		epiphany = epiphany.plusDays(epiphany.getDayOfWeek().getValue() == 7 ? 7 : 7 - epiphany.getDayOfWeek().getValue());
		return epiphany;
	}
	
	private int getSeason() {
		christmast = LocalDate.of(toDate.getYear(), 12, 25);
		epiphany = getEpiphanyDate();
		
		beginAdvent = getFirstDayOfAdvent();
		
		
		
		if ((toDate.isAfter(beginAdvent) || toDate.isEqual(beginAdvent)) && toDate.isBefore(christmast)) {
			return 1;
		}
		
		if ((toDate.isAfter(christmast) || toDate.isEqual(christmast)) && (toDate.isBefore(epiphany)
				|| toDate.isEqual(epiphany))) {
			return 2;
		}
		
		easterDate = getEasterDate(toDate.getYear());
		beginLent = easterDate.minusDays(43);
		if ((toDate.isAfter(beginLent) || toDate.equals(beginLent)) && (toDate.isBefore(beginLent.plusDays(41)))) {
			return 3;
		}
		
		if ((toDate.isAfter(easterDate) || toDate.equals(easterDate)) && toDate.isBefore(easterDate.minusDays(51))) {
			return 4;
		}
		
		return 0;
	}

	private LocalDate getFirstDayOfAdvent() {
		LocalDate advent = toDate.minusDays(21);
		int dayofweek = advent.getDayOfWeek().getValue();
		advent = advent.minusDays(dayofweek);
		return advent;
	}
	
	private LocalDate getEasterDate(int year) {
		int a = year % 19;
		int b = year / 100;
		int c = year % 100;
		int d = b / 4;
		int e = b % 4;
		int f = (b + 8) / 25;
		int g = (b - f + 1) / 3;
		int h = (19 * a + b - d - g + 15) % 30;
		int i = c / 4;
		int k = c % 4;
		int l = (32 + 2 * e + 2 * i - h - k) % 7;
		int m = (a + 11 * h + 22 * l) / 451;
		int n = (h + l - 7 * m + 114) / 31;
		int p = (h + l - 7 * m + 114) % 31;
		LocalDate easterDate = LocalDate.of(year, n - 1, p + 1);
		return easterDate;
	}

	private char getCycle(int year) {
		if (year % 3 == 1) {
			return 'A';
		} else if (year % 3 == 2) {
			return 'B';
		}
		return 'C';
	}
}
