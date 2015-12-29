package introsde.finalproject.soap.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import introsde.finalproject.soap.dao.LifeCoachDao;

/**
 * The persistent class for the "Target" database table.
 * 
 */
@Entity
@Table(name="Target")
@NamedQuery(name="Target.findAll", query="SELECT t FROM Target t")
public class Target implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator="sqlite_target")
	@TableGenerator(name="sqlite_target", table="sequence",
	    pkColumnName="name", valueColumnName="seq",
	    pkColumnValue="Target")
	@Column(name="idTarget", nullable=false)
	private int idTarget;
	
	@Column(name="value", nullable=false)
	private int value;
	
	@Temporal(TemporalType.DATE)
	@Column(name="startDateTarget")
	private Date startDateTarget;
	
	@Temporal(TemporalType.DATE)
	@Column(name="endDateTarget")
	private Date endDateTarget;
	
	@Column(name="conditionTarget", nullable=false)
	private int conditionTarget;
	
	@Column(name="achieved")
	private int achieved;
	
	@ManyToOne
	@JoinColumn(name="idPerson",referencedColumnName="idPerson", nullable=false)
	private Person person;
	
	@ManyToOne
	@JoinColumn(name = "idMeasureDef", referencedColumnName = "idMeasureDef")
	private MeasureDefinition measureDefinition;
	
	public Target() {
    }
    
    @XmlAttribute(name="idTarget")
    public int getIdTarget() {
        return idTarget;
    }
   
    public void setIdTarget(int idTarget) {
        this.idTarget = idTarget;
    }
    
    /**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * @return the startDateTarget
	 */
	public Date getStartDateTarget() {
		return startDateTarget;
	}

	/**
	 * @param startDateTarget the startDateTarget to set
	 */
	public void setStartDateTarget(Date startDateTarget) {
		this.startDateTarget = startDateTarget;
	}

	/**
	 * @return the endDateTarget
	 */
	public Date getEndDateTarget() {
		return endDateTarget;
	}

	/**
	 * @param endDateTarget the endDateTarget to set
	 */
	public void setEndDateTarget(Date endDateTarget) {
		this.endDateTarget = endDateTarget;
	}

	/**
	 * @return the conditionTarget
	 */
	public int getConditionTarget() {
		return conditionTarget;
	}

	/**
	 * @param conditionTarget the conditionTarget to set
	 */
	public void setConditionTarget(int conditionTarget) {
		this.conditionTarget = conditionTarget;
	}

	/**
	 * @return the achieved
	 */
	public int getAchieved() {
		return achieved;
	}

	/**
	 * @param achieved the achieved to set
	 */
	public void setAchieved(int achieved) {
		this.achieved = achieved;
	}
	
	public MeasureDefinition getMeasureDefinition() {
		return measureDefinition;
	}

	public void setMeasureDefinition(MeasureDefinition param) {
		this.measureDefinition = param;
	}
	
	// we make this transient for JAXB to avoid and infinite loop on serialization
	@XmlTransient
	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
		
	// database operations
		public static Target getTargetById(int personId) {
			EntityManager em = LifeCoachDao.instance.createEntityManager();
			Target p = em.find(Target.class, personId);
			LifeCoachDao.instance.closeConnections(em);
			return p;
		}
		
		public static List<Target> getAll() {
			EntityManager em = LifeCoachDao.instance.createEntityManager();
		    List<Target> list = em.createNamedQuery("Target.findAll", Target.class).getResultList();
		    LifeCoachDao.instance.closeConnections(em);
		    return list;
		}
		
		public static Target saveTarget(Target p) {
			EntityManager em = LifeCoachDao.instance.createEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			em.persist(p);
			tx.commit();
		    LifeCoachDao.instance.closeConnections(em);
		    return p;
		}
		
		public static Target updateTarget(Target p) {
			EntityManager em = LifeCoachDao.instance.createEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			p=em.merge(p);
			tx.commit();
		    LifeCoachDao.instance.closeConnections(em);
		    return p;
		}
		
		public static void removeTarget(Target p) {
			EntityManager em = LifeCoachDao.instance.createEntityManager();
			EntityTransaction tx = em.getTransaction();
			tx.begin();
		    p=em.merge(p);
		    em.remove(p);
		    tx.commit();
		    LifeCoachDao.instance.closeConnections(em);
		}
}
