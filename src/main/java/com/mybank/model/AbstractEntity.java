//=======================================================================
// ARCHIVO AbstractEntity.java
// FECHA CREACIÓN: 15/02/2014
//=======================================================================
package com.mybank.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Clase que modela los objetos persistentes que hacen parte del negocio de MyBank
 * @author Hernán Tenjo
 * @version 1.0
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable{
	//Identificador requerido para la serialización
	private static final long serialVersionUID = 1L;
	//Identificador único de la entidad
	protected String id;
	
	/**
	 * Metodo que garantiza que todas las entidades definan la forma de generar su identificador único 
	 * Se debe definir en cada entidad concreta para que se manejen de manera independiente en la BD
	 * @return el identificador de la entidad
	 */	
	@Transient
	public abstract String getId();
	
	/**
	 * Metodo que debe ser implementado para definir la 
	 * manera en la que se asignara un nuevo id a la entidad
	 * @param id Nuevo identificador que será asignado a la entidad
	 */
	public void setId(String id){
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		//Se garantiza que no se hagan comparaciones nulas
		if(obj == null){
			return false;
		}
		//Se garantiza la relacion reflexiva
		if(this == obj){
			 return true;
		}
		//Se garantiza que los dos objetos si sean de la misma clase
		//el instance of solo garantiza que pertenezcan a la misma familia
		if(this.getClass() != obj.getClass()){
			return false;
		}
		
		//Si lo anterior no se cumple se puede pasar a comparar las entidades
		//que contienen el id, atributo diferenciador entre entidades
		return this.getId().equals(this.getClass().cast(obj).getId());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getId().length()*17*this.getClass().getName().length();
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String pattern = "\n::: %s [id=%s]";
		return String.format(pattern, this.getClass().getSimpleName(), this.getId());
	}
}