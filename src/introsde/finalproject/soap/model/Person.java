package introsde.finalproject.soap.model;

import introsde.finalproject.soap.dao.LifeCoachDao;
import introsde.finalproject.soap.model.Measure;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;

import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * The persistent class for the "Person" database table.
 * 
 */
@Entity
@Table(name="Person")
@NamedQueries({
	@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p"),
//	@NamedQuery(name="Person.currentHealth", query="SELECT h FROM HealthMeasureHistory h "
//												+ "WHERE h.person = ?1 "
//												+ "GROUP BY h.measureType "
//												+ "HAVING h.timestamp = MAX(h.timestamp)"),
//	@NamedQuery(name="Person.readHistory", query="SELECT h FROM HealthMeasureHistory h "
//												+ "WHERE h.person = ?1 AND h.measureType LIKE ?2")
})
@XmlType(propOrder={"idPerson", "firstname", "lastname" , "birthdate", "email", "fiscalcode", "gender", "measure", "target", "doctor"})
public class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    // For sqlite in particular, you need to use the following @GeneratedValue annotation
    // This holds also for the other tables
    // SQLITE implements auto increment ids through named sequences that are stored in a 
    // special table named "sqlite_sequence"
    @GeneratedValue(generator="sqlite_person")
    @TableGenerator(name="sqlite_person", table="sequence",
        pkColumnName="name", valueColumnName="seq",
        pkColumnValue="Person",
        initialValue=1, allocationSize=1)
    @Column(name="idPerson", nullable=false)
    private int idPerson;
    
    @Column(name="firstname", nullable=false)
    private String firstname;
    
    @Column(name="lastname", nullable=false)
    private String lastname;
    
    @Temporal(TemporalType.DATE)
    @Column(name="birthdate", nullable=false)
    private Date birthdate;
    
    @Column(name="email")
    private String email;
    
    @Column(name="fiscalcode")
    private String fiscalcode;
    
    @Column(name="gender", nullable=false)
    private String gender;
    
    // mappedBy must be equal to the name of the attribute in Measure that maps this relation
    @OneToMany(mappedBy="person",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private List<Measure> measure;
    
    //mappedBy must be equal to the name of the attribute in Measure that maps this relation
    @OneToMany(mappedBy="person",cascade=CascadeType.ALL,fetch=FetchType.EAGER)
    private List<Target> target;
    
    @ManyToOne
	@JoinColumn(name = "idDoctor", referencedColumnName = "idDoctor")
	private Doctor doctor;
    
    public Person() {
    }
    
    public int person() {
        return this.idPerson;
    }
    
    @XmlElement(required=true)
    public int getIdPerson() {
        return idPerson;
    }
   
    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }
    
    @XmlElement(required=true)
    public String getFirstname() {
        return this.firstname;
    }
    
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    
    @XmlElement(required=true)
    public String getLastname() {
        return this.lastname;
    }
    
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    
    @XmlElement(required=true)
    public String getBirthdate(){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(this.birthdate);
    }

    public void setBirthdate(String bd) throws ParseException{
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date date = format.parse(bd);
        this.birthdate = date;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFiscalcode() {
        return this.fiscalcode;
    }

    public void setFiscalcode(String fiscalcode) {
        this.fiscalcode = fiscalcode;
    }
    
    @XmlElement(required=true)
    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    

    // the XmlElementWrapper defines the name of node in which the list of Measure elements
    // will be inserted
    @XmlElementWrapper(name = "Measurements")
    public List<Measure> getMeasure() {
        return this.measure;
    }

    public void setMeasure(List<Measure> param) {
        this.measure = param;
    }
    
    @XmlElementWrapper(name = "Targets")
    public List<Target> getTarget() {
        return this.target;
    }

    public void setTarget(List<Target> param) {
        this.target = param;
    }
    
 	public Doctor getDoctor() {
 		return this.doctor;
 	}

 	public void setDoctor(Doctor doctor) {
 		this.doctor = doctor;
 	}
 		
    // Database operations 
    public static Person getPersonById(int idPerson) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
        Person p = em.find(Person.class, idPerson);
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }
    
    public static List<Person> getAll() {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        em.getEntityManagerFactory().getCache().evictAll();
        List<Person> list = em.createNamedQuery("Person.findAll", Person.class).getResultList();
        LifeCoachDao.instance.closeConnections(em);
        return list;
    }
    
    public static Person savePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }
    
    public static Person updatePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
        return p;
    }
    
    public static void removePerson(Person p) {
        EntityManager em = LifeCoachDao.instance.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        p=em.merge(p);
        em.remove(p);
        tx.commit();
        LifeCoachDao.instance.closeConnections(em);
    }  
}