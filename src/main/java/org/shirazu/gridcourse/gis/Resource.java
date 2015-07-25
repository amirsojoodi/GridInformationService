package org.shirazu.gridcourse.gis;

//============================================================================
//Author      : Fatemeh Marzban, Amir Hossein Sojoodi
//Version     : 0.0.1
//Year        : 2014
//Copyright   : GNU
//Description : Grid Information Service in Java
//============================================================================

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import net.Server;

@Entity
public class Resource{

	@Id
	@GeneratedValue
	@Column(name = "Resource_ID")
	private long id;

	// TODO - read enumeration configuration
	@Column
	private ResourceType resourceType;

	private String name;

	@OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Attribute> attributes;

	@ManyToOne
	@JoinColumn(name = "Node_ID", nullable = false)
	private Node node;
	
	public Resource(ResourceType rt, String name) {
		setAttributes(new ArrayList<Attribute>());
		setResourceType(rt);
		setName(name);
	}

	public Resource() {
		setAttributes(new ArrayList<Attribute>());
	}

	public void persist() {
		Server.getEm().getTransaction().begin();
		Server.getEm().persist(this);
		for (Attribute attribute : getAttributes()) {
			Server.getEm().persist(attribute);
		}
		Server.getEm().getTransaction().commit();
	}

	public void addAllAttributes(ArrayList<Attribute> newAttributes) {
		attributes.addAll(newAttributes);
		for (Attribute attribute : newAttributes) {
			attribute.setResource(this);
		}
	}

	public void addAttribute(Attribute attribute) {
		attributes.add(attribute);
		attribute.setResource(this);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ArrayList<Attribute> getAttributes() {
		return (ArrayList<Attribute>) attributes;
	}

	public void setAttributes(ArrayList<Attribute> attributes) {
		this.attributes = attributes;
		for (Attribute attribute : attributes) {
			attribute.setResource(this);
		}
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	@Override
	public boolean equals(Object obj) {
		return this.getResourceType() == ((Resource) obj).getResourceType();
	}
}
