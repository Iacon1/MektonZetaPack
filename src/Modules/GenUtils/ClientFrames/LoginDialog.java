package Modules.GenUtils.ClientFrames;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import Client.GameClientThread;
import Modules.GenUtils.PacketTypes.LoginPacket;
import Utils.Logging;
import Utils.MiscUtils;

import javax.swing.SpringLayout;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Color;

@SuppressWarnings("serial")
public class LoginDialog extends JDialog
{

	private final JPanel contentPanel = new JPanel();
	private final JCheckBox newUserBox = new JCheckBox("New user");
	private final JTextField usernameBox = new JTextField();
	private final JPasswordField passwordBox = new JPasswordField();
	private final JLabel usernameLabel = new JLabel("Username");
	private final JLabel passwordLabel = new JLabel("Password");
	private final JLabel serverLabel = new JLabel("Login to ");

	private boolean send = false;
	private final JLabel errorLabel = new JLabel("");

	public LoginPacket getPacket()
	{
		if (!send) return null;
		
		LoginPacket packet = new LoginPacket();
		packet.username = usernameBox.getText();
		packet.myPassword = new String(passwordBox.getPassword()); // TODO is this the most secure way?
		packet.newUser = newUserBox.isSelected();
		
		send = false;
		
		return packet;
	}
	public void onFail(String reason)
	{
		usernameBox.setText(null);
		passwordBox.setText(null);
		
		errorLabel.setText(reason);
		
		send = false;
	}
	private void onClickOK() // When OK is pressed
	{
		if (usernameBox.getText() != null && passwordBox.getPassword().length != 0) send = true;
	}
	
	public static void main(GameClientThread thread)
	{
		try
		{
			LoginDialog dialog = new LoginDialog();
			thread.setContainer("login", dialog);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {Logging.logException(e);}
	}

	
	public LoginDialog()
	{
		passwordBox.setColumns(10);
		usernameBox.setColumns(10);
		setIconImages(MiscUtils.getIcons(MiscUtils.ExecType.client));
		
		setTitle(MiscUtils.getProgramName() + " Client: Login");
		setBounds(100, 100, 400, 300);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, errorLabel, 6, SpringLayout.SOUTH, newUserBox);
		sl_contentPanel.putConstraint(SpringLayout.EAST, errorLabel, -10, SpringLayout.EAST, newUserBox);
		
		sl_contentPanel.putConstraint(SpringLayout.HORIZONTAL_CENTER, serverLabel, 0, SpringLayout.HORIZONTAL_CENTER, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.VERTICAL_CENTER, serverLabel, -40, SpringLayout.VERTICAL_CENTER, contentPanel);
		
		sl_contentPanel.putConstraint(SpringLayout.HORIZONTAL_CENTER, usernameBox, 0, SpringLayout.HORIZONTAL_CENTER, serverLabel);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, usernameBox, 10, SpringLayout.SOUTH, serverLabel);
		
		sl_contentPanel.putConstraint(SpringLayout.EAST, passwordBox, 0, SpringLayout.EAST, usernameBox);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, passwordBox, 10, SpringLayout.SOUTH, usernameBox);	

		sl_contentPanel.putConstraint(SpringLayout.WEST, newUserBox, 0, SpringLayout.WEST, passwordBox);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, newUserBox, 10, SpringLayout.SOUTH, passwordBox);
		
		sl_contentPanel.putConstraint(SpringLayout.VERTICAL_CENTER, usernameLabel, 0, SpringLayout.VERTICAL_CENTER, usernameBox);
		sl_contentPanel.putConstraint(SpringLayout.EAST, usernameLabel, -20, SpringLayout.WEST, usernameBox);
		
		sl_contentPanel.putConstraint(SpringLayout.VERTICAL_CENTER, passwordLabel, 0, SpringLayout.VERTICAL_CENTER, passwordBox);
		sl_contentPanel.putConstraint(SpringLayout.EAST, passwordLabel, -20, SpringLayout.WEST, passwordBox);
		
		contentPanel.setLayout(sl_contentPanel);
		
		contentPanel.add(newUserBox);
		
		contentPanel.add(usernameBox);
		
		contentPanel.add(passwordBox);
		
		contentPanel.add(usernameLabel);
		
		contentPanel.add(passwordLabel);
		
		contentPanel.add(serverLabel);
		errorLabel.setForeground(Color.RED);
		
		contentPanel.add(errorLabel);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton okButton = new JButton("Login / Sign Up");
		okButton.setActionCommand("Login / Sign Up");
		okButton.addActionListener(e -> {onClickOK();});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(e -> {this.setVisible(false); this.dispose();});
		buttonPane.add(cancelButton);
	}
}
