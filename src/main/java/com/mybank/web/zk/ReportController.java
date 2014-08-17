//=======================================================================
// ARCHIVO ReportController.java
// FECHA CREACIÓN: 20/03/2014
//=======================================================================
package com.mybank.web.zk;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

import com.mybank.model.Account;
import com.mybank.model.Client;
import com.mybank.model.Movement;
import com.mybank.service.IBankService;

/**
 * Controlador que gestiona la presentación de reportes del banco
 * @author Hernán Tenjo
 * @version 1.0
 */
public class ReportController extends GenericForwardComposer{
	//Atributo requerido para la correcta serialización
	private static final long serialVersionUID = 1L;
	private static final String SPRING_SERVICE_NAME = "bankService";
	private static final String MOVEMENT_LABEL_PREFIX = "Movimiento ";
	private static final String MOVEMENT_TYPE_CREDIT = "Crédito";
	private static final String MOVEMENT_TYPE_DEBIT = "Débito";
	private IBankService service;
	private Combobox clientComboBox;
	private Datebox startDate, endDate;
	private Grid reportList;
	
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
		List<Client> clients = service.listAllClients();
		ListModelList clientsModel = new ListModelList(clients);
		clientComboBox.setModel(clientsModel);
		clientComboBox.setItemRenderer(new ComboitemRenderer() {
			@Override
			public void render(Comboitem item, Object data) throws Exception {
				Client client = (Client)data;
				item.setLabel(client.getName());
				item.setValue(client);
			}
		});
	}

	/**
	 * Método que construye la información del reporte de transacciones
	 */
	public void onClick$buildReportButton(){
		Date startDateValue = startDate.getValue();
		Date endDateValue = endDate.getValue();
		Client selectedClient = (Client)clientComboBox.getSelectedItem().getValue();
		Map<Account, List<Movement>> reportInfo = service.buildMovementsReport(selectedClient.getId(), startDateValue, endDateValue);
		ListModelList model = new ListModelList();
		
		for(Entry<Account, List<Movement>> entry : reportInfo.entrySet()){
			model.addAll(entry.getValue());			
		}
		
		reportList.setModel(model);
		reportList.setRowRenderer(new RowRenderer() {
			@Override
			public void render(Row row, Object data) throws Exception {
				Movement movement = (Movement)data;
				Account account = movement.getAccount();
				
				Label accountLabel = new Label(account.getNumber());
				Label movementLabel = new Label(MOVEMENT_LABEL_PREFIX + movement.getId());
				Label dateLabel = new Label(movement.getMovDate().toString());
				Label typeLabel = new Label(movement.isCredit() ? MOVEMENT_TYPE_CREDIT : MOVEMENT_TYPE_DEBIT);
				Label valueLabel = new Label(Double.toString(movement.getValue()));
				
				accountLabel.setParent(row);
				movementLabel.setParent(row);
				dateLabel.setParent(row);
				typeLabel.setParent(row);
				valueLabel.setParent(row);
			}
		});
	}
}
