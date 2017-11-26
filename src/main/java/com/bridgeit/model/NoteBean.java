package com.bridgeit.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="notes")
public class NoteBean {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="idgen")
	@GenericGenerator(name="idgen", strategy="increment")
	private int noteId;
	
	@Column(name="note_title")
	private String title;
	
	@Column(name="note_body")
	private String body;
	
	@Column(name="note_color")
	private String color;
	
	@Column(name="create_date")
	private Date createDate;
	
	@Column(name="last_update")
	private Date lastUpdated;
	
	@Column(name="ARCHIVE")
	private boolean isArchive;
	
	@Column(name="PINNED")
	private boolean isPinned;
	
	@Column(name="TRASH")
	private boolean isTrash;
	

	@Column(name="REMINDER")
	private Date reminder;
	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private String image;
	
	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="user_id")
	UserBean user;
	
	public int getNoteId() {
		return noteId;
	}
	
	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	
	}

	public String getColor() {
	
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Date getLastUpdated() {
		return lastUpdated;
	}
	
	
	
	public boolean isArchive() {
		return isArchive;
	}
	

	public void setArchive(boolean isArchive) {
		this.isArchive = isArchive;
	}

	
	public boolean isPinned() {
		return isPinned;
	}

	public void setPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}

	public boolean isTrash() {
		return isTrash;
	}

	public void setTrash(boolean isTrash) {
		this.isTrash = isTrash;
	}

	

	public void setLastUpdated(Date lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	
	public UserBean getUser() {
		return user;
	}
	
	public void setUser(UserBean user) {
		this.user = user;
	}


	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Date getReminder() {
		return reminder;
	}

	public void setReminder(Date reminder) {
		this.reminder = reminder;
	}

}