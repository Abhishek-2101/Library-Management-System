package gui_program;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Client_GUI {
	int entered_Option;
	String modify;
	boolean field_try;
	JFrame frame = new JFrame("Book Database");
	JButton insert_Button = new JButton("Insert");
	JButton display_Button = new JButton("Display database");
	JButton ok_Button = new JButton("OK");
	JTextField name = new JTextField(5);
	JTextField author = new JTextField(5);
	JTextField publisher = new JTextField(5);
	JTextField price = new JTextField(5);
	JTextField bid = new JTextField(5);
	JButton exit_button = new JButton("Exit");
	JButton delete_button = new JButton("Delete");
	JLabel acknowlegement_label = new JLabel();
	JButton search_button= new JButton("Search");
	JButton modify_button = new JButton("Modify");
	JLabel name_Label = new JLabel("Enter name");
	JLabel author_Label = new JLabel("Enter Author");
	JLabel publisher_Label = new JLabel("Enter Publisher");
	JLabel price_Label = new JLabel("Enter Price");
	JLabel bid_Label = new JLabel("Enter Book ID");
	JComboBox<String> modify_combo_box = new JComboBox<String>();
	JPanel TextEditPanel = new JPanel();
	JPanel ButtonPanel = new JPanel();
	JPanel LabelPanel = new JPanel();
	JButton cancel_button = new JButton("Cancel");
	JTable table = new JTable() {
		public boolean isCellEditable(int row, int column) {                
            return false;  
		};
	};

	public void prepareGUI() {
		frame.setLayout(new GridLayout(3,1,2,2));
		TextEditPanel.setBounds(200,200, 1000, 1000);
		LabelPanel.setBounds(400, 400, 100, 100);
		ButtonPanel.setBounds(600, 600, 100, 100);
		frame.setBounds(1000, 1000, 1000, 1000);
		name.setBounds(100, 125, 150, 145);
		name.setSize(100, 100);
		TextEditPanel.setLayout(new FlowLayout());
//		model.setBounds(150, 125, 200, 145);
//		company.setBounds(200, 125, 250, 145);
//		cc.setBounds(250, 125, 300, 145);
//		price.setBounds(300, 125, 350, 145);
//		registration.setBounds(350, 125, 400, 145);
//		TextEditPanel.setSize(100, 100);
		frame.add(TextEditPanel);
		frame.add(LabelPanel);
		frame.add(ButtonPanel);
		init_visibility();
		modify_combo_box.addItem("Name");
		modify_combo_box.addItem("Author");
		modify_combo_box.addItem("Publisher");
		modify_combo_box.addItem("Price");
		TextEditPanel.add(name);
		TextEditPanel.add(publisher);
		TextEditPanel.add(author);
		TextEditPanel.add(price);
		TextEditPanel.add(bid);
		TextEditPanel.add(table);
		ButtonPanel.add(insert_Button);
		ButtonPanel.add(display_Button);
		ButtonPanel.add(search_button);
		ButtonPanel.add(modify_button);
		ButtonPanel.add(modify_combo_box);
		ButtonPanel.add(delete_button);
		ButtonPanel.add(cancel_button);
		ButtonPanel.add(exit_button);
		ButtonPanel.add(ok_Button);
		LabelPanel.add(name_Label);
		LabelPanel.add(author_Label);
		LabelPanel.add(publisher_Label);
		LabelPanel.add(price_Label);
		LabelPanel.add(bid_Label);
		LabelPanel.add(acknowlegement_label);
		frame.setVisible(true);
	}

	private void init_visibility() {
		name.setVisible(false);
		publisher.setVisible(false);
		author.setVisible(false);
		price.setVisible(false);
		bid.setVisible(false);
		insert_Button.setVisible(true);
		display_Button.setVisible(true);
		exit_button.setVisible(true);
		delete_button.setVisible(true);
		ok_Button.setVisible(false);
//		table.setVisible(false);
		cancel_button.setVisible(false);
		modify_button.setVisible(true);
		modify_combo_box.setVisible(false);
		acknowlegement_label.setVisible(true);
		name_Label.setVisible(false);
		author_Label.setVisible(false);
		publisher_Label.setVisible(false);
		price_Label.setVisible(false);
		bid_Label.setVisible(false);
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Client_GUI gui_Client = new Client_GUI();
		// TODO Auto-generated method stub
		Socket s = new Socket("localhost", 6060);
		book C = new book();
		gui_Client.prepareGUI();
		gui_Client.exit_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gui_Client.entered_Option = 6;
			}
		});
		gui_Client.ok_Button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					DataOutputStream dout = new DataOutputStream(s.getOutputStream());
					DataInputStream din = new DataInputStream(s.getInputStream());
					switch (gui_Client.entered_Option) {
					case 1: {
						dout.writeUTF("1");
						String book_name = gui_Client.name.getText();
						String book_author = gui_Client.publisher.getText();
						String book_publisher = gui_Client.author.getText();
						String book_price = gui_Client.price.getText();
						String book_bid = gui_Client.bid.getText();
						gui_Client.name.setVisible(false);
						gui_Client.publisher.setVisible(false);
						gui_Client.author.setVisible(false);
						gui_Client.price.setVisible(false);
						gui_Client.bid.setVisible(false);
						gui_Client.name.setText("");
						gui_Client.publisher.setText("");
						gui_Client.author.setText("");
						gui_Client.price.setText("");
						gui_Client.bid.setText("");
						if (C.insert(book_name, book_author, book_publisher, book_price, book_bid)) {
							dout.writeUTF("Add");
							ObjectOutputStream oout = new ObjectOutputStream(s.getOutputStream());
							oout.writeObject(C);
							String temp = din.readUTF();
							if (temp.equals("1")) {
								gui_Client.acknowlegement_label.setText("Successfully added");
							} else {
								gui_Client.acknowlegement_label.setText("Couldn't Add! Book ID is already registered");
							}
						} else {
							dout.writeUTF("Do not add");
						}
					}
					break;
					case 2:
						break;
					case 3: {
						dout.writeUTF("3");
						int id = Integer.parseInt(gui_Client.bid.getText());
						dout.writeInt(id);
						boolean found = din.readBoolean();
						if (found) {
							gui_Client.acknowlegement_label.setText("Found");
							ObjectInputStream oin = new ObjectInputStream(s.getInputStream());
							book C = (book) oin.readObject();
							gui_Client.table.setVisible(true);
							DefaultTableModel table = new DefaultTableModel(new String[] { "", "", "", "", "", "" },0);
							gui_Client.table.setModel(table);
							table.addRow(new Object[] { "Book ID", "Name", "Author", "Publisher", "Price" });
							table.addRow(new Object[] { C.getBid(), C.getName(), C.getAuthor(),
									C.getPublisher() ,C.getPrice() });
						} else {
							gui_Client.acknowlegement_label.setText("No Book Registered with this Book ID");
						}
						break;
					}
					case 4:{
						dout.writeUTF("4");
						int Registration=Integer.parseInt(gui_Client.bid.getText());
						dout.writeInt(Registration);
						boolean registered=din.readBoolean();
						if(!registered) {
							gui_Client.acknowlegement_label.setText("No book registered with this ID");
						}
						else {
							String modify=(String) gui_Client.modify_combo_box.getSelectedItem();
							dout.writeUTF(modify);
							String modification="";
							switch(modify.toLowerCase()){
								case "name":{
								    modification=gui_Client.name.getText();
									dout.writeUTF(modification);
									break;
								}
								case "author":{
									gui_Client.acknowlegement_label.setText("Enter author to be modified");
									modification=gui_Client.name.getText();
									dout.writeUTF(modification);
									break;
								}
								case "publisher":{
									gui_Client.acknowlegement_label.setText("Enter publisher to be modified");
									modification=gui_Client.name.getText();
									dout.writeUTF(modification);
									break;
								}
								case "price":{
									gui_Client.acknowlegement_label.setText("Enter Price to be modified");
									modification=gui_Client.name.getText();
									dout.writeUTF(modification);
									break;
								}
								default:{
									
								}
							}
							boolean field=din.readBoolean();
							if(field) {
								gui_Client.acknowlegement_label.setText("Modified Succesfully");
							}
							else {
								gui_Client.acknowlegement_label.setText("Modification unsuccessful! Check Entered data!");
							}
						}
						break;
					}
					case 5:{
						dout.writeUTF("5");
						int Registration=Integer.parseInt(gui_Client.bid.getText());
						dout.writeInt(Registration);
						if(din.readBoolean()) {
							gui_Client.acknowlegement_label.setText("Deleted Successfully");
						}
						else {
							gui_Client.acknowlegement_label.setText("No book registered with this ID");
						}
						break;
					}
					}
					gui_Client.init_visibility();
				} catch (Exception e) {
					// TODO: handle exception

				}

			}
		});
		
		gui_Client.frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		gui_Client.cancel_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gui_Client.init_visibility();
			}
		});
		gui_Client.insert_Button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gui_Client.ok_Button.setVisible(true);
				gui_Client.cancel_button.setVisible(true);
				gui_Client.name.setVisible(true);
				gui_Client.publisher.setVisible(true);
				gui_Client.author.setVisible(true);
				gui_Client.price.setVisible(true);
				gui_Client.bid.setVisible(true);
				gui_Client.entered_Option = 1;
				gui_Client.name_Label.setVisible(true);
				gui_Client.author_Label.setVisible(true);
				gui_Client.publisher_Label.setVisible(true);
				gui_Client.price_Label.setVisible(true);
				gui_Client.bid_Label.setVisible(true);
			}
		});

		gui_Client.display_Button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gui_Client.table.setVisible(true);
				try {
					DataOutputStream dout = new DataOutputStream(s.getOutputStream());
					dout.writeUTF("2");

					DataInputStream din = new DataInputStream(s.getInputStream());
					ObjectInputStream oin;
					boolean exit = din.readBoolean();
					if (exit) {
						gui_Client.acknowlegement_label.setText("Database is empty");
					} else {
						DefaultTableModel table = new DefaultTableModel(new String[] { "r", "n", "m", "", "" }, 0);
						gui_Client.table.setModel(table);
						table.addRow(new Object[] { "Book Id", "Name", "Author", "Publisher", "Price" });
						while (!exit) {
							oin = new ObjectInputStream(s.getInputStream());
							book C = (book) oin.readObject();
							int i = 1;
							table.addRow(new Object[] { C.getBid(), C.getName(), C.getAuthor(),
									C.getPublisher(), C.getPrice() });
							exit = din.readBoolean();
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});
		gui_Client.exit_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gui_Client.entered_Option=6;
				gui_Client.frame.dispose();
			}
		});

		gui_Client.search_button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gui_Client.entered_Option = 3;
				gui_Client.bid.setVisible(true);
				gui_Client.bid_Label.setVisible(true);
				gui_Client.ok_Button.setVisible(true);
			}
		});
		
		gui_Client.delete_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gui_Client.entered_Option = 5;
				gui_Client.bid.setVisible(true);
				gui_Client.bid_Label.setVisible(true);
				gui_Client.ok_Button.setVisible(true);
			}
		});
		
		gui_Client.modify_button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				gui_Client.entered_Option=4;
				gui_Client.ok_Button.setVisible(true);
				gui_Client.bid.setVisible(true);
				gui_Client.bid_Label.setVisible(true);
				gui_Client.modify_combo_box.setVisible(true);
			}
		});
		
		gui_Client.modify_combo_box.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				gui_Client.name.setVisible(true);
				gui_Client.acknowlegement_label.setText("Enter modification");
			}
		});
	}
}
