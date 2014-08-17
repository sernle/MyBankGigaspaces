//=======================================================================
// ARCHIVO IClientRepository.java
// FECHA CREACIÓN: 15/02/2014
//=======================================================================
package com.mybank.persistence;

import java.util.List;

import com.mybank.model.Client;

/**
 * Intefaz que expone las funcionalidades básicas de persistencia para la entidad {@link Client}
 * @author Hernán Tenjo
 * @version 1.0
 */
public interface IClientRepository {//extends JpaRepository<Client, Long>{
	/**
	 * Permite obtener los clientes que tiene un nombre con una cadena dada
	 * @param name Parte del nombre del cliente por el que se desea filtrar
	 * @return Los clientes que tienen un nombre con el filtro dado
	 */
//	@Query("FROM Client c WHERE lower(c.name) like lower(?1 || '%')")	
	public List<Client> findAllByNameLike(String name);
}

