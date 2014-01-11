package de.shop.artikelverwaltung.domain;

import static de.shop.util.Constants.KEINE_ID;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.logging.Logger;

import de.shop.util.persistence.AbstractAuditable;


@XmlRootElement
@Entity
@Table(indexes = @Index(columnList = "bezeichnung"))
@NamedQueries({
	@NamedQuery(name  = Artikel.FIND_VERFUEGBARE_ARTIKEL,
            	query = "SELECT      a"
            	        + " FROM     Artikel a"
						+ " WHERE    a.ausgesondert = FALSE"
                        + " ORDER BY a.id ASC"),
	@NamedQuery(name  = Artikel.FIND_ARTIKEL_BY_BEZ,
            	query = "SELECT      a"
                        + " FROM     Artikel a"
						+ " WHERE    a.bezeichnung LIKE :" + Artikel.PARAM_BEZEICHNUNG
						+ "          AND a.ausgesondert = FALSE"
			 	        + " ORDER BY a.id ASC"),
   	@NamedQuery(name  = Artikel.FIND_ARTIKEL_MAX_PREIS,
            	query = "SELECT      a"
                        + " FROM     Artikel a"
						+ " WHERE    a.preis < :" + Artikel.PARAM_PREIS
			 	        + " ORDER BY a.id ASC"),
	@NamedQuery(name  = Artikel.FIND_BEZEICHNUNG_BY_PREFIX,
       query = "SELECT   DISTINCT a.bezeichnung"
	        + " FROM  Artikel a "
    		+ " WHERE UPPER(a.bezeichnung) LIKE UPPER(:"
    		+ Artikel.PARAM_ARTIKEL_BEZEICHNUNG_PREFIX + ")")
})
public class Artikel extends AbstractAuditable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7595241091410918682L;

	private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	private static final int BEZEICHNUNG_LENGTH_MAX = 32;
	
	private static final String PREFIX = "Artikel.";
	public static final String FIND_VERFUEGBARE_ARTIKEL = PREFIX + "findVerfuegbareArtikel";
	public static final String FIND_ARTIKEL_BY_BEZ = PREFIX + "findArtikelByBez";
	public static final String FIND_ARTIKEL_MAX_PREIS = PREFIX + "findArtikelByMaxPreis";
	public static final String FIND_BEZEICHNUNG_BY_PREFIX = PREFIX + "findArtikelByBezeichnung";

	public static final String PARAM_BEZEICHNUNG = "bezeichnung";
	public static final String PARAM_PREIS = "preis";
	public static final String PARAM_ARTIKEL_BEZEICHNUNG_PREFIX = "bezeichnung";

	@Id
	@GeneratedValue
	@Basic(optional = false)
	private Long id = KEINE_ID;
	
	@NotNull(message = "{artikelverwaltung.artikel.bezeichnung.notNull}")
	@Size(max = BEZEICHNUNG_LENGTH_MAX, message = "{artikelverwaltung.artikel.bezeichnung.length}")
	private String bezeichnung = "";
	
	//@Digits legt fest wieviele integer-Zeichen erlaubt sind und wieviele Stellen nach dem Komma (fraction)
	
	@Digits(integer = 10, fraction = 2, message = "{artikelverwaltung.artikel.preis.digits}")
	private BigDecimal preis;
	
	@Basic(optional = false)
	private boolean ausgesondert;
	
	public Artikel() {
		super();
	}
	
	public Artikel(String bezeichnung, BigDecimal preis) {
		super();
		this.bezeichnung = bezeichnung;
		this.preis = preis;
	}

	@PostPersist
	private void postPersist() {
		LOGGER.debugf("Neuer Artikel mit ID=%d", id);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBezeichnung() {
		return bezeichnung;
	}

	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public void setPreis(BigDecimal preis) {
		this.preis = preis;
	}

	public BigDecimal getPreis() {
		return preis;
	}

	public boolean isAusgesondert() {
		return ausgesondert;
	}

	public void setAusgesondert(boolean ausgesondert) {
		this.ausgesondert = ausgesondert;
	}
	
	public void setValues(Artikel a) {
		bezeichnung = a.bezeichnung;
		preis = a.preis;
		ausgesondert = a.ausgesondert;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ausgesondert ? 1231 : 1237);
		result = prime * result
				+ ((bezeichnung == null) ? 0 : bezeichnung.hashCode());
		result = prime * result + ((preis == null) ? 0 : preis.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Artikel other = (Artikel) obj;
		if (ausgesondert != other.ausgesondert)
			return false;
		if (bezeichnung == null) {
			if (other.bezeichnung != null) {
				return false;
			}
		}
		else if (!bezeichnung.equals(other.bezeichnung)) {
			return false;
		}
		if (preis == null) {
			if (other.preis != null) {
				return false;
			}
		}
		else if (!preis.equals(other.preis)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Artikel [id=" + id + ", bezeichnung=" + bezeichnung
		       + ", preis=" + preis + ", ausgesondert=" + ausgesondert
		       + ", " + super.toString() + "]";
	}
}
