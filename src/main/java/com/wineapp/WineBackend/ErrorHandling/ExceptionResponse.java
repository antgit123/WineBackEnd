package com.wineapp.WineBackend.ErrorHandling;

import java.util.Date;

public class ExceptionResponse {

	private String message;
	private Date timeStamp;

	public ExceptionResponse(String errorMessage, Date timeStamp) {
		this.message = errorMessage;
		this.timeStamp = timeStamp;
	}
	public String getMessage() {
		return message;
	}

	public void setErrorMessage(final String message) {
		this.message = message;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(final Date timeStamp) {
		this.timeStamp = timeStamp;
	}
}