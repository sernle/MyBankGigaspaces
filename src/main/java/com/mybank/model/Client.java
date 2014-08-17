//=======================================================================
// ARCHIVO Client.java
// FECHA CREACIÓN: 14/02/2014
//=======================================================================
package com.mybank.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.mybank.common.MyBankConstants;

/**
 * Clase que representa un cliente de MyBank
 * @author Hernán Tenjo
 * @version 1.0
 */
@Entity
@SpaceClass
@Table(name=MyBankConstants.PERSISTENCE_TABLE_NAME_CLIENT, schema = MyBankConstants.PERSISTENCE_SCHEMA_CORE)
public class Client extends AbstractEntity{
	//Identificador requerido para la serialización
	private static final long serialVersionUID = 1L;
	//Nombre completo
	private String name;
	//Dirección de residencia
	private String address;
	//Teléfono principal de contacto
	private String phoneNumber;
	
	//=======================================================================
	// 								METODOS GET/SET
	//=======================================================================
	/**
	 * Metodo que define como se debe generar el id de la entidad
	 * @return El identificador de la entidad que se desea persistir
	 */
	@Id
	@SpaceId(autoGenerate=true)
	public String getId() {
		return this.id;
	}
	
	/**
	 * Obtiene el nombre completo del cliente
	 * @return El nombre que se encuentra registrado para el cliente 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Modifica el nombre registrado del cliente
	 * @param name El nuevo nombre completo del cliente
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Obtiene la dirección de residencia del cliente
	 * @return La dirección que se encuentra registrada para el cliente
	 */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Modifica la dirección registrada del cliente 
	 * @param address La nueva dirección que será relacionada al cliente
	 * @Pre: La dirección no debe ser vacia
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
	/**
	 * Obtiene el teléfono de contacto principal del cliente
	 * @return El número principal que se encuentra registrado apra el cliente
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	/**
	 * Modifica el teléfono de contacto principal del cliente 
	 * @param phoneNumber El nuevo número telefónico que servira de contacto principal con el cliente
	 * @Pre: El número de teléfono no debe ser vacio
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}