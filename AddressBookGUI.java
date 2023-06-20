import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class AddressBookGUI extends JFrame {
    
    private DefaultListModel<String> contactsListModel;
    private JList<String> contactsList;
    private JButton showButton;
    private JButton addButton;
    private JButton deleteButton;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField addressField;
    private JTextField emailField;
    
    private List<Contact> contacts;
    
    private static final String FILE_PATH = "abook.txt";

    public AddressBookGUI() {
        initialize();
        loadContacts();
    }
    
    private void initialize() {
        setTitle("Address Book");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        contactsListModel = new DefaultListModel<>();
        contactsList = new JList<>(contactsListModel);
        contactsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(contactsList);
        add(scrollPane, BorderLayout.CENTER);
        
        showButton = new JButton("Show Contact");
        addButton = new JButton("Add Contact");
        deleteButton = new JButton("Delete Contact");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(showButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showContact();
            }
        });
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addContact();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteContact();
            }
        });
        
        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        addressField = new JTextField(20);
        emailField = new JTextField(20);
        
        JPanel inputPanel = new JPanel();
        inputPanel.add(nameField);
        inputPanel.add(phoneField);
        inputPanel.add(addressField);
        inputPanel.add(emailField);
        
        add(inputPanel, BorderLayout.NORTH);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void loadContacts() {
        contacts = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    String phone = parts[1];
                    String address = parts[2];
                    String email = parts[3];
                    Contact contact = new Contact(name, phone, address, email);
                    contacts.add(contact);
                    contactsListModel.addElement(name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void saveContacts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Contact contact : contacts) {
                String line = contact.getName() + "," + contact.getPhone() + "," + contact.getAddress()
                        + "," + contact.getEmail();
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showContact() {
        int selectedIndex = contactsList.getSelectedIndex();
        if (selectedIndex != -1) {
            Contact contact = contacts.get(selectedIndex);
            nameField.setText(contact.getName());
            phoneField.setText(contact.getPhone());
            addressField.setText(contact.getAddress());
            emailField.setText(contact.getEmail());
        }
    }
    
    private void addContact() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        if (!name.isEmpty() && !phone.isEmpty() && !address.isEmpty() && !email.isEmpty()) {
            Contact contact = new Contact(name, phone, address, email);
            contacts.add(contact);
            contactsListModel.addElement(name);
            clearFields();
            saveContacts();
        }
    }
    
    private void deleteContact() {
        int selectedIndex = contactsList.getSelectedIndex();
        if (selectedIndex != -1) {
            contacts.remove(selectedIndex);
            contactsListModel.remove(selectedIndex);
            clearFields();
            saveContacts();
        }
    }
    
    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        addressField.setText("");
        emailField.setText("");
    }
    
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddressBookGUI().setVisible(true);
            }
        });
    }
    
    private static class Contact {
        
        private String name;
        private String phone;
        private String address;
        private String email;
        
        public Contact(String name, String phone, String address, String email) {
            this.name = name;
            this.phone = phone;
            this.address = address;
            this.email = email;
        }
        
        public String getName() {
            return name;
        }
        
        public String getPhone() {
            return phone;
        }
        
        public String getAddress() {
            return address;
        }
        
        public String getEmail() {
            return email;
        }
    }
}
