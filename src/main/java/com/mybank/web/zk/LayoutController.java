//=======================================================================
// ARCHIVO LayoutController.java
// FECHA CREACIÓN: 20/03/2014
//=======================================================================
package com.mybank.web.zk;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Include;

/**
 * Controlador que gestiona las operaciones de la página de transacciones del banco
 * @author Hernán Tenjo
 * @version 1.0
 */
public class LayoutController extends GenericForwardComposer{
	//Atributo requerido para la correcta serialización
	private static final long serialVersionUID = 1L;
	private Include transactionsLayout, reportLayout;
	
	/*
	 * (non-Javadoc)
	 * @see org.zkoss.zk.ui.util.GenericForwardComposer#doAfterCompose(org.zkoss.zk.ui.Component)
	 */
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		transactionsLayout.setVisible(true);
		reportLayout.setVisible(false);
	}
	
	/**
	 * Método que muestra las funcionalidades transaccionales de la aplicación
	 */
	public void onClick$clientLayoutButton(){
		transactionsLayout.setVisible(true);
		reportLayout.setVisible(false);
	}
	
	/**
	 * Método que muestra el reporte disponible en la aplicación
	 */
	public void onClick$reportLayoutButton(){
		transactionsLayout.setVisible(false);
		reportLayout.setVisible(true);
	}
}
