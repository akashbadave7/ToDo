package com.bridgeit.model;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.ManyToAny;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="LABEL")
public class Label {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO , generator = "label")
	@GenericGenerator(name="label",strategy="native")
	@Column(name="LABEL_ID")
	private int labelId;
	
	@Column(name="LABEL_NAME")
	private String name;
	
	
	@ManyToOne
	@JoinColumn(name="user_id")
	@JsonIgnore
	UserBean user;
	
	@ManyToMany(mappedBy="labels")
	@JsonIgnore
	private Set<NoteBean> notes = new HashSet<NoteBean>();

	public int getLabelId() {
		return labelId;
	}

	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}

	public String getName() {
		return name;
		}

	public void setName(String name) {
		this.name = name;
	}

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public Set<NoteBean> getNotes() {
		return notes;
	}

	public void setNotes(Set<NoteBean> notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		return "Label [labelId=" + labelId + ", name=" + name + ", user=" + user + ", notes=" + notes + "]";
	}

}
