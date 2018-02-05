
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.border.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import java.sql.*;

@SuppressWarnings("serial")
public class JDBCMainWindowContent extends JInternalFrame implements ActionListener {
	// DB Connectivity Attributes
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;

	private Container content;

	private JPanel detailsPanel;
	private JPanel exportButtonPanel;
	private JPanel exportConceptDataPanel;
	private JPanel otherTableUpdate;
	private JScrollPane dbContentsPanel;

	private Border lineBorder;

	private JLabel idLabel = new JLabel("id:      ");
	private JLabel genderLabel = new JLabel("gender:                 ");
	private JLabel wantedFeatureLabel = new JLabel("wanted feature:               ");
	private JLabel ratingLabel = new JLabel("rating:      ");

	private JTextField id = new JTextField(5);
	private JTextField gender = new JTextField(10);
	private JTextField wantedFeature = new JTextField(5);
	private JTextField rating = new JTextField(5);

	private JLabel idDevicesLabel = new JLabel("id:      ");
	private JLabel typeLabel = new JLabel("type:                 ");
	private JLabel featureLabel = new JLabel("feature:               ");
	private JLabel ratingDevicesLabel = new JLabel("rating:      ");

	private JTextField idDevices = new JTextField(5);
	private JTextField type = new JTextField(20);
	private JTextField feature = new JTextField(20);
	private JTextField ratingDevices = new JTextField(5);

	private String message = null;
	private JLabel messageDisplay = new JLabel(message);

	private static QueryTableModel TableModel = new QueryTableModel();

	// Add the models to JTabels
	private JTable TableofDBContents = new JTable(TableModel);

	// Buttons for inserting, and updating members
	// also a clear button to clear details panel
	private JButton updateButton = new JButton("Update");
	private JButton insertButton = new JButton("Insert");
	private JButton exportButton = new JButton("Export");
	private JButton deleteButton = new JButton("Delete");
	private JButton clearButton = new JButton("Clear");

	private JButton updateDevicesButton = new JButton("Update Devices");
	private JButton insertDevicesButton = new JButton("Insert Devices");
	private JButton deleteDevicesButton = new JButton("Delete from Devices");

	private JButton featuresSelection = new JButton("Features rating by gender ");
	private JTextField featuresSelectionTF = new JTextField(10);
	private JButton bestPhoneSelection = new JButton("Find best phone by feature ");
	private JTextField bestPhoneSelectionTF = new JTextField(10);
	private JButton deviceTableExport = new JButton("Export device table");
	private JButton salesRating = new JButton("View sales rating");
	private JTextField salesRatingTF = new JTextField(10);
	private JButton exportSales = new JButton("Export sales table");
	private JButton buy = new JButton("Buy");
	private JLabel deviceLabel = new JLabel("name:      ");
	private JTextField deviceSelection = new JTextField(20);
	private JLabel quantityLabel = new JLabel("quantity:                 ");
	private JTextField quantity = new JTextField(3);
	private String purchaseConfirmation;

