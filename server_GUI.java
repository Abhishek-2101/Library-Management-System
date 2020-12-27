package gui_program;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import java.sql.*;

public class server_GUI implements Runnable{
	
	Socket s;

	server_GUI(Socket s) {
		this.s = s;
	}
	@SuppressWarnings("resource")
	public void run() {
		try {

			DataOutputStream dout = null;

			ObjectInputStream oin = null;
			ObjectOutputStream oout = null;
			book b = null;

			Integer option = null;
			Statement st = null;
			Connection con = null;

			do {
				DataInputStream din = null;
				din = new DataInputStream(s.getInputStream());
				option = Integer.parseInt(din.readUTF());
				Class.forName("com.mysql.jdbc.Driver");
				String SQL;
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/books?autoReconnect=true&useSSL=false",
						"root", "abhishek");
				st = con.createStatement();
//				st.executeUpdate(
//						"create table if not exists book(Bid int,Name varchar(30),Author varchar(30),Publisher varchar(30),Price int,primary key(bid));");
				switch (option) {
				case 1: {
					String temp = null;
					temp = din.readUTF();
					if (temp.contentEquals("Add")) {
						oin = new ObjectInputStream(s.getInputStream());
						b = (book) oin.readObject();
						dout = new DataOutputStream(s.getOutputStream());
						SQL = "insert into book(Bid, Name ,Author ,Publisher,Price) values(?,?,?,?,?)";
						PreparedStatement pst=con.prepareStatement(SQL);
						pst.setInt(1,b.getBid());
						pst.setString(2,b.getName());
						pst.setString(3, b.getAuthor());
						pst.setString(4,b.getPublisher());
				 		pst.setInt(5,(int) b.getPrice());
				 	
						try {
							pst.executeUpdate();
							dout.writeUTF("1");
						}
						catch(Exception e) {
							dout.writeUTF("0");
						}

						
					}
					break;
				}
				case 2: {
					st = con.createStatement();
					ResultSet rs = null;
					rs = st.executeQuery("select * from book;");
					boolean exit = false;
					while (rs.next()) {
						dout = new DataOutputStream(s.getOutputStream());
						dout.writeBoolean(false);
						b=new book(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getLong(5));
						oout = new ObjectOutputStream(s.getOutputStream());
						oout.writeObject(b);
					}
					dout = new DataOutputStream(s.getOutputStream());
					dout.writeBoolean(true);
					break; 
				}
				case 3: {
					din = new DataInputStream(s.getInputStream());
					int id = 0;
					id = din.readInt();
					dout = new DataOutputStream(s.getOutputStream());
					st = con.createStatement();
					ResultSet rs = null;
					rs = st.executeQuery("select * from book where bid=" + id);
					if (rs.next()) {
						dout.writeBoolean(true);
						oout = new ObjectOutputStream(s.getOutputStream());
						b=new book(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getLong(5));
						oout.writeObject(b);
					} else {
						dout.writeBoolean(false);
					}
					break;
				}
				case 4: {
					dout = new DataOutputStream(s.getOutputStream());
					int id = 0;
					id = din.readInt();
					st = con.createStatement();
					boolean found = false;
					ResultSet rs = null;
					rs = st.executeQuery("select * from book");
					while (rs.next()) {
						if (id == rs.getInt("bid")) {
							found = true;
							break;
						}
					}
					dout.writeBoolean(found);
					if (found) {
						String field = null;
						field = din.readUTF();
						field = field.toLowerCase();
						switch (field) {
						case "name": {
							st = con.createStatement();
							st.execute("update book set Name='" + din.readUTF() + "' where bid="
									+ id);
							dout.writeBoolean(true);
							break;
						}
						case "author": {
							st = con.createStatement();
							st.execute("update book set author='" + din.readUTF() + "' where bid="
									+ id);
							dout.writeBoolean(true);
							break;
						}
						case "publisher": {
							st = con.createStatement();
							st.execute("update book set publisher='" + din.readUTF() + "' where bid="
									+ id);
							dout.writeBoolean(true);
							break;
						}

						case "price": {
							st = con.createStatement();
							st.execute("update book set price='" + din.readUTF() + "' where bid="
									+ id);
							dout.writeBoolean(true);
							break;
						}
						default: {
							dout.writeBoolean(false);
							break;
						}
						}

						}
					}
					break;
				
				case 5: {
					int ID = 0;
					ID = din.readInt();
					st = con.createStatement();
					ResultSet rs = null;
					rs = st.executeQuery("select * from book where bid=" + ID);
					boolean found = false;
					if (rs.next()) {
						found = true;
					}
					if (found) {
						st.execute("delete from book where bid=" + ID);
						dout = new DataOutputStream(s.getOutputStream());
						dout.writeBoolean(found);
					}
					else {
						dout.writeBoolean(found);
					}
					break;
				}
				case 6: {
				}

				}

			} while (option != 6);
		} catch (Exception e) {
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		// TODO Auto-generated method stub
		ServerSocket ss = new ServerSocket(6060);

		while (true) {
			Socket s = ss.accept();
			new Thread(new server_GUI(s)).start();
		}
	}

}

class book implements Serializable {
	String name,author,publisher;
	int bid;
	long price;
	
	public book(int Bid, String Name, String Author, String Publisher, long Price) {
		// TODO Auto-generated constructor stub
		super();
		bid=Bid;
		name=Name;
		author=Author;
		publisher=Publisher;
		price=Price;
				
	}

	public book() {

	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public boolean insert(String name,String Author,String Publisher,String price,String Bid) {
		try {
			setName(name);
			setAuthor(Author);
			setPublisher(Publisher);
			setPrice(Long.parseLong(price));
			setBid(Integer.parseInt(Bid));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public void display() {
		System.out.println("Book Id:"+bid+"\tName:"+name+"\t\tAuthor:"+author+"\tPublisher:"+publisher+"\tPrice:"+price);
	}
}
