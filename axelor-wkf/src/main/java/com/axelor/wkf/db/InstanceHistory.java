package com.axelor.wkf.db;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.axelor.auth.db.AuditableModel;
import com.axelor.db.JPA;
import com.axelor.db.Model;
import com.axelor.db.Query;
import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

@Entity
@Table(name = "WORKFLOW_INSTANCE_HISTORY")
public class InstanceHistory extends AuditableModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Instance instance;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Transition transition;


	public InstanceHistory() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Instance getInstance() {
		return instance;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public Transition getTransition() {
		return transition;
	}

	public void setTransition(Transition transition) {
		this.transition = transition;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (!(obj instanceof InstanceHistory)) return false;
		
		InstanceHistory other = (InstanceHistory) obj;
		if (this.getId() != null && other.getId() != null) {
			return Objects.equal(this.getId(), other.getId());
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public String toString() {
		ToStringHelper tsh = Objects.toStringHelper(this);

		tsh.add("id", this.getId());

		return tsh.omitNullValues().toString();
	}

	/**
	 * Make the entity managed and persistent.
	 * 
	 * @see EntityManager#persist(Object)
	 */
	public InstanceHistory persist() {
		return JPA.persist(this);
	}

	/**
	 * Merge the state of the entity into the current persistence context.
	 * 
	 * @see EntityManager#merge(Object)
	 */
	public InstanceHistory merge() {
		return JPA.merge(this);
	}

	/**
	 * Save the state of the entity.<br>
	 * <br>
	 * It uses either {@link #persist()} or {@link #merge()} and calls
	 * {@link #flush()} to synchronize values with database.
	 * 
	 * @see #persist(Model)
	 * @see #merge(Model)
	 * 
	 */
	public InstanceHistory save() {
		return JPA.save(this);
	}
	
	/**
	 * Remove the entity instance.
	 * 
	 * @see EntityManager#remove(Object)
	 */
	public void remove() {
		JPA.remove(this);
	}
	
	/**
	 * Refresh the state of the instance from the database, overwriting changes
	 * made to the entity, if any.
	 * 
	 * @see EntityManager#refresh(Object)
	 */
	public void refresh() {
		JPA.refresh(this);
	}
	
	/**
	 * Synchronize the persistence context to the underlying database.
	 * 
	 * @see EntityManager#flush()
	 */
	public void flush() {
		JPA.flush();
	}
	
	/**
	 * Find a <code>InstanceHistory</code> by <code>id</code>.
	 *
	 */
	public static InstanceHistory find(Long id) {
		return JPA.find(InstanceHistory.class, id);
	}
	
	/**
	 * Return a {@link Query} instance for <code>InstanceHistory</code> to filter
	 * on all the records.
	 *
	 */
	public static Query<InstanceHistory> all() {
		return JPA.all(InstanceHistory.class);
	}
	
	/**
	 * A shortcut method to <code>InstanceHistory.all().filter(...)</code>
	 *
	 */
	public static Query<InstanceHistory> filter(String filter, Object... params) {
		return JPA.all(InstanceHistory.class).filter(filter, params);
	}
}
