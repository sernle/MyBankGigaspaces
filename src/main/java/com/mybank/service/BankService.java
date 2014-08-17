//=======================================================================
// ARCHIVO BankService.java
// FECHA CREACIÓN: 15/02/2014
//=======================================================================
package com.mybank.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fachada que agrupa los servicios proporcionados por MyBank
 * @author Hernán Tenjo
 * @version 1.0
 */
@Service
@Transactional(readOnly=true)
//public class BankService implements IBankService{
public class BankService{
//	//Repositorios que permite el acceso a la persistencia
//	@Autowired private IClientRepository clientRepository;
//	@Autowired private IAccountRepository accountRepository;
//	@Autowired private IMovementRepository movementRepository;
//	
//	/* (non-Javadoc)
//	 * @see com.mybank.service.IBankService#saveClient(com.mybank.model.Client)
//	 */
//	@Transactional(readOnly=false)
//	@Override
//	public Client saveClient(Client client) {
//		clientRepository.save(client);
//		return client;
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.mybank.service.IBankService#deleteClient(long)
//	 */
//	@Transactional(readOnly=false)
//	@Override
//	public void deleteClient(long clientId) {
//		movementRepository.deleteMassiveByClientId(clientId);
//		accountRepository.deleteMassiveByClientId(clientId);
//		clientRepository.delete(clientId);
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.mybank.service.IBankService#findClient(long)
//	 */
//	@Override
//	public Client findClient(long clientId) {
//		return clientRepository.findOne(clientId);
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.mybank.service.IBankService#listAllClients()
//	 */
//	@Override
//	public List<Client> listAllClients(int currentPage, int pageSize) {
//		Sort sort = new Sort(Direction.ASC, MyBankConstants.PARAM_ENTITY_ID);
//		Pageable page = new PageRequest(currentPage, pageSize, sort);
//		Page<Client> clientPage = clientRepository.findAll(page);
//		return clientPage.getContent(); 
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.mybank.service.IBankService#listAllClients()
//	 */
//	@Override
//	public List<Client> listAllClients() {
//		return clientRepository.findAll();
//	}
//
//	/* (non-Javadoc)
//	 * @see com.mybank.service.IBankService#listAllClientsByName()
//	 */
//	@Override
//	public List<Client> listAllClientsByName(String name) {
//		return clientRepository.findAllByNameLike(name);
//	}
//
//	/* (non-Javadoc)
//	 * @see com.mybank.service.IBankService#saveAccount(com.mybank.model.Account)
//	 */
//	@Transactional(readOnly=false)
//	@Override
//	public Account saveAccount(Account account) {
//		return accountRepository.save(account);
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.mybank.service.IBankService#deleteAccount(long)
//	 */
//	@Transactional(readOnly=false)
//	@Override
//	public void deleteAccount(long accountId) {
//		movementRepository.deleteMassiveByAccountId(accountId);
//		accountRepository.delete(accountId);
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.mybank.service.IBankService#findAccount(long)
//	 */
//	@Override
//	public Account findAccount(long accountId) {
//		return accountRepository.findOne(accountId);
//	}
//	
//	/*
//	 * (non-Javadoc)
//	 * @see com.mybank.service.IBankService#listAllAccounts(long)
//	 */
//	@Override
//	public List<Account> listAllAccountsByClient(long clientId) {
//		return accountRepository.findAllByClientId(clientId);
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.mybank.service.IBankService#saveMovement(com.mybank.model.Movement)
//	 */
//	@Transactional(readOnly=false)
//	@Override
//	public Movement saveMovement(Movement movement) {
//		Account account = movement.getAccount();
//		double balance = account.getBalance() + (movement.isCredit() ? -movement.getValue() : movement.getValue());
//		
//		if(balance < 0 ){
//			throw new IllegalStateException("La cuenta no puede estar sobregirada!");
//		}else{
//			account.setBalance(balance);
//			movementRepository.save(movement);
//			accountRepository.save(account);
//			return movement;
//		}
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.mybank.service.IBankService#deleteMovement(long)
//	 */
//	@Transactional(readOnly=false)
//	@Override
//	public void deleteMovement(long movementId) {
//		movementRepository.delete(movementId);
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.mybank.service.IBankService#findMovement(long)
//	 */
//	@Override
//	public Movement findMovement(long movementId) {
//		return movementRepository.findOne(movementId);
//	}
//	
//	/*
//	 * (non-Javadoc)
//	 * @see com.mybank.service.IBankService#listAllMovements(long)
//	 */
//	@Override
//	public List<Movement> listAllMovementsByAccount(long accountId) {
//		return movementRepository.findAll();
//	}
//
//	/* (non-Javadoc)
//	 * @see com.mybank.service.IBankService#generateMovementsReport(long, java.util.Date, java.util.Date)
//	 */
//	@Override
//	public Map<Account, List<Movement>> buildMovementsReport(long clientId, Date startDate, Date endDate) {
//		//Se consultan de forma independiente las cuentas por si no se encuentran movimientos asociados
//		List<Account> accounts = accountRepository.findAllByClientId(clientId);
//		List<Movement> movements = movementRepository.generateMovementsReport(clientId, startDate, endDate);
//		Map<Account, List<Movement>> reportInfo = new LinkedHashMap<Account, List<Movement>>();
//		
//		for(Account account : accounts){
//			reportInfo.put(account, new ArrayList<Movement>());
//		}
//		
//		for(Movement movement : movements){
//			reportInfo.get(movement.getAccount()).add(movement);
//		}
//		
//		return reportInfo;
//	}	
}