/**
 * 
 */
package com.mybank.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gigaspaces.query.IdQuery;
import com.j_spaces.core.client.SQLQuery;
import com.mybank.model.Account;
import com.mybank.model.Client;
import com.mybank.model.Movement;

/**
 * @author hernan.tenjo
 *
 */
@Service
@Transactional(readOnly=true)
public class BankGigaspaceService implements IBankService {
	@Autowired
	private GigaSpace space;
	
	/* (non-Javadoc)
	 * @see com.mybank.service.IBankService#saveClient(com.mybank.model.Client)
	 */
	@Transactional(readOnly=false)
	@Override
	public Client saveClient(Client client) {
		space.write(client);
		return client;
	}

	/* (non-Javadoc)
	 * @see com.mybank.service.IBankService#deleteClient(String)
	 */
	@Transactional(readOnly=false)
	@Override
	public void deleteClient(String clientId) {
		SQLQuery<Client> queryClient = new SQLQuery<Client>(Client.class, "id = ?");
		SQLQuery<Account> queryAccount = new SQLQuery<Account>(Account.class, "client.id = ?");
		SQLQuery<Movement> queryMovement = new SQLQuery<Movement>(Movement.class, "account.client.id = ?");
		
		queryClient.setParameter(1, clientId);
		queryAccount.setParameter(1, clientId);
		queryMovement.setParameter(1, clientId);
		
		space.takeMultiple(queryMovement);
		space.takeMultiple(queryAccount);
		space.take(queryClient);
	}

	/* (non-Javadoc)
	 * @see com.mybank.service.IBankService#findClient(String)
	 */
	@Override
	public Client findClient(String clientId) {
		IdQuery<Client> idQuery = new IdQuery<Client>(Client.class, clientId);
		return space.readById(idQuery);
	}

	/* (non-Javadoc)
	 * @see com.mybank.service.IBankService#listAllClients()
	 */
	@Override
	public List<Client> listAllClients() {
		Client arrayClients[] = space.readMultiple(new Client());
		List<Client> clients = Arrays.asList(arrayClients);
		return clients;
	}

	/* (non-Javadoc)
	 * @see com.mybank.service.IBankService#saveAccount(com.mybank.model.Account)
	 */
	@Transactional(readOnly=false)
	@Override
	public Account saveAccount(Account account) {
		space.write(account);
		return account;
	}

	/* (non-Javadoc)
	 * @see com.mybank.service.IBankService#deleteAccount(String)
	 */
	@Transactional(readOnly=false)
	@Override
	public void deleteAccount(String accountId) {
		SQLQuery<Account> queryAccount = new SQLQuery<Account>(Account.class, "id = ?");
		SQLQuery<Movement> queryMovement = new SQLQuery<Movement>(Movement.class, "account.id = ?");
		
		queryAccount.setParameter(1, accountId);
		queryMovement.setParameter(1, accountId);
		
		space.takeMultiple(queryMovement);
		space.take(queryAccount);
	}

	/* (non-Javadoc)
	 * @see com.mybank.service.IBankService#findAccount(String)
	 */
	@Override
	public Account findAccount(String accountId) {
		IdQuery<Account> idQuery = new IdQuery<Account>(Account.class, accountId);
		return space.readById(idQuery);
	}

	/* (non-Javadoc)
	 * @see com.mybank.service.IBankService#listAllAccountsByClient(String)
	 */
	@Override
	public List<Account> listAllAccountsByClient(String clientId) {
		SQLQuery<Account> query = new SQLQuery<Account>(Account.class, "client.id = ?");
		query.setParameter(1, clientId);
		
		Account arrayAccounts[] = space.readMultiple(query);
		List<Account> accounts = Arrays.asList(arrayAccounts);
		return accounts;
	}

	/* (non-Javadoc)
	 * @see com.mybank.service.IBankService#saveMovement(com.mybank.model.Movement)
	 */
	@Transactional(readOnly=false)
	@Override
	public Movement saveMovement(Movement movement) {
		Account account = movement.getAccount();
		double balance = account.getBalance() + (movement.isCredit() ? -movement.getValue() : movement.getValue());
		
		if(balance < 0 ){
			throw new IllegalStateException("La cuenta no puede estar sobregirada!");
		}else{
			account.setBalance(balance);
			space.write(movement);
			space.write(account);
			return movement;
		}
	}

	/* (non-Javadoc)
	 * @see com.mybank.service.IBankService#deleteMovement(String)
	 */
	@Transactional(readOnly=false)
	@Override
	public void deleteMovement(String movementId) {
		SQLQuery<Movement> queryMovement = new SQLQuery<Movement>(Movement.class, "id = ?");
		queryMovement.setParameter(1, movementId);
		space.take(queryMovement);
	}

	/* (non-Javadoc)
	 * @see com.mybank.service.IBankService#findMovement(String)
	 */
	@Override
	public Movement findMovement(String movementId) {
		IdQuery<Movement> idQuery = new IdQuery<Movement>(Movement.class, movementId);
		return space.readById(idQuery);
	}

	/* (non-Javadoc)
	 * @see com.mybank.service.IBankService#listAllMovementsByAccount(String)
	 */
	@Override
	public List<Movement> listAllMovementsByAccount(String accountId) {
		SQLQuery<Movement> query = new SQLQuery<Movement>(Movement.class, "account.id = ?");
		query.setParameter(1, accountId);
		
		Movement arrayMovements[] = space.readMultiple(query);
		List<Movement> movements = Arrays.asList(arrayMovements);
		return movements;
	}

	/* (non-Javadoc)
	 * @see com.mybank.service.IBankService#buildMovementsReport(String, java.util.Date, java.util.Date)
	 */
	@Override
	public Map<Account, List<Movement>> buildMovementsReport(String clientId, Date startDate, Date endDate) {
		//Se consultan de forma independiente las cuentas por si no se encuentran movimientos asociados
		List<Account> accounts = listAllAccountsByClient(clientId);
		SQLQuery<Movement> movementsQuery = new SQLQuery<Movement>(Movement.class, "account.client.id = ? AND movDate BETWEEN ? AND ?");
		movementsQuery.setParameter(1, clientId);
		movementsQuery.setParameter(2, startDate);
		movementsQuery.setParameter(3, endDate);
		
		List<Movement> movements = Arrays.asList(space.readMultiple(movementsQuery));
		Map<Account, List<Movement>> reportInfo = new LinkedHashMap<Account, List<Movement>>();
		
		for(Account account : accounts){
			reportInfo.put(account, new ArrayList<Movement>());
		}
		
		for(Movement movement : movements){
			reportInfo.get(movement.getAccount()).add(movement);
		}
		
		return reportInfo;

	}

	/*
	 * (non-Javadoc)
	 * @see com.mybank.service.IBankService#listAllClientsByName(java.lang.String)
	 */
	@Override
	public List<Client> listAllClientsByName(String namePattern) {
		SQLQuery<Client> queryClient = new SQLQuery<Client>(Client.class, " name like '%?%'");
		queryClient.setParameter(1, namePattern);
		Client[] clients = space.readMultiple(queryClient);
		return Arrays.asList(clients);
	}
}
