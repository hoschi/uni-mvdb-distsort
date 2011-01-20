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
	Type type;
	boolean stuff;

	public TimeEntry() {
	}

	public enum Type {
		invoke,
		get,
		replay,
		received,
		traffic,
		logic
	};

	public TimeEntry(String message, Date date, Type type, boolean stuff) {
		this.message = message;
		this.date = date;
		this.type = type;
		this.stuff = stuff;
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public boolean isStuff() {
		return stuff;
	}

	public void setStuff(boolean stuff) {
		this.stuff = stuff;
	}

	@Override
	public String toString() {
		return "TimeEntry{" + "message=" + message + "type=" + type + "stuff=" + stuff + '}';
	}

	
}
