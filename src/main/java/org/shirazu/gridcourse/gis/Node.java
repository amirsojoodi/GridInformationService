package gis;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.TypedQuery;

import net.Server;

@Entity
public class Node {

	@Id
	@GeneratedValue
	@Column(name = "Node_ID")
	private long id;

	private transient static ArrayList<Node> allNodes;
	private String name;
	private String IP;

	@OneToMany(mappedBy = "node", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Resource> resources;

	public Node(String name, String IP) {
		setAllNodes(new ArrayList<Node>());
		setResources(new ArrayList<Resource>());
		setName(name);
		setIP(IP);
	}

	public Node() {
		setAllNodes(new ArrayList<Node>());
		setResources(new ArrayList<Resource>());
	}

	public void addResource(Resource resource) {
		resources.add(resource);
		resource.setNode(this);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ArrayList<Resource> getResources() {
		return (ArrayList<Resource>) resources;
	}

	public void setResources(ArrayList<Resource> resources) {
		this.resources = resources;
		for (Resource resource : resources) {
			resource.setNode(this);
		}
	}

	public static ArrayList<Node> getAllNodes() {
		try {
			// Server.getEm().getTransaction().begin();
			TypedQuery<Node> query = Server.getEm().createQuery(
					"SELECT n FROM Node n", Node.class);
			return (ArrayList<Node>) query.getResultList();

		} catch (IndexOutOfBoundsException e) {
			return allNodes;
		}
	}

	public static void setAllNodes(ArrayList<Node> allNodes) {
		Node.allNodes = allNodes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public static Node getNode(long nodeID) {
		try {
			Server.getEm().getTransaction().begin();
			TypedQuery<Node> query = Server.getEm().createQuery(
					"SELECT n FROM Node n WHERE n.id = " + nodeID, Node.class);
			Server.getEm().getTransaction().commit();
			return (Node) query.getResultList().get(0);

		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public void updateResources(ArrayList<Resource> newResources) {
		for (Resource newResource : newResources) {
			if (resources.contains(newResource)) {
				resources.get(resources.indexOf(newResource)).addAllAttributes(
						newResource.getAttributes());
				Server.getEm().getTransaction().begin();
				for (Attribute attribute : newResource.getAttributes()) {
					Server.getEm().persist(attribute);
				}
				Server.getEm().getTransaction().commit();
			} else {
				addResource(newResource);
				newResource.persist(); // This will persist attributes too.
			}
		}
	}

	// This method must be used only the first time the node is created
	public void persist() {
		Server.getEm().getTransaction().begin();
		Server.getEm().persist(this);
		for (Resource resource : getResources()) {
			Server.getEm().persist(resource);
			for (Attribute attribute : resource.getAttributes()) {
				Server.getEm().persist(attribute);
			}
		}
		Server.getEm().getTransaction().commit();
	}

	public void persistAttributes() {
		Server.getEm().getTransaction().begin();
		for (Resource resource : getResources()) {
			for (Attribute attribute : resource.getAttributes()) {
				Server.getEm().persist(attribute);
			}
		}
		Server.getEm().getTransaction().commit();
	}
}
