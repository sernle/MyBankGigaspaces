//=======================================================================
// ARCHIVO BankController.java
// FECHA CREACIÓN: 14/03/2014
//=======================================================================
package com.mybank.web.zk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.j_spaces.jms.utils.StringsUtils;
import com.mybank.model.Account;
import com.mybank.model.Client;
import com.mybank.model.Movement;
import com.mybank.service.IBankService;

/**
 * Controlador que gestiona las operaciones de la página de transacciones del banco
 * @author Hernán Tenjo
 * @version 1.0
 */
public class BankController extends GenericForwardComposer{
	//Atributo requerido para la correcta serialización
	private static final long serialVersionUID = 1L;
	//Atributos que definen las constantes/labels a trabajar en el controller
	private static final String LABEL_BUTTON_EDIT_CLIENT = "Editar Cliente";
	private static final String LABEL_BUTTON_DELETE_CLIENT = "Eliminar Cliente";
	private static final String LABEL_BUTTON_EDIT_ACCOUNT = "Editar Cuenta";
	private static final String LABEL_BUTTON_DELETE_ACCOUNT = "Eliminar Cuenta";
	private static final String LABEL_BUTTON_MOVEMENT_ACCOUNT = "Movimientos";
	private static final String SPRING_SERVICE_NAME = "bankService";
	private static final String EVENT_BUTTON_CLICK = "onClick";
	
	private IBankService service;
	private List<Client> clients;
	private Listbox clientList, accountList;
	private Groupbox clientForm, accountForm;
	private Textbox clientName, clientAddress, clientPhone, accountNumber;
	private Doublebox accountBalance;
	private boolean editClientMode, editAccountMode;
	private Client selectedClient;
	private Account selectedAccount;
	
	private Window movementWindow;
	private Doublebox movementWindow$movementValue;
	private Combobox movementWindow$movementType;
	
