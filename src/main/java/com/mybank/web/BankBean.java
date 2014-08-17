//=======================================================================
// ARCHIVO BankBean.java
// FECHA CREACIÓN: 15/02/2014
//=======================================================================
package com.mybank.web;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.springframework.dao.DataIntegrityViolationException;

import com.mybank.model.Account;
import com.mybank.model.Client;
import com.mybank.model.Movement;
import com.mybank.service.IBankService;

/**
 * Bean que gestiona las operaciones de la página de transacciones del banco
 * @author Hernán Tenjo
 * @version 1.0
 */
@ManagedBean
@ViewScoped
public class BankBean implements Serializable{
	private static final long serialVersionUID = 1L;
	//Fachada de servicios del banco
    @ManagedProperty(value="#{bankService}")
    private IBankService bankService;
    //Atributos que permiten gestionar los componentes de la página
    private List<Client> currentClients;
    private List<Account> currentAccounts;
    private List<Movement> currentMovements;
    private Client selectedClient;
    private Account selectedAccount;
    private Client formClient;
    private Account formAccount;
    private Movement formMovement;
    private boolean showReports;
    private boolean showClientForm;
    private boolean showAccountForm;
    
    /**
     * Método que inicia las variables requeridas al cargar la página
     */
    @PostConstruct
    public void init(){
    	listClients();
    	listAccounts();
    }
    
    /**
     * Busca todos los clientes del sistema 
     */
    public void listClients(){
    	currentClients = bankService.listAllClients();
    }
    
    /**
     * Busca todas las cuentas que pertenecen al cliente seleccionado
     */
    public void listAccounts(){
    	if(selectedClient != null){
    		currentAccounts = bankService.listAllAccountsByClient(selectedClient.getId());
    		selectedAccount = null;
    	}else{
    		currentAccounts = null;
    	}
    }
    
    /**
     * Persiste un cliente que ha sido creado o actualizada
     */
	public void saveClient(){
		bankService.saveClient(formClient);
		listClients();
		displayClientList();
	}
	
