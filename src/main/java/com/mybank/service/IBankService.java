//=======================================================================
// ARCHIVO IBankService.java
// FECHA CREACIÓN: 15/02/2014
//=======================================================================
package com.mybank.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mybank.model.Account;
import com.mybank.model.Client;
import com.mybank.model.Movement;

/**
 * Interfaz que expone los servicios proporcionados por MyBank
 * @author Hernán Tenjo
 * @version 1.0
 */
public interface IBankService {
	/**
	 * Servicio que persiste un cliente 
	 * @param client Objeto con la información del cliente que se desea persistir
	 * @return Entidad persistida
	 */
	public Client saveClient(Client client);
	
	/**
	 * Servicio que eliminar un cliente
	 * @param clientId Identificador del cliente que se desea eliminar
	 */
	public void deleteClient(String clientId);
	
	/**
	 * Servicio que busca un cliente por su identificador
	 * @param clientId Identificador único del cliente que se desea encontrar
	 * @return Objeto con la información del cliente si existe
	 */
	public Client findClient(String clientId);

	/**
	 * Servicio que busca todos los clientes del sistema
	 * @return Todos los clientes que se encuentran registrados en el sistema
	 */
	public List<Client> listAllClients();
	
	/**
	 * Servicio que busca todos los clientes del sistema
	 * @return Todos los clientes que se encuentran registrados en el sistema
	 */
	public List<Client> listAllClientsByName(String namePattern);
	
	/**
	 * Servicio que persiste una cuenta
	 * @param account Objeto con la información de la cuenta que se desea persistir
	 * @return Entidad persistida
	 */
	public Account saveAccount(Account account);
	
	/**
	 * Servicio que elimina una cuenta
	 * @param accountId Identificador de la cuenta que se desea eliminar
	 */
	public void deleteAccount(String accountId);
	
	/**
	 * Servicio que busca una cuenta por su identificar
	 * @param accountId Identificador único de la cuenta que se desea encontrar
	 * @return Objeto con la información de la cuenta si existe
	 */
	public Account findAccount(String accountId);
	
	/**
	 * Servicio que busca todas las cuentas que pertenecen a un cliente dado
	 * @param clientId Identificador único del cliente al que pertenecen las cuentas
	 * @return Las cuentas que pertenecen al cliente dado
	 */
	public List<Account> listAllAccountsByClient(String clientId);
	
	/**
	 * Servicio que persiste un movimiento
	 * @param movement Objeto con la información del movimiento que se desea persistir
	 * @return Entidad persistida
	 */
	public Movement saveMovement(Movement movement);
	
	/**
	 * Servicio que elimina un movimiento
	 * @param movementId Identificador del movimiento que se desea eliminar
	 */
	public void deleteMovement(String movementId);
	
	/**
	 * Servicio que busca un movimiento por su identificador
	 * @param movementId Identificar único de la cuenta que se desea encontrar
	 * @return Objeto con la información del movimiento si existe
	 */
	public Movement findMovement(String movementId);
	
	/**
	 * Servicio que busca todos los movimientos que pertenecen a una cuenta dada
	 * @param accountId Identificador único de la cuenta a la que pertenecen los movimientos
	 * @return Los movimientos que pertenecen a la cuenta dada
	 */
	public List<Movement> listAllMovementsByAccount(String accountId);
	
	/**
	 * Servicio que construye la información requerida para el reporte de movimientos x cuenta x cliente
	 * realizados dentro de unas fechas dadas
	 * @param clientId Identificador del cliente al que pertenece la información
	 * @param startDate Fecha inicial de los movimientos deseados
	 * @param endDate Fecha final de los movimientos deseados
	 * @return Mapa que relaciona las cuentas (Key) con sus movimientos (Value)
	 */
	public Map<Account, List<Movement>> buildMovementsReport(String clientId, Date startDate, Date endDate);
}