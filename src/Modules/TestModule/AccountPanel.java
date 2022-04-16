package Modules.TestModule;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

@SuppressWarnings("serial")
public class AccountPanel extends JPanel
{

	
	/**
	 * Create the panel.
	 */
	public AccountPanel(String username, String status)
	{
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		
		JLabel usernameLabelLabel = new JLabel("Name:");
		add(usernameLabelLabel);
		
		JLabel usernameLabel = new JLabel(username);
		springLayout.putConstraint(SpringLayout.NORTH, usernameLabel, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.NORTH, usernameLabelLabel, 0, SpringLayout.NORTH, usernameLabel);
		add(usernameLabel);
		
		JLabel statusLabelLabel = new JLabel("Status:");
		springLayout.putConstraint(SpringLayout.NORTH, statusLabelLabel, 6, SpringLayout.SOUTH, usernameLabelLabel);
		springLayout.putConstraint(SpringLayout.WEST, statusLabelLabel, 10, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.WEST, usernameLabelLabel, 0, SpringLayout.WEST, statusLabelLabel);
		add(statusLabelLabel);
		
		JLabel StatusLabel = new JLabel(status);
		springLayout.putConstraint(SpringLayout.WEST, StatusLabel, 12, SpringLayout.EAST, statusLabelLabel);
		springLayout.putConstraint(SpringLayout.EAST, usernameLabel, 0, SpringLayout.EAST, StatusLabel);
		springLayout.putConstraint(SpringLayout.NORTH, StatusLabel, 0, SpringLayout.NORTH, statusLabelLabel);
		add(StatusLabel);
		
		
	}
}
