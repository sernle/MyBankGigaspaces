//=======================================================================
// ARCHIVO IAccountRepository.java
// FECHA CREACIÓN: 15/02/2014
//=======================================================================
package com.mybank.persistence;

import java.util.List;

import com.mybank.model.Account;

/**
 * Intefaz que expone las funcionalidades básicas de persistencia para la entidad {@link Account}
 * @author Hernán Tenjo
 * @version 1.0
 */
//public interface IAccountRepository extends JpaRepository<Account, Long>{
public interface IAccountRepository{
	/**
	 * Permite encontrar todos las cuentas que se encuentran asociados a un cliente
	 * @param clientId Identificador del cliente al que pertenecen las cuentas
	 * @return Las cuentas que tiene registradas el cliente
	 */
	public List<Account> findAllByClientId(long clientId);
	
	/**
	 * Permite eliminar de forma masiva todos las cuentas que pertencen a un cliente
	 * @param clientId Identificador del cliente dueño de las cuentas que se desean eliminar
	 */
//	@Modifying  
//	@Query("delete from Account a where a.id in (select a2.id from Account a2  where a2.client.id = ?1)")
	public void deleteMassiveByClientId(long clientId);
}