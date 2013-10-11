package com.eviware.soapui.impl.rest.panels.request;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.impl.rest.RestRequest;
import com.eviware.soapui.impl.rest.RestRequestInterface;
import com.eviware.soapui.impl.rest.actions.support.NewRestResourceActionBase;
import com.eviware.soapui.impl.rest.panels.resource.RestParamsTable;
import com.eviware.soapui.impl.rest.panels.resource.RestParamsTableModel;
import com.eviware.soapui.impl.rest.support.RestUtils;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A component that displays matrix and query string parameters for a REST request and provides a popup to edit them.
 */
class ParametersField extends JPanel
{

	private final RestRequestInterface request;
	private final JLabel textLabel;
	private final JTextField textField;
	private RestParamsTable paramsTable;
	private JPopupMenu popup;

	ParametersField( RestRequestInterface request )
	{
		this.request = request;
		textLabel = new JLabel( "Parameters" );
		String paramsString = RestUtils.makeSuffixParameterString( request );
		textField = new JTextField( paramsString);
		setToolTipText( paramsString );
		super.setLayout( new BorderLayout() );
		super.add( textLabel, BorderLayout.NORTH );
		super.add( textField, BorderLayout.SOUTH );
		addListeners();

	}

	private void addListeners()
	{
		textField.addCaretListener( new CaretListener()
		{

			@Override
			public void caretUpdate( CaretEvent e )
			{
				ParameterFinder finder = new ParameterFinder(textField.getText());
				openPopup( finder.findParameterAt( e.getDot() ) );
			}



		} );
	}

	public String getText()
	{
		return textField.getText();
	}

	public void setText( String text )
	{
		textField.setText( text );
		setToolTipText( text );
	}

	@Override
	public void setToolTipText( String text )
	{
		super.setToolTipText( text );
		textLabel.setToolTipText( text );
		textField.setToolTipText( text );
	}

	private void openPopup( String selectedParameter )
	{
		paramsTable = new RestParamsTable( request.getParams(), false, new RestParamsTableModel( request.getParams(), RestParamsTableModel.Mode.MINIMAL ),
				NewRestResourceActionBase.ParamLocation.METHOD, true, true );
		paramsTable.focusParameter(selectedParameter);
		popup = new JPopupMenu();
		popup.setLayout( new BorderLayout() );
		popup.add( paramsTable, BorderLayout.CENTER );
		popup.setInvoker( textField );
		//TODO: We have to choose the parent component as destination to get the setLocation work properly
		popup.setLocation( SwingUtilities.convertPoint( textField, 3, getHeight() + 2, SoapUI.getFrame() ) );
		popup.setVisible( true );
	}

	public void closePopup()
	{
		if( popup != null )
		{
			popup.setVisible( false );
			popup = null;
			paramsTable = null;
		}
	}


}