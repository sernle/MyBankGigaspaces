//=======================================================================
// ARCHIVO MyBankConstants.java
// FECHA CREACIÓN: 15/02/2014
//=======================================================================
package com.mybank.common;

/**
 * Clase donde se agrupan las constantes de la aplicación
 * @author Hernán Tenjo
 * @version 1.0
 */
public class MyBankConstants {
	//Constantes útiles para el manejo de la persistencia
	public static final String PERSISTENCE_SCHEMA_CORE = "core";
	
	public static final String PERSISTENCE_TABLE_NAME_CLIENT = "client";
	public static final String PERSISTENCE_SEQUENCE_NAME_CLIENT = PERSISTENCE_SCHEMA_CORE + ".seq_" + PERSISTENCE_TABLE_NAME_CLIENT;
	
	public static final String PERSISTENCE_TABLE_NAME_ACCOUNT = "account";
	public static final String PERSISTENCE_SEQUENCE_NAME_ACCOUNT = PERSISTENCE_SCHEMA_CORE + ".seq_" + PERSISTENCE_TABLE_NAME_ACCOUNT;
	
	public static final String PERSISTENCE_TABLE_NAME_MOVEMENT = "movement";
	public static final String PERSISTENCE_SEQUENCE_NAME_MOVEMENT = PERSISTENCE_SCHEMA_CORE + ".seq_" + PERSISTENCE_TABLE_NAME_MOVEMENT;
	
	public static final String PARAM_ENTITY_ID = "id";
}