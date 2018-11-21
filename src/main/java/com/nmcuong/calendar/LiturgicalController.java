package com.nmcuong.calendar;

import java.time.LocalDate;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.nmcuong.model.LiturgicalCalendar;

@Path("/liturgical")
public class LiturgicalController {

	private LocalDate gregorialDate;
	
	@GET
	public Response fromGregorianDate(@QueryParam("date") String date) {
		try {
			gregorialDate = LocalDate.parse(date);
		} catch (Exception e) {
			gregorialDate = LocalDate.now();
		}

		LiturgicalCalendar liturgicalCalendar = new LiturgicalCalendar();
		liturgicalCalendar.setWeekday(gregorialDate.getDayOfWeek().getValue());
		liturgicalCalendar.setYear(gregorialDate.getYear() % 2);
		liturgicalCalendar.setCycle(getCycle(gregorialDate.getYear()));

		return Response.ok(date).build();
	}

	private int getSeason() {
		LocalDate christmast = LocalDate.of(gregorialDate.getYear(), 12, 25);
		LocalDate epiphany = LocalDate.of(gregorialDate.getYear(), 1, 6);
		
		LocalDate beginAdvent = getFirstDayOfAdvent();
		
		if ((gregorialDate.isAfter(beginAdvent) || gregorialDate.isEqual(beginAdvent)) && gregorialDate.isBefore(christmast)) {
			return 1;
		}
		
		if ((gregorialDate.isAfter(christmast) || gregorialDate.isEqual(christmast)) && (gregorialDate.isBefore(epiphany)
				|| gregorialDate.isEqual(epiphany))) {
			return 2;
		}
		
		LocalDate beginLent = getEasterDate(gregorialDate.getYear()).minusDays(43);
		if ((gregorialDate.isAfter(beginLent) || gregorialDate.equals(beginLent)) && (gregorialDate.isBefore(beginLent.plusDays(41)))) {
			return 3;
		}
		
		return 0;
	}

	private LocalDate getFirstDayOfAdvent() {
		LocalDate advent = gregorialDate.minusDays(21);
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
