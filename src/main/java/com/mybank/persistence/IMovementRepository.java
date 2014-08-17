//=======================================================================
// ARCHIVO IMovementRepository.java
// FECHA CREACIÓN: 15/02/2014
//=======================================================================
package com.mybank.persistence;

import java.util.Date;
import java.util.List;

import com.mybank.model.Movement;

/**
 * Intefaz que expone las funcionalidades básicas de persistencia para la entidad {@link Movement}
 * @author Hernán Tenjo
 * @version 1.0
 */
public interface IMovementRepository {//extends JpaRepository<Movement, Long>{
	/**
	 * Permite encontrar todos los movimientos que se encuentran asociados a una cuenta
	 * @param accountId Identificador de la cuenta en la que se han registrado los movimientos
	 * @return Los movimientos que han sido registrados para la cuenta
	 */
	public List<Movement> findAllByAccountId(long accountId);
	
	/**
	 * Permite eliminar de forma masiva todos los movimientos asociados a una cuenta
	 * @param accountId Identificador de la cuenta a la que pertenecen los movimientos
	 */
//	@Modifying  
//	@Query("delete from Movement m where m.id in (select m2.id from Movement m2  where m2.account.id = ?1)")
	public void deleteMassiveByAccountId(long accountId);
	
	/**
	 * Permite eliminar de forma masivo todos los movimientos asociados a las cuentas de un cliente
	 * @param clientId Identificador del cliente dueño de las cuentas a las que perntenecen los movimientos
	 */
//	@Modifying  
//	@Query("delete from Movement m where m.id in (select m2.id from Movement m2 where m2.account.client.id = ?1)")
	public void deleteMassiveByClientId(long clientId);
	
	/**
	 * Permite obtener los movimientos dentro de un rango de fechas de las cuentas que pertenecen a un cliente
	 * @param clientId Identificador del cliente al que pertenecen los movimientos
	 * @param startDate Fecha inicial de los movimientos deseados
	 * @param endDate Fecha final de los movimientos deseados
	 * @return Los movimientos que cumplen con las condiciones dadas
	 */
//	@Query("select m from Movement m where m.id in (select m2.id from Movement m2 "
//			+ "where m2.date between ?2 and ?3 and m.account.client.id = ?1) order by m.date asc")
	public List<Movement> generateMovementsReport(long clientId, Date startDate, Date endDate);
}