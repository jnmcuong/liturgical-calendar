package com.nmcuong.model;

public class LiturgicalCalendar {
	private int year;
	
	private char cycle;
	
	private int season;
	
	private int week;
	
	private int weekday;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public char getCycle() {
		return cycle;
	}

	public void setCycle(char cycle) {
		this.cycle = cycle;
	}

	public int getSeason() {
		return season;
	}

	public void setSeason(int season) {
		this.season = season;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getWeekday() {
		return weekday;
	}

	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}

	public LiturgicalCalendar(int year, char cycle, int season, int week, int weekday) {
		super();
		this.year = year;
		this.cycle = cycle;
		this.season = season;
		this.week = week;
		this.weekday = weekday;
	}

	public LiturgicalCalendar() {
		super();
	}

	@Override
	public String toString() {
		return "LiturgicalCalendar [year=" + year + ", cycle=" + cycle + ", season=" + season + ", week=" + week
				+ ", weekday=" + weekday + "]";
	}
}
