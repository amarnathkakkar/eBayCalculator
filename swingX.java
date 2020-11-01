import javax.swing.*;
import java.awt.event.*;  
import javax.swing.text.BadLocationException;
import javax.swing.event.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class swingX implements ActionListener{

	JFrame f;

	int width, height, middleFirstWindow, middleSecondWindow, middleHeight;

    int nextLabelYPos = 0;

	JLabel itemLabel, shippingLabel, parcelLabel, vatLabel, catLabel, subCatLabel;
    JTextField itemField, shippingField, parcelField, vatField, catField, subCatField;
    JComboBox<String> categoryComboBox, subCatComboBox;

    JTextArea resultsArea;
    JScrollPane scroll;

	JButton calculateButton, resetButton, clearButton;

	eBayProfitCalculator calc;
	JSONReader json;

	String usrDefinedInputStream;

	boolean gotPriceOfOneGood = false;


    public swingX() {
		f = new JFrame();
    	width = 800;
    	height = 400;
    	middleFirstWindow = width/4;
    	middleSecondWindow = 3*width/4;
    	middleHeight = height/2;

    	calc = new eBayProfitCalculator();
    	json = new JSONReader();
    }


    public void createComponents() {
    	//-------------------------------------------------------------------------------------- ADDING LABELS & FIELDS
    	itemLabel = new JLabel("Item Cost");
    	itemField = new JTextField();
    	displayLabelAndTextField(itemLabel, itemField);

    	shippingLabel = new JLabel("Shipping Cost");
    	shippingField = new JTextField();
    	displayLabelAndTextField(shippingLabel, shippingField);

    	parcelLabel = new JLabel("Parcel Cost");
    	parcelField = new JTextField();
    	displayLabelAndTextField(parcelLabel, parcelField);

    	vatLabel = new JLabel("VAT");
    	vatField = new JTextField();
    	displayLabelAndTextField(vatLabel, vatField);

		

		
    	catLabel = new JLabel("Category");
    	categoryComboBox = new JComboBox<String>(json.getCategories());
    	
    	categoryComboBox.addActionListener (new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                
                String str = categoryComboBox.getSelectedItem().toString();

                subCatComboBox.setModel(
                	new DefaultComboBoxModel<String>(json.getSubCat(str)) 
                );

            }
        });
    	displayLabelAndCombo(catLabel, categoryComboBox);

    	
    	subCatLabel = new JLabel("Subcategory");
    	subCatComboBox = new JComboBox<String>();	
    	displayLabelAndCombo(subCatLabel, subCatComboBox);
		


    	//-------------------------------------------------------------------------------------- ADDING RESULTS PANEL
    	resultsArea = new JTextArea(null, 0, 0); 
        resultsArea.setLineWrap(true);
        resultsArea.setEditable(false);
        resultsArea.addKeyListener(new KeyAdapter() {
  			public void keyReleased(KeyEvent e) {
  				if(e.getKeyCode() == KeyEvent.VK_ENTER) {

					if (gotPriceOfOneGood == false) {
	  					usrDefinedInputStream = getUsrInput() + System.getProperty("line.separator");;
	  					resultsArea.append("numberOfGoods = ");
	  					gotPriceOfOneGood = true;
					}
					else {
  						String numberOfGoods = getUsrInput();

	  					if (numberOfGoods == null || numberOfGoods.isEmpty()) {

	  						usrDefinedInputStream += System.getProperty("line.separator");

	  						//System.out.println(usrDefinedInputStream);

	  						calc.setInputStream(usrDefinedInputStream);
	  						calc.setOutputStream(resultsArea);

	  						calc.startWithSwing();

	  						resultsArea.setEditable(false);
	  						gotPriceOfOneGood = false;
	  					} 
	  					else {
	  						usrDefinedInputStream += numberOfGoods + System.getProperty("line.separator");
	  						resultsArea.append("numberOfGoods = ");
	  					}

					}
  				}
  			}
	    });

        scroll = new JScrollPane(resultsArea);
        scroll.setLocation(width/2,0);
        scroll.setSize(width/2,height-5);

        f.add(scroll);
        //f.pack();

        

        //-------------------------------------------------------------------------------------- ADDING BUTTONS
		calculateButton = new JButton("Calculate");
		calculateButton.setBounds(50,height-100, 100,40);
		calculateButton.addActionListener(this); 
		f.add(calculateButton);

		resetButton = new JButton("Reset");
		resetButton.setBounds(150,height-100, 100,40);
		resetButton.addActionListener(this);
		f.add(resetButton);

		clearButton = new JButton("Clear");
		clearButton.setBounds(250,height-100, 100,40);
		clearButton.addActionListener(this);
		f.add(clearButton);

    }


    public String getUsrInput () {
  		try {
			String lastLine = resultsArea.getText(
				resultsArea.getLineStartOffset(resultsArea.getLineCount()-2),
				resultsArea.getLineEndOffset(resultsArea.getLineCount()-2) - resultsArea.getLineStartOffset(resultsArea.getLineCount()-2) - 1
			);

			String lastInputedValue = lastLine.substring(lastLine.indexOf("=") + 2);

			return lastInputedValue;

		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}

		return null;
    }


	public void displayLabelAndTextField(JLabel label, JTextField field) {

		label.setBounds(50,nextLabelYPos, 100,30);
		field.setBounds(middleFirstWindow,nextLabelYPos, 100,30);

		if (nextLabelYPos>=height-100) {
			height += 100; 
		}

		f.add(label);
		f.add(field);

		nextLabelYPos += 50;
	}


	public void displayLabelAndCombo(JLabel label, JComboBox<String> comboBox) {

		label.setBounds(50,nextLabelYPos, 100,30);
		comboBox.setBounds(middleFirstWindow,nextLabelYPos, 200,30);

		if (nextLabelYPos>=height-100) {
			height += 100; 
		}

		f.add(label);
		f.add(comboBox);

		nextLabelYPos += 50;
	}


	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == resetButton) {
			itemField.setText("");
			shippingField.setText("");
			parcelField.setText("");
			vatField.setText("");
			categoryComboBox.setSelectedIndex(0);
			subCatComboBox.setSelectedIndex(0);

			gotPriceOfOneGood = false;
		} 
		else if (e.getSource() == calculateButton) {

			String itemFieldValue = itemField.getText();
			String shippingFieldValue = shippingField.getText();
			String parcelFieldValue = parcelField.getText();
			String vatFieldValue = vatField.getText();
			String categoryComboBoxValue = (String) categoryComboBox.getSelectedItem();
			String subCatComboBoxValue = (String) subCatComboBox.getSelectedItem();

			gotPriceOfOneGood = false;

			calc.setVariables(itemFieldValue, shippingFieldValue, parcelFieldValue, vatFieldValue, categoryComboBoxValue, subCatComboBoxValue);

			resultsArea.append(calc.getVariables()+"\n");
			resultsArea.append("Type anything other than a number to exit.\n");
			resultsArea.append("priceOfOneGood = ");

			resultsArea.setEditable(true);
			/*
			try {
				resultsArea.insert(
					calc.getVariables(), 
					resultsArea.getLineStartOffset(
						resultsArea.getLineCount() - 1
					)
				);
			}
			catch (BadLocationException ex) {
				ex.printStackTrace();
			}

			
			System.out.println(resultsArea.getLineCount());
			*/

		} else if (e.getSource() == clearButton) {
			gotPriceOfOneGood = false;
			resultsArea.setText(null);
		}
	}


	public void createAndShowGUI() {
		createComponents();
		f.setSize(width,height);
		f.setResizable(false);
		f.setLayout(null);
		f.setVisible(true);
	}


	public static void main(String args[]) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			new swingX().createAndShowGUI();
			}
		});
	}
}