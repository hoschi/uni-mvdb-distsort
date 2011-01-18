/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.ifis.mvdbproject.joins.shared;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author hoschi
 */
public class TimeEntry implements Serializable {

	String message;
	Date date;

	public TimeEntry() {
	}

	public TimeEntry(String message, Date date) {
		this.message = message;
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "TimeEntry{" + "message=" + message + " date=" + date + '}';
	}
}
