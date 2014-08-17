//=======================================================================
// ARCHIVO ReportBean.java
// FECHA CREACIÓN: 15/02/2014
//=======================================================================
package com.mybank.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.commons.lang.StringUtils;

import com.mybank.model.Account;
import com.mybank.model.Client;
import com.mybank.model.Movement;
import com.mybank.service.IBankService;

/**
 * Bean que gestiona las operaciones de la página de reportes del banco
 * @author Hernán Tenjo
 * @version 1.0
 */
@ManagedBean
@ViewScoped
public class ReportBean implements Serializable{
	private static final long serialVersionUID = 1L;
	//Fachada de servicios del banco
    @ManagedProperty(value="#{bankService}")
    private IBankService bankService;
    //Atributos que permiten gestionar los componentes de la página
    private Client selectedClient;
    private Date startDate;
    private Date endDate;
    private Map<Account, List<Movement>> reportInfo;
    
    /**
     * Obtiene los clientes con un nombre que contiene el patron dado
     * @param query Patron por el que se desea filtrar los clientes
     * @return Los clientes que contienen el filtro dado en el nombre
     */
    public List<Client> filterClients(String query){
    	List<Client> clientsWithName = bankService.listAllClientsByName(query);
    	return clientsWithName;
    }
    
    /**
     * Construye la información para la generación del reporte
     */
    public void buildReport(){
    	reportInfo = bankService.buildMovementsReport(selectedClient.getId(), startDate, endDate);
    }
    
	/**
	 * Obtiene el cliente seleccionado para la generación del reporte
	 * @return El cliente por el que se generará el reporte
	 */
	public Client getSelectedClient() {
		return selectedClient;
	}

	/**
	 * Modifica el cliente seleccionado para la generación del reporte
	 * @param selectedClient El nuevo cliente que fue seleccionado para generar el reporte
	 */
	public void setSelectedClient(Client selectedClient) {
		this.selectedClient = selectedClient;
	}
	
	/**
	 * Asigna la fachada de servicios
	 * @param myBankService La fachada de servicios
	 */
	public void setBankService(IBankService bankService) {
		this.bankService = bankService;
	}

	/**
	 * Obtiene la fecha inicial para la generación del reporte
	 * @return Fecha inicial para el registro de transaccioness
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Modifica la fecha inicial para la generación del reporte
	 * @param startDate Fecha límite para el registro de transacciones
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Obtiene la fecha final para la generación del reporte
	 * @return Fecha límite para el registro de transacciones
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Modifica la fecha final para la generación del reporte
	 * @param endDate Fecha límite para el registro de transacciones
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Obtiene la información requerida para la generación del reporte
	 * @return Estructura que contiene la información para generar el reporte
	 */
	public Map<Account, List<Movement>> getReportInfo() {
		return reportInfo;
	}

	/**
	 * Modifica la información que se utiliza para la generación del reporte
	 * @param reportInfo La nueva información que será utilizada para generar el reporte
	 */
	public void setReportInfo(Map<Account, List<Movement>> reportInfo) {
		this.reportInfo = reportInfo;
	}

	/**
	 * Obtiene las cuentas que sirven de llave para la tabla principal del reporte
	 * @return Lista con las cuentas que hacen parte del reporte
	 */
	public List<Account> getReportInfoAccounts(){
		if(reportInfo != null){
			return new ArrayList<Account>(reportInfo.keySet());
		}else{
			return null;
		}
	}
	
	/**
	 * Metodo que construye el convertidos requerido del cliente para poder utilizar el autocomplete
	 * @return Objeto que permite convertir un Cliente 
	 */
	public Converter getClientConverter(){
		return new Converter(){
			/*
			 * (non-Javadoc)
			 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
			 */
			@Override
			public String getAsString(FacesContext context, UIComponent component, Object value) {
				if (value == null) {  
		            return null;  
		        } else {  
		            return String.valueOf(((Client)value).getId());  
		        }  
			}
			
			/*
			 * (non-Javadoc)
			 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.String)
			 */
			@Override
			public Object getAsObject(FacesContext context, UIComponent component, String clientId) {
				if (StringUtils.isBlank(clientId)) {  
		            return null;  
		        } else {
		        	return bankService.findClient(clientId);
		        }  
			}
		};
	}
}