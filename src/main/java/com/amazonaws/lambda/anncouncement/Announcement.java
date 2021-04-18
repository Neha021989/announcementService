package com.amazonaws.lambda.anncouncement;

import java.time.LocalDate;

public class Announcement {
	private String title;
    private String description;
    private LocalDate date;
    
    @SuppressWarnings("unused")
	private Announcement() {
    	
    }
	public Announcement(String title, String description, LocalDate date) {
		super();
		this.title = title;
		this.description = description;
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}


}