	public JDBCMainWindowContent(String aTitle) {
		// setting up the GUI
		super(aTitle, false, false, false, false);
		setEnabled(true);

		initiate_db_conn();
		// add the 'main' panel to the Internal Frame
		content = getContentPane();
		content.setLayout(null);
		content.setBackground(Color.yellow);
		lineBorder = BorderFactory.createEtchedBorder(15, Color.black, Color.black);

		// setup details panel and add the components to it
		detailsPanel = new JPanel();
		detailsPanel.setLayout(new GridLayout(8, 2));
		detailsPanel.setSize(460, 300);
		detailsPanel.setLocation(3, 0);
		detailsPanel.setBackground(Color.orange);
		detailsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "User preferences table update"));

		detailsPanel.add(idLabel);
		detailsPanel.add(id);
		detailsPanel.add(genderLabel);
		detailsPanel.add(gender);
		detailsPanel.add(wantedFeatureLabel);
		detailsPanel.add(wantedFeature);
		detailsPanel.add(ratingLabel);
		detailsPanel.add(rating);

		insertButton.setSize(100, 30);
		updateButton.setSize(100, 30);
		exportButton.setSize(100, 30);
		deleteButton.setSize(100, 30);
		clearButton.setSize(100, 30);

		detailsPanel.add(insertButton);
		detailsPanel.add(updateButton);
		detailsPanel.add(exportButton);
		detailsPanel.add(deleteButton);
		detailsPanel.add(clearButton);

		content.add(detailsPanel);
		//

		// setup details panel and add the components to it
		exportButtonPanel = new JPanel();
		exportButtonPanel.setLayout(new GridLayout(4, 2));
		exportButtonPanel.setBackground(Color.orange);
		exportButtonPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Data analysis"));
		exportButtonPanel.add(featuresSelection);
		exportButtonPanel.add(featuresSelectionTF);
		exportButtonPanel.add(bestPhoneSelection);
		exportButtonPanel.add(bestPhoneSelectionTF);
		exportButtonPanel.add(deviceTableExport);
		exportButtonPanel.add(exportSales);
		exportButtonPanel.add(salesRating);
		exportButtonPanel.add(salesRatingTF);
		exportButtonPanel.setSize(460, 200);
		exportButtonPanel.setLocation(3, 300);
		content.add(exportButtonPanel);

		insertButton.addActionListener(this);
		updateButton.addActionListener(this);
		exportButton.addActionListener(this);
		deleteButton.addActionListener(this);
		clearButton.addActionListener(this);
		featuresSelection.addActionListener(this);
		bestPhoneSelection.addActionListener(this);
		deviceTableExport.addActionListener(this);
		salesRating.addActionListener(this);
		updateDevicesButton.addActionListener(this);
		insertDevicesButton.addActionListener(this);
		deleteDevicesButton.addActionListener(this);
		buy.addActionListener(this);
		exportSales.addActionListener(this);

		TableofDBContents.setPreferredScrollableViewportSize(new Dimension(900, 300));

		dbContentsPanel = new JScrollPane(TableofDBContents, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dbContentsPanel.setBackground(Color.orange);
		dbContentsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "User preferences content"));
		dbContentsPanel.setSize(695, 250);
		dbContentsPanel.setLocation(477, 0);
		content.add(dbContentsPanel);

		exportConceptDataPanel = new JPanel();
		exportConceptDataPanel.setBackground(Color.orange);
		exportConceptDataPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Result of phone search"));
		exportConceptDataPanel.setSize(300, 250);
		exportConceptDataPanel.setLocation(477, 250);
		exportConceptDataPanel.setLayout(new GridLayout(6, 4));
		exportConceptDataPanel.add(messageDisplay);
		exportConceptDataPanel.add(deviceLabel);
		exportConceptDataPanel.add(deviceSelection);
		exportConceptDataPanel.add(quantityLabel);
		exportConceptDataPanel.add(quantity);
		exportConceptDataPanel.add(buy);
		content.add(exportConceptDataPanel);

		otherTableUpdate = new JPanel();
		otherTableUpdate.setBackground(Color.orange);
		otherTableUpdate.setBorder(BorderFactory.createTitledBorder(lineBorder, "Update devices table"));
		otherTableUpdate.setLayout(new GridLayout(6, 4));
		otherTableUpdate.setSize(390, 250);
		otherTableUpdate.setLocation(782, 250);

		otherTableUpdate.add(idDevicesLabel);
		otherTableUpdate.add(idDevices);
		otherTableUpdate.add(typeLabel);
		otherTableUpdate.add(type);
		otherTableUpdate.add(featureLabel);
		otherTableUpdate.add(feature);
		otherTableUpdate.add(ratingDevicesLabel);
		otherTableUpdate.add(ratingDevices);

		otherTableUpdate.add(updateDevicesButton);
		otherTableUpdate.add(insertDevicesButton);
		otherTableUpdate.add(deleteDevicesButton);

		content.add(otherTableUpdate);

		setSize(982, 645);
		setVisible(true);

		TableModel.refreshFromDB(stmt);
	}

	public void initiate_db_conn() {
		try {
			// Load the JConnector Driver
			Class.forName("com.mysql.jdbc.Driver");
			// Specify the DB Name
			String url = "jdbc:mysql://localhost:3306/JDBCProject";
			// Connect to DB using DB URL, Username and password
			con = DriverManager.getConnection(url, "root", "przemysl01");
			// Create a generic statement which is passed to the TestInternalFrame1
			stmt = con.createStatement();
		} catch (Exception e) {
			System.out.println("Error: Failed to connect to database\n" + e.getMessage());
		}
	}

	public void pieGraph(ResultSet rs, String title) {
		try {
			DefaultPieDataset dataset = new DefaultPieDataset();

			while (rs.next()) {
				String category = rs.getString(1);
				String value = rs.getString(2);
				dataset.setValue(category + " " + value, new Double(value));
			}
			JFreeChart chart = ChartFactory.createPieChart(title, dataset, false, true, true);

			ChartFrame frame = new ChartFrame(title, chart);
			chart.setBackgroundPaint(Color.WHITE);
			frame.pack();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeToFile(ResultSet rs, String title) {
		try {
			FileWriter outputFile = new FileWriter(title);
			PrintWriter printWriter = new PrintWriter(outputFile);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();

			for (int i = 0; i < numColumns; i++) {
				printWriter.print(rsmd.getColumnLabel(i + 1) + ",");
			}
			printWriter.print("\n");
			while (rs.next()) {
				for (int i = 0; i < numColumns; i++) {
					printWriter.print(rs.getString(i + 1) + ",");
				}
				printWriter.print("\n");
				printWriter.flush();
			}
			printWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// event handling for members desktop
	public void actionPerformed(ActionEvent e) {
		Object target = e.getSource();
		String cmd = null;
		ResultSet rs = null;
		if (target.equals(featuresSelection)) {
			try {
				cmd = "select wanted_feature, sum(rating) from JDBCProject.user_preferences where gender = '"
						+ featuresSelectionTF.getText() + "' group by wanted_feature;";
				rs = stmt.executeQuery(cmd);
				pieGraph(rs, "Activities by age");
			} catch (SQLException sqle) {
				System.err.println("Error\n" + sqle.toString());
			}
		} else if (target.equals(clearButton)) {
			id.setText("");
			gender.setText("");
			wantedFeature.setText("");
			rating.setText("");

		}

		else if (target.equals(insertButton)) {
			try {
				String updateTemp = "INSERT INTO user_preferences VALUES ('" + id.getText() + "','" + gender.getText()
						+ "','" + wantedFeature.getText() + "','" + rating.getText() + "');";

				stmt.executeUpdate(updateTemp);

			} catch (SQLException sqle) {
				System.err.println("Error with members insert:\n" + sqle.toString());
			} finally {
				TableModel.refreshFromDB(stmt);
			}
		} else if (target.equals(deleteButton)) {

			try {
				String updateTemp = "DELETE FROM user_preferences WHERE id = '" + id.getText() + "';";
				stmt.executeUpdate(updateTemp);

			} catch (SQLException sqle) {
				System.err.println("Error with delete:\n" + sqle.toString());
			} finally {
				TableModel.refreshFromDB(stmt);
			}
		} else if (target.equals(updateButton)) {
			try {
				String updateTemp = "UPDATE user_preferences SET  gender = '" + gender.getText()
						+ "', wanted_feature = '" + wantedFeature.getText() + "', rating = '" + rating.getText()
						+ "' where id = '" + id.getText() + "';";

				System.out.println(updateTemp);
				stmt.executeUpdate(updateTemp);
				// these lines do nothing but the table updates when we access the db.
				rs = stmt.executeQuery("SELECT * from user_preferences ");
				rs.next();
				rs.close();
			} catch (SQLException sqle) {
				System.err.println("Error with members insert:\n" + sqle.toString());
			} finally {
				TableModel.refreshFromDB(stmt);
			}
		} else if (target.equals(exportButton)) {
			try {
				// set cmd to write out all the table data to the csv
				cmd = "select * from JDBCProject.user_preferences";
				rs = stmt.executeQuery(cmd);
				writeToFile(rs, "User preferences table.csv");
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				JOptionPane.showMessageDialog(null, "Data exported.");
			}
		} else if (target.equals(deviceTableExport)) {
			try {
				// set cmd to write out all the table data to the csv
				cmd = "select * from JDBCProject.Devices";
				rs = stmt.executeQuery(cmd);
				writeToFile(rs, "Devices details table.csv");
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				JOptionPane.showMessageDialog(null, "Data exported.");
			}
		} else if (target.equals(bestPhoneSelection)) {
			try {
				cmd = "select devices.type from JDBCProject.Devices where devices.rating=(select max(rating)from devices where devices.feature = '"
						+ bestPhoneSelectionTF.getText() + "');";
				rs = stmt.executeQuery(cmd);
				while (rs.next()) {
					message = "Best device for you is: " + rs.getObject(1).toString();
					rs.getObject(1).toString();
					messageDisplay.setText(message);
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		} else if (target.equals(buy)) {
			try {
				String updateSales = "INSERT INTO Sales VALUES (null, '" + deviceSelection.getText() + "','"
						+ bestPhoneSelectionTF.getText() + "', '" + quantity.getText() + "');";
				stmt.executeUpdate(updateSales);
				purchaseConfirmation = "Device has been purchased";
				JOptionPane.showMessageDialog(null, purchaseConfirmation);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}

		} else if (target.equals(insertDevicesButton)) {
			try {
				String updateTemp = "INSERT INTO JDBCProject.Devices VALUES ('" + idDevices.getText() + "', '"
						+ type.getText() + "','" + feature.getText() + "','" + ratingDevices.getText() + "');";

				stmt.executeUpdate(updateTemp);
				System.out.println(updateTemp);

			} catch (SQLException sqle) {
				System.err.println("Error with members insert:\n" + sqle.toString());
			}
		} else if (target.equals(deleteDevicesButton)) {

			try {
				String updateTemp = "DELETE FROM Devices WHERE id = '" + idDevices.getText() + "';";
				stmt.executeUpdate(updateTemp);

			} catch (SQLException sqle) {
				System.err.println("Error with delete:\n" + sqle.toString());
			}
		} else if (target.equals(updateDevicesButton)) {
			try {
				String updateTemp = "UPDATE Devices SET  type = '" + type.getText() + "', feature = '"
						+ feature.getText() + "', rating = '" + ratingDevices.getText() + "' where id = '"
						+ idDevices.getText() + "';";

				System.out.println(updateTemp);
				stmt.executeUpdate(updateTemp);
			} catch (SQLException sqle) {
				System.err.println("Error with members insert:\n" + sqle.toString());
			}
		} else if (target.equals(salesRating)) {
			try {
				cmd = "select device, sum(quantity) from JDBCProject.Sales where feature_selected = '"
						+ salesRatingTF.getText() + "' group by device;";
				rs = stmt.executeQuery(cmd);
				pieGraph(rs, "Devices purchased");
			} catch (SQLException sqle) {
				System.err.println("Error\n" + sqle.toString());
			}
		} else if (target.equals(exportSales)) {
			try {
				// set cmd to write out all the table data to the csv
				cmd = "select * from JDBCProject.Sales";
				rs = stmt.executeQuery(cmd);
				writeToFile(rs, "Sales details table.csv");
			} catch (Exception e1) {
				e1.printStackTrace();
			} finally {
				JOptionPane.showMessageDialog(null, "Data exported.");
			}
		}
	}
}
