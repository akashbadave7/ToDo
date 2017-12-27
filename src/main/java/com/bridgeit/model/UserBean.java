package com.bridgeit.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.jboss.resteasy.annotations.providers.img.ImageWriterParams;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name="user")
public class UserBean 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY, generator="idgen")
	@GenericGenerator(name="idgen", strategy="increment")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="email")
	private String email;
	
	@JsonProperty(access=Access.WRITE_ONLY)
	@Column(name="password")
	private String password;
	
	@Column(name="mobilenumber")
	private String mobilenumber;
	
	@Column(name="isActivated")
	private boolean isActivated;
	
	@Column(name="picUrl",columnDefinition = "LONGBLOB")
	private String picUrl;
	
	@JsonIgnore
	//@LazyCollection(LazyCollectionOption.FALSE)
	@OneToMany(mappedBy="user")
	List<NoteBean> notes;

	
	@OneToMany(mappedBy = "user",fetch=FetchType.EAGER)
	private Set<Label> labels = new HashSet<Label>();

	
	
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public boolean isActivated() {
		return isActivated;
	}

	public void setActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}
	

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public List<NoteBean> getNotes() {
		return notes;
	}

	public void setNotes(List<NoteBean> notes) {
		this.notes = notes;
	}

	public Set<Label> getLabels() {
		return labels;
	}

	public void setLabels(Set<Label> labels) {
		this.labels = labels;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		//result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + id;
		/*result = prime * result + (isActivated ? 1231 : 1237);
		result = prime * result + ((labels == null) ? 0 : labels.hashCode());
		result = prime * result + ((mobilenumber == null) ? 0 : mobilenumber.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((picUrl == null) ? 0 : picUrl.hashCode());*/
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		UserBean user = (UserBean) obj;
		if (user.getId() == id) {
			return true;
		}
		return false;
	}
	
	
}