	/**
	 * Persiste una cuenta que ha sido creada o actualizada
	 */
	public void saveAccount(){
		try{
			formAccount.setClient(selectedClient);
			bankService.saveAccount(formAccount);
			listAccounts();
			displayAccountList();
			
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Cuenta Registrada", null));
		}catch(DataIntegrityViolationException e){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "El número de cuenta ya existe", null));
		}
	}
	
	/**
	 * Persiste un movimiento asociado a una cuenta seleccionada
	 */
	public void saveMovement(){
		formMovement.setAccount(selectedAccount);
		formMovement.setMovDate(new Date());
		
		try{
			bankService.saveMovement(formMovement);
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Transacción Registrada", null));
		}catch(IllegalStateException e){
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
		}
	}
	
	/**
	 * Elimina el cliente que se ha seleccionado
	 */
	public void deleteClient(){
		bankService.deleteClient(formClient.getId());
		currentAccounts = null;
		listClients();
	}
	
	/**
	 * Elimina la cuenta que se ha seleccionado
	 */
	public void deleteAccount(){
		bankService.deleteAccount(formAccount.getId());
		listAccounts();
	}

	/**
	 * Obtiene los clientes que poblarán la lista correspondiente
	 * @return Lista con los clientes actuales
	 */
	public List<Client> getCurrentClients() {
		return currentClients;
	}

	/**
	 * Obtiene las cuentas que poblarán la lista correspondiente
	 * @return Lista con las cuentas que pertenecen al cliente seleccionado 
	 */
	public List<Account> getCurrentAccounts() {
		return currentAccounts;
	}

	/**
	 * Obtiene los movimientos asociados a la cuenta seleccionada
	 * @return Lista con los movimientos asociados a la cuenta seleccionada
	 */
	public List<Movement> getCurrentMovements() {
		return currentMovements;
	}

	/**
	 * Obtiene el cliente que ha sido seleccionado en la lista
	 * @return Cliente seleccionado de la lista
	 */
	public Client getSelectedClient() {
		return selectedClient;
	}

	/**
	 * Modifica el cliente que ha sido seleccionado en la lista
	 * @param selectedClient El nuevo cliente que ha sido seleccionado
	 */
	public void setSelectedClient(Client selectedClient) {
		this.selectedClient = selectedClient;
		listAccounts();
		currentMovements = null;
	}

	/**
	 * Obtiene la cuenta que ha sido seleccionada de la lista
	 * @return Cuenta seleccionada de la lista
	 */
	public Account getSelectedAccount() {
		return selectedAccount;
	}

	/**
	 * Modifica la cuenta que ha sido seleccionada en la lista
	 * @param selectedAccount La nueva cuenta que ha sido seleccionada
	 */
	public void setSelectedAccount(Account selectedAccount) {
		this.selectedAccount = selectedAccount;
		currentMovements = bankService.listAllMovementsByAccount(selectedAccount.getId());
	}

	/**
	 * Determina si se debe mostrar la sección de los reportes
	 * @return True si se desea mostrar la sección de deportes, false de lo contrario
	 */
	public boolean isShowReports() {
		return showReports;
	}
	
	/**
	 * Determina si se desea mostrar el formulario de las cuentas
	 * @return true si se desea mostrar el form, false si se desea mostrar el listado
	 */
	public boolean isShowAccountForm() {
		return showAccountForm;
	}

	/**
	 * Modifica el estado para mostrar el formulario de los clientes
	 * @param showClientForm true si se desea mostrar el formulario, false si se desea mostrar el listado
	 */
	public void setShowClientForm(boolean showClientForm) {
		this.showClientForm = showClientForm;
	}
	
	/**
	 * Establece que se desea mostrar la sección de transacciones
	 */
	public void showClientPanel(){
		this.showReports = false;
		init();
	}
	
	/**
	 * Establece que se desea mostrar la sección de reportes
	 */
	public void showReportPanel(){
		this.showReports = true;
	}
	
	/**
	 * Inicia las variables requeridas cuando se va a mostrar el formulario de clientes
	 */
	public void initFormClient(){
		formClient = new Client();
//		formClient.setId(System.currentTimeMillis());
		displayClientForm();
	}
	
	/**
	 * Inicia las variables requeridas cuando se va a mostrar el formulario de cuentas
	 */
	public void initFormAccount(){
		formAccount = new Account();
		displayAccountForm();
	}

	/**
	 * Obtiene objeto con la información de cliente que será utilizado en el formulario de clientes
	 * @return El cliente a usar en el formulario
	 */
	public Client getFormClient() {
		return formClient;
	}

	/**
	 * Modifica el objeto con la información del cliente que fue utilizado en el formulario de clientes
	 * @param newClient El objeto con la información registrada en el formulario
	 */
	public void setFormClient(Client formClient) {
		this.formClient = formClient;
	}
	
	/**
	 * Obtiene el objeto con la información de la cuenta que será utilizada en el formulario de cuentas
	 * @return La cuenta a usar en el formulario
	 */
	public Account getFormAccount() {
		return formAccount;
	}

	/**
	 * Modifica el objeto con la información de la cuenta que fue utilizado en el formulario de cuentas
	 * @param formAccount El objeto con la información registrada en el formulario
	 */
	public void setFormAccount(Account formAccount) {
		this.formAccount = formAccount;
	}

	/**
	 * Determina si se desea mostrar el formulario de clientes
	 * @return true si se desea mostar el formulario de clientes, false para mostrar el listado de clientes
	 */
	public boolean isShowClientForm() {
		return showClientForm;
	}
	
	/**
	 * Modifica el estado de mostrar el formulario de la cuenta
	 * @param showAccountForm True si se desea mostrar el formulario de cuentas, flase si se desea mostrar el listado
	 */
	public void setShowAccountForm(boolean showAccountForm) {
		this.showAccountForm = showAccountForm;
	}
	
	/**
	 * Establece que se desea mostrar el formulario de clientes
	 */
	public void displayClientForm(){
		this.showClientForm = true;
	}
	
	/**
	 * Establece que se desea mostrar el listado de clientes
	 */
	public void displayClientList(){
		this.showClientForm = false;
	}
	
	/**
	 * Establece que se desea mostrar el formulario de cuentas
	 */
	public void displayAccountForm(){
		this.showAccountForm = true;
	}
	
	/**
	 * Establece que se desea mostrar el listado de cuentas
	 */
	public void displayAccountList(){
		this.showAccountForm = false;
	}
	
	/**
	 * Asigna la fachada de servicios
	 * @param myBankService La fachada de servicios
	 */
	public void setBankService(IBankService bankService) {
		this.bankService = bankService;
	}
	
	/**
	 * Inicia las variables requeridas para el formulario de movimientos
	 */
	public void initMovement(){
		formMovement = new Movement();
	}

	/**
	 * Obtiene el movimiento que es utilizado en el formulario
	 * @return El objeto que será usado en el formulario
	 */
	public Movement getFormMovement() {
		return formMovement;
	}

	/**
	 * Modifica el moviento con la información del formulario
	 * @param formMovement Objeto con la información del formulario
	 */
	public void setFormMovement(Movement formMovement) {
		this.formMovement = formMovement;
	}
}