	/*
	 * (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.GenericComposer#doBeforeComposeChildren(org.zkoss.zk.ui.Component)
	 */
	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		super.doBeforeComposeChildren(comp);
		service = (IBankService)SpringUtil.getBean(SPRING_SERVICE_NAME);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.GenericForwardComposer#doAfterCompose(org.zkoss.zk.ui.Component)
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		initElements();
		movementWindow.setVisible(false);
	}
	
	/**
	 * Método que asigna los valores iniciales a la vista
	 */
	private void initElements(){
		buildClientsModel();
		buildAccountsModel();
		clientList.setVisible(true);
		clientForm.setVisible(false);
		accountList.setVisible(true);
		accountForm.setVisible(false);
	}
	
	/**
	 * Método que gestiona las acciones requeridas para mostrar el formulario de la información del cliente
	 */
	public void onClick$addClientButton(){
		editClientMode = false;
		clientList.setVisible(false);
		clientForm.setVisible(true);
		
		clientName.setValue(StringsUtils.EMPTY);
		clientAddress.setValue(StringsUtils.EMPTY);
		clientPhone.setValue(StringsUtils.EMPTY);
	}
	
	/**
	 * Método que persiste la información de un cliente
	 */
	public void onClick$saveClientFormButton(){
		clientList.setVisible(true);
		clientForm.setVisible(false);
		
		Client client = new Client();
		client.setName(clientName.getValue());
		client.setAddress(clientAddress.getValue());
		client.setPhoneNumber(clientPhone.getValue());
		
		if(editClientMode){
			client.setId(selectedClient.getId());
		}
		
		service.saveClient(client);
		initElements();
	}
	
	/**
	 * Método que gestiona las acciones requeridas al cancelar la visualización del formulario del cliente
	 */
	public void onClick$cancelClientFormButton(){
		clientList.setVisible(true);
		clientForm.setVisible(false);
		editClientMode = false;
	}
	
	/**
	 * Método ejecutado al seleccionar un cliente en el listado, el cual debe actualizar la info del listado de cuentas asociadas
	 */
	public void onSelect$clientList(){
		Listitem item = clientList.getSelectedItem();
		selectedClient = (Client)item.getValue();
		buildAccountsModel();
	}
	
	/**
	 * Método que construye el listener encargado editar la información del cliente seleccionado
	 */
	private EventListener getEditClientListener(){
		return new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				editClientMode = true;
				Listcell selectedCell = (Listcell)event.getTarget().getParent();
				Listitem selectedItem = (Listitem)selectedCell.getParent();
				clientList.setSelectedItem(selectedItem);
				onSelect$clientList();
				clientList.setVisible(false);
				clientForm.setVisible(true);
				
				clientName.setValue(selectedClient.getName());
				clientAddress.setValue(selectedClient.getAddress());
				clientPhone.setValue(selectedClient.getPhoneNumber());
			}
		};
	}
	
	/**
	 * Método que construye el listener encargado de eliminar al cliente seleccionado del listado
	 */
	private EventListener getDeleteClientListener(){
		return new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Listcell selectedCell = (Listcell)event.getTarget().getParent();
				Listitem selectedItem = (Listitem)selectedCell.getParent();
				Client clientToDelete = (Client)selectedItem.getValue();
				service.deleteClient(clientToDelete.getId());
				initElements();
			}
		};
		
	}

	/**
	 * Método que construye el modelo requerido para la tabla de clientes
	 */
	private void buildClientsModel(){
		clients = service.listAllClients();
		ListModelList clientsModel = new ListModelList(clients);
		clientList.setModel(clientsModel);
		
		if(clients.size() > 0){
			selectedClient = clients.get(0);
			clientList.setSelectedIndex(0);
		}
		
		clientList.setItemRenderer(new ListitemRenderer() {
			@Override
			public void render(Listitem item, Object clientParameter) throws Exception {
				Client client = (Client)clientParameter;
				Listcell nameCell = new Listcell(client.getName());
				Listcell addressCell = new Listcell(client.getAddress());
				Listcell phoneCell = new Listcell(client.getPhoneNumber());
				Listcell optionsCell = new Listcell();
				
				Button editButton = new Button(LABEL_BUTTON_EDIT_CLIENT);
				Button deleteButton = new Button(LABEL_BUTTON_DELETE_CLIENT);
				editButton.addEventListener(EVENT_BUTTON_CLICK, getEditClientListener());
				deleteButton.addEventListener(EVENT_BUTTON_CLICK, getDeleteClientListener());
				editButton.setParent(optionsCell);
				deleteButton.setParent(optionsCell);
				
				nameCell.setParent(item);
				addressCell.setParent(item);
				phoneCell.setParent(item);
				optionsCell.setParent(item);
				item.setValue(client);
			}
		});
	}
	
	/**
	 * Método que gestiona las acciones requeridas para mostrar el formulario de la información de la cuenta
	 */
	public void onClick$addAccountButton(){
		editAccountMode = false;
		accountList.setVisible(false);
		accountForm.setVisible(true);
		
		accountNumber.setValue(StringsUtils.EMPTY);
		accountBalance.setValue(0);
	}
	
	/**
	 * Método que persiste la información de una cuenta
	 */
	public void onClick$saveAccountFormButton(){
		accountList.setVisible(true);
		accountForm.setVisible(false);
		Account account = new Account();
		account.setBalance(accountBalance.getValue());
		account.setNumber(accountNumber.getValue());
		account.setClient(selectedClient);
		
		if(editAccountMode){
			account.setId(selectedAccount.getId());
		}
		
		service.saveAccount(account);
		buildAccountsModel();
		accountList.setVisible(true);
		accountForm.setVisible(false);
	}
	
	/**
	 * Método que gestiona las acciones requeridas al cancelar la visualización del formulario de la cuenta
	 */
	public void onClick$cancelAccountFormButton(){
		accountList.setVisible(true);
		accountForm.setVisible(false);
		editAccountMode = false;
	}
	
	/**
	 * Método ejecutado al seleccionar una cuenta en el listado
	 */
	public void onSelect$accountList(){
		Listitem item = accountList.getSelectedItem();
		selectedAccount = (Account)item.getValue();
	}
	
	/**
	 * Método que permite editar la información de la cuenta seleccionada
	 */
	private EventListener getEditAccountListener(){
		return new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				editAccountMode = true;
				Listcell selectedCell = (Listcell)event.getTarget().getParent();
				Listitem selectedItem = (Listitem)selectedCell.getParent();
				accountList.setSelectedItem(selectedItem);
				onSelect$accountList();
				accountList.setVisible(false);
				accountForm.setVisible(true);
				
				accountBalance.setValue(selectedAccount.getBalance());
				accountNumber.setValue(selectedAccount.getNumber());				
			}
		};
	}
	
	/**
	 * Método que elimina la cuenta seleccionada del listado
	 */
	private EventListener getDeleteAccountListener(){
		return new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Listcell selectedCell = (Listcell)event.getTarget().getParent();
				Listitem selectedItem = (Listitem)selectedCell.getParent();
				Account accountToDelete = (Account)selectedItem.getValue();
				service.deleteAccount(accountToDelete.getId());
				buildAccountsModel();				
			}
		};
	}
	
	/**
	 * Método que construye la tabla del lista de cuentas asociadas a un cliente
	 * @param selectedClient Cliente que ha sido seleccionado
	 */
	private void buildAccountsModel(){
		List<Account> accounts;
		
		if(selectedClient != null){
			accounts = service.listAllAccountsByClient(selectedClient.getId());
		}else{
			accounts = new ArrayList<Account>();
		}
		
		ListModelList accountsModel = new ListModelList(accounts);
		accountList.setModel(accountsModel);
		accountList.setItemRenderer(new ListitemRenderer() {
			@Override
			public void render(Listitem item, Object accountParameter) throws Exception {
				Account account = (Account)accountParameter;
				Listcell accountCell = new Listcell();
				Listcell balanceCell = new Listcell();
				Listcell optionsCell = new Listcell();
				
				Label accountNumber = new Label(account.getNumber());
				Label accountBalance = new Label(Double.toString(account.getBalance()));
				Button editButton = new Button(LABEL_BUTTON_EDIT_ACCOUNT);
				Button deleteButton = new Button(LABEL_BUTTON_DELETE_ACCOUNT);
				Button movementButton = new Button(LABEL_BUTTON_MOVEMENT_ACCOUNT);
				editButton.addEventListener(EVENT_BUTTON_CLICK, getEditAccountListener());
				deleteButton.addEventListener(EVENT_BUTTON_CLICK, getDeleteAccountListener());
				movementButton.addEventListener(EVENT_BUTTON_CLICK, getMovementTransactionListener());
				
				accountNumber.setParent(accountCell);
				accountBalance.setParent(balanceCell);
				editButton.setParent(optionsCell);
				deleteButton.setParent(optionsCell);
				movementButton.setParent(optionsCell);
				
				accountCell.setParent(item);
				balanceCell.setParent(item);
				optionsCell.setParent(item);
				item.setValue(account);
			}
		});
	}
	
	/**
	 * Método que muestra la ventana para el registro de movimientos en una cuenta
	 * @param event Evento generado al seleccionar el botón de movimientos
	 */
	private EventListener getMovementTransactionListener(){
		return new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Listcell selectedCell = (Listcell)event.getTarget().getParent();
				Listitem selectedItem = (Listitem)selectedCell.getParent();
				accountList.setSelectedItem(selectedItem);
				selectedAccount = (Account)selectedItem.getValue();
				movementWindow.setVisible(true);
				movementWindow$movementValue.setValue(0);
				movementWindow$movementType.setSelectedIndex(0);				
			}
		};
	}
	
	/**
	 * Método que persiste un movimiento en la cuenta seleccionada
	 * @throws InterruptedException
	 */
	public void onClick$saveMovementFormButton$movementWindow() throws InterruptedException{
		try{
			double transactionValue = movementWindow$movementValue.getValue();
			boolean credit = Boolean.parseBoolean(movementWindow$movementType.getSelectedItem().getValue().toString());
			Movement movement = new Movement();
			movement.setCredit(credit);
			movement.setMovDate(new Date());
			movement.setValue(transactionValue);
			movement.setAccount(selectedAccount);
			service.saveMovement(movement);
			buildAccountsModel();
			movementWindow.setVisible(false);
		}catch(IllegalStateException e){
			Messagebox.show(e.getMessage());
		}
	}
	
	/**
	 * Método que cancela la generación de un movimiento en la cuenta
	 */
	public void onClick$cancelMovementFormButton$movementWindow(){
		movementWindow.setVisible(false);
	}
}
