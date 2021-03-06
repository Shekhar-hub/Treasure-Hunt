

import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DELL
 */
public class Add_stock extends javax.swing.JInternalFrame {

    /**
     * Creates new form Add_stock
     */
    //datechooser
    Connection con;
    public Add_stock(Connection con) {
        initComponents();
        this.con=con;
        search_txt.setUI(new HintTextFieldUI("Search", true));
        pre_data();
        show_stock_details();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        add_stock_panel = new javax.swing.JPanel();
        stock_details_panel = new javax.swing.JPanel();
        material_name_txt = new javax.swing.JTextField();
        material_name_label = new javax.swing.JLabel();
        quantity_label = new javax.swing.JLabel();
        quantity_txt = new javax.swing.JTextField();
        dob_purchased_label = new javax.swing.JLabel();
        price_txt = new javax.swing.JTextField();
        delete_btn = new javax.swing.JButton();
        submit_btn = new javax.swing.JButton();
        reset_btn = new javax.swing.JButton();
        view_stock_scrollpane = new javax.swing.JScrollPane();
        view_stock_table = new javax.swing.JTable();
        search_txt = new javax.swing.JTextField();
        search_btn = new javax.swing.JButton();
        id_label = new javax.swing.JLabel();
        id_txt = new javax.swing.JTextField();
        dob_date_chooser = new com.toedter.calendar.JDateChooser();
        price_label = new javax.swing.JLabel();
        material_name_cmb = new javax.swing.JComboBox<>();
        category_label = new javax.swing.JLabel();
        category_txt = new javax.swing.JTextField();
        category_cmb = new javax.swing.JComboBox<>();
        supplier_name_txt = new javax.swing.JTextField();
        supplier_name_label = new javax.swing.JLabel();
        supplier_name_cmb = new javax.swing.JComboBox<>();
        add_stock_label = new javax.swing.JLabel();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        add_stock_panel.setBackground(new java.awt.Color(204, 0, 204));
        add_stock_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        stock_details_panel.setBackground(new java.awt.Color(255, 153, 0));
        stock_details_panel.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(0, 0, 204)));
        stock_details_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        material_name_txt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                material_name_txtFocusLost(evt);
            }
        });
        material_name_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                material_name_txtActionPerformed(evt);
            }
        });
        stock_details_panel.add(material_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, 340, 30));

        material_name_label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        material_name_label.setText("Material Name");
        stock_details_panel.add(material_name_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        quantity_label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        quantity_label.setText("Quantity");
        stock_details_panel.add(quantity_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 340, -1, -1));
        stock_details_panel.add(quantity_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 370, 340, 30));

        dob_purchased_label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dob_purchased_label.setText("Date of Purchased");
        stock_details_panel.add(dob_purchased_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 520, -1, -1));
        stock_details_panel.add(price_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 460, 340, 30));

        delete_btn.setBackground(new java.awt.Color(0, 0, 204));
        delete_btn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        delete_btn.setForeground(new java.awt.Color(255, 255, 255));
        delete_btn.setText("Delete");
        delete_btn.setBorder(null);
        delete_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_btnActionPerformed(evt);
            }
        });
        stock_details_panel.add(delete_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 640, 80, 30));

        submit_btn.setBackground(new java.awt.Color(0, 0, 204));
        submit_btn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        submit_btn.setForeground(new java.awt.Color(255, 255, 255));
        submit_btn.setText("Submit");
        submit_btn.setBorder(null);
        submit_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submit_btnActionPerformed(evt);
            }
        });
        stock_details_panel.add(submit_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 640, 80, 30));

        reset_btn.setBackground(new java.awt.Color(0, 0, 204));
        reset_btn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        reset_btn.setForeground(new java.awt.Color(255, 255, 255));
        reset_btn.setText("Reset");
        reset_btn.setBorder(null);
        reset_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reset_btnActionPerformed(evt);
            }
        });
        stock_details_panel.add(reset_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 640, 80, 30));

        view_stock_scrollpane.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                view_stock_scrollpaneMouseClicked(evt);
            }
        });

        view_stock_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        view_stock_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                view_stock_tableMouseClicked(evt);
            }
        });
        view_stock_scrollpane.setViewportView(view_stock_table);

        stock_details_panel.add(view_stock_scrollpane, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 110, 750, 480));

        search_txt.setBackground(new java.awt.Color(255, 153, 0));
        search_txt.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        search_txt.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(0, 0, 0)));
        stock_details_panel.add(search_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 50, 290, 30));

        search_btn.setBackground(new java.awt.Color(0, 0, 204));
        search_btn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        search_btn.setForeground(new java.awt.Color(255, 255, 255));
        search_btn.setText("Search");
        search_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search_btnActionPerformed(evt);
            }
        });
        stock_details_panel.add(search_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 50, -1, 30));

        id_label.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        id_label.setText("ID");
        stock_details_panel.add(id_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        id_txt.setEditable(false);
        id_txt.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        id_txt.setEnabled(false);
        stock_details_panel.add(id_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 90, 30));
        stock_details_panel.add(dob_date_chooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 550, 340, 30));

        price_label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        price_label.setText("Price per material");
        stock_details_panel.add(price_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 430, -1, -1));

        material_name_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---Select Material---" }));
        material_name_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                material_name_cmbActionPerformed(evt);
            }
        });
        stock_details_panel.add(material_name_cmb, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 110, 140, 30));

        category_label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        category_label.setText("Category");
        stock_details_panel.add(category_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, -1, -1));

        category_txt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                category_txtFocusLost(evt);
            }
        });
        category_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                category_txtActionPerformed(evt);
            }
        });
        stock_details_panel.add(category_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 340, 30));

        category_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---Select Category---" }));
        category_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                category_cmbActionPerformed(evt);
            }
        });
        stock_details_panel.add(category_cmb, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 190, 140, 30));

        supplier_name_txt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                supplier_name_txtFocusLost(evt);
            }
        });
        supplier_name_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplier_name_txtActionPerformed(evt);
            }
        });
        stock_details_panel.add(supplier_name_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 340, 30));

        supplier_name_label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        supplier_name_label.setText("Supplier Name");
        stock_details_panel.add(supplier_name_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, -1, -1));

        supplier_name_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "---Select Supplier---" }));
        supplier_name_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplier_name_cmbActionPerformed(evt);
            }
        });
        stock_details_panel.add(supplier_name_cmb, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 280, 140, 30));

        add_stock_panel.add(stock_details_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 1300, 690));

        add_stock_label.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        add_stock_label.setForeground(new java.awt.Color(255, 255, 255));
        add_stock_label.setText("ADD  STOCK DETAILS");
        add_stock_panel.add(add_stock_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 20, -1, -1));

        getContentPane().add(add_stock_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1350, 840));

        pack();
    }// </editor-fold>//GEN-END:initComponents
public void pre_data()
{
        stock_id_show();
        material_names();
        category_names();
        supplier_names();
}
    public Vector show_data(String search)
{
    String found=search;
     try {
            PreparedStatement ps = con.prepareStatement(query_show);
            if(!found.isEmpty())
            {
                ps.setString(1,found);
            }
            ResultSet rs = ps.executeQuery();
            h = new Vector();
           Vector d = new Vector();
           h.add("S.No.");
            h.add("M.Name");
            h.add("M.Category");
            
            h.add("M.Qty");
           //h.add("M.Unit");
            h.add("S.Name");
            //h.add("Price Unit");
            h.add("Price");
            h.add("Total price");
            h.add("Date");
            
            row=0;
            while (rs.next()) {
                Vector d1=new Vector();
                
                d1.add(rs.getString("serial_id"));
                d1.add(rs.getString("material_name"));
                d1.add(rs.getString("category_name"));
                d1.add(rs.getString("material_qty"));
                //d1.add(rs.getString("material_qty_unit"));
                d1.add(rs.getString("name"));
                //d1.add(rs.getString("material_price_unit"));
                d1.add(rs.getString("material_price"));
                 d1.add(rs.getString("total_price"));
                  d1.add(rs.getString("purchased_date"));
                d.add(d1);
                row++;
            }
            if(d.isEmpty())
            {
                return null;
            }
            return d;
           // DefaultTableModel dtf=new DefaultTableModel(d,h);
            //view_stock_entry_table.setModel(dtf);
            
        } catch (SQLException ex) {
           System.out.println(ex.getMessage());
           return null;
        }
        
}
    public void show_stock_details() {
        query_show ="select * from stock_entry a inner join stock_category b on a.category_id = b.serial_id inner join supplier c on a.supplier_id = c.serial_id order by a.serial_id desc";
        Vector d=show_data("");
        System.out.println(d);
            DefaultTableModel dtf=new DefaultTableModel(d,h);
            view_stock_table.setModel(dtf);
      
        
    }
    private void material_name_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_material_name_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_material_name_txtActionPerformed
public void stock_id_show()
{
    String stock_id_separator[]=new String[4000];
     String query1 = "select serial_id as count_id from stock_entry order by serial_id desc";
     PreparedStatement ps;
        try {
            ps = con.prepareStatement(query1);
             ResultSet rs=ps.executeQuery();
             if(rs.next())
             {
                 stock_id=rs.getString("count_id");
             }
        } catch (SQLException ex) {
            Logger.getLogger(Add_stock.class.getName()).log(Level.SEVERE, null, ex);
        }
        stock_id_separator=stock_id.split("IM");
        
    
     if(stock_id_separator.length>1)
            {
            stock_id= String.valueOf((Integer.parseInt(stock_id_separator[1]) + 1));
            }
            else
            {
               stock_id= String.valueOf((Integer.parseInt(stock_id) + 1));
            }
           id_txt.setText("IM"+stock_id);
}
    public void material_names()
{
    material_name_cmb.removeAllItems();
    material_name_cmb.addItem("---Select Material---");
    String query="select material_name from stock_entry";
        try {
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                material_name_cmb.addItem(rs.getString("material_name"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Add_stock.class.getName()).log(Level.SEVERE, null, ex);
        }
    
}
public void category_names()
{
    serial_id_list=new ArrayList<>();
    category_cmb.removeAllItems();
    category_cmb.addItem("---Select Category---");
    String query="select serial_id,category_name from stock_category";
        try {
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                serial_id_list.add(rs.getString("serial_id"));
                category_cmb.addItem(rs.getString("category_name"));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Add_stock.class.getName()).log(Level.SEVERE, null, ex);
        }
}
public void supplier_names()
{
    supplier_id_list=new ArrayList<>();
  supplier_name_cmb.removeAllItems();
    supplier_name_cmb.addItem("---Select Supplier---");
    String query="select serial_id,name from supplier";
        try {
            PreparedStatement ps=con.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                supplier_id_list.add(rs.getString("serial_id"));
                supplier_name_cmb.addItem(rs.getString("name"));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Add_stock.class.getName()).log(Level.SEVERE, null, ex);
        }  
}

    private void delete_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_btnActionPerformed
        // TODO add your handling code here:
      
      
        delete_stock_details();
        reset_form_fields();
        show_stock_details();
        JOptionPane.showMessageDialog(this, "Data deleted successfully!");
    
    }//GEN-LAST:event_delete_btnActionPerformed
public void delete_stock_details()
{
    String query = "delete from stock_entry  where serial_id=?";
           
    try {
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1,id_txt.getText());
                ps.executeUpdate();
                ps.close();
                
    
}       catch (SQLException ex) {
            Logger.getLogger(Add_stock.class.getName()).log(Level.SEVERE, null, ex);
        }
}
    public void update_stock_details()
{
   String query = "update stock_entry set material_name=?,category_id=?,material_qty=?,material_qty_unit=?,supplier_id=?,material_price=?,material_price_unit=?,total_price=?,purchased_date=? where serial_id=?";
           
    try {
                PreparedStatement ps = con.prepareStatement(query);
                
                ps.setString(1,material_name);
                ps.setString(2,serial_id_list.get((category_cmb.getSelectedIndex())-1));
                ps.setString(3,quantity);
                ps.setString(4,"");
                ps.setString(5,supplier_id_list.get((supplier_name_cmb.getSelectedIndex())-1));
                ps.setString(6, price);
                ps.setString(7,"");
                String total=String.valueOf(Integer.parseInt(price)*Integer.parseInt(quantity));
                ps.setString(8,total);
                ps.setString(9,"");
                ps.setString(10,id_txt.getText());
                int n = ps.executeUpdate();
                
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(Add_Customer.class.getName()).log(Level.SEVERE, null, ex);
            } 
}
    public void submit_stock_entry()
{
    System.out.println(dob_date_chooser.getDate());
    String query = "insert into stock_entry(serial_id,material_name,category_id,material_qty,material_qty_unit,supplier_id,material_price,material_price_unit,total_price,purchased_date) values(?,?,?,?,?,?,?,?,?,?)";
           
    try {
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1,id_txt.getText());
                ps.setString(2,material_name);
                ps.setString(3,serial_id_list.get((category_cmb.getSelectedIndex())-1));
                ps.setString(4,quantity);
                ps.setString(5, "");
                ps.setString(6,supplier_id_list.get((supplier_name_cmb.getSelectedIndex())-1));
                ps.setString(7, price);
                ps.setString(8,"");
                String total=String.valueOf(Integer.parseInt(price)*Integer.parseInt(quantity));
                ps.setString(9,total);
                ps.setString(10,"");
                int n = ps.executeUpdate();
                
                ps.close();
            } catch (SQLException ex) {
                Logger.getLogger(Add_Customer.class.getName()).log(Level.SEVERE, null, ex);
            }
}
public void category_add()
{
  String query = "insert into stock_category(category_name) values(?)";
  String query1 = "select serial_id from stock_category where category_name=?";
           
    try {
                PreparedStatement ps = con.prepareStatement(query);
                PreparedStatement ps1 = con.prepareStatement(query1);
                ps.setString(1,category_name);
                ps1.setString(1,category_name);
                ps.executeUpdate();
                
                ps.close();
                ResultSet rs=ps1.executeQuery();
                if(rs.next())
                {
                    serial_id_list.add(rs.getString("serial_id"));
                }
                ps1.close();
               
}       catch (SQLException ex) {
            Logger.getLogger(Add_stock.class.getName()).log(Level.SEVERE, null, ex);
        }
}
    private void submit_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submit_btnActionPerformed
        // TODO add your handling code here:
      
        material_name = material_name_txt.getText();
        category_name = category_txt.getText();
        quantity = quantity_txt.getText();
        //qty_cmb=quantity_cmb.getSelectedIndex();
        //price_cmb=price_cmbx.getSelectedIndex();
        supplier_name = supplier_name_txt.getText();
        price = price_txt.getText();
        //purchased_date=dob_date_chooser.get
        if (material_name.trim().matches("")) {
            JOptionPane.showMessageDialog(this, "'Material Name' Field cannot be empty");
        } else if (category_name.trim().matches("")) {
            JOptionPane.showMessageDialog(this, "'Category Name' Field cannot be empty");
        } else if (quantity.trim().matches("")) {
            JOptionPane.showMessageDialog(this, "'Quantity' Field cannot be empty");
        }
        
        else if (supplier_name.trim().matches("")) {
            JOptionPane.showMessageDialog(this, "'Supplier Name' Field cannot be empty");
        
        }else if (price.trim().matches("")) {
            JOptionPane.showMessageDialog(this, "'Price per material' Field cannot be empty");
        
        }
        
        else {
             if(!found_category)
                {
                    category_add();
                    
                }
            if(submit_btn.getText().matches("Submit"))
            {
                submit_stock_entry();
                JOptionPane.showMessageDialog(this, "Data added successfully!");
            }
            else
            {
                update_stock_details();
               
                JOptionPane.showMessageDialog(this, "Data updated successfully!");

            }
            //pre_data();
            
            reset_form_fields();
            show_stock_details();

        }

    }//GEN-LAST:event_submit_btnActionPerformed
public void reset_form_fields()
{
        pre_data();
        
        quantity_txt.setText("");
       // quantity_cmb.setSelectedIndex(0);
        price_txt.setText("");
        //price_cmbx.setSelectedIndex(0);
        dob_date_chooser.setDateFormatString("");
        dob_date_chooser.setDateFormatString("");
        submit_btn.setText("Submit");
        found_category=false;
        delete_btn.setVisible(false);
}
    private void reset_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reset_btnActionPerformed
        // TODO add your handling code here:
       reset_form_fields();
        show_stock_details();

    }//GEN-LAST:event_reset_btnActionPerformed

    private void view_stock_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_view_stock_tableMouseClicked
        // TODO add your handling code here:
       int row=view_stock_table.getSelectedRow();
        TableModel tm=view_stock_table.getModel();
        serial_id=(String) tm.getValueAt(row,0);
        id_txt.setText(serial_id);
        material_name_cmb.setSelectedItem((String) tm.getValueAt(row,1));
        found_category=true;
        category_cmb.setSelectedItem((String) tm.getValueAt(row,2));
        quantity_txt.setText((String) tm.getValueAt(row,3));
       // quantity_cmb.setSelectedItem((String) tm.getValueAt(row,4));
        supplier_name_cmb.setSelectedItem((String) tm.getValueAt(row,4));
        // price_cmbx.setSelectedItem((String) tm.getValueAt(row,6));
        price_txt.setText((String) tm.getValueAt(row,5));
       
        submit_btn.setText("Update");
        delete_btn.setVisible(true);

    }//GEN-LAST:event_view_stock_tableMouseClicked

    private void search_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search_btnActionPerformed
        // TODO add your handling code here:
       String search_stock=search_txt.getText();
     
        if(search_stock.isEmpty())
        {
            JOptionPane.showMessageDialog(this,"Please Enter Something to search!!!");
        }else
        {
            query_show = "select * from stock_entry a inner join stock_category b on a.category_id = b.serial_id inner join supplier c on a.supplier_id = c.serial_id and a.serial_id=? order by a.serial_id desc";
            Vector d1=show_data(search_stock);
            if(d1==null)
            {
                query_show = "select * from stock_entry a inner join stock_category b on a.category_id = b.serial_id inner join supplier c on a.supplier_id = c.serial_id and a.material_name=?order by a.serial_id desc";
                d1=show_data(search_stock);
                System.out.println("sk");
                System.out.println(d1);
                if(d1==null)
                {
                    query_show = "select * from stock_entry a inner join stock_category b on a.category_id = b.serial_id inner join supplier c on a.supplier_id = c.serial_id and a.purchased_date=? order by a.serial_id desc";
                    d1=show_data(search_stock);
                    System.out.println("sk");
                    if(d1==null)
                    {
                        query_show = "select * from stock_entry a inner join stock_category b on a.category_id = b.serial_id inner join supplier c on a.supplier_id = c.serial_id and b.category_name=? order by a.serial_id desc ";
                        d1=show_data(search_stock);
                        System.out.println("sk"+d1);
                        if(d1==null )
                        {
                            query_show = "select * from stock_entry a inner join stock_category b on a.category_id = b.serial_id inner join supplier c on a.supplier_id = c.serial_id and c.name=? order by a.serial_id desc";
                            d1=show_data(search_stock);
                            System.out.println("ok");
                            
                                if(d1==null)
                                {
                                    query_show = "select * from stock_entry a inner join stock_category b on a.category_id = b.serial_id inner join supplier c on a.supplier_id = c.serial_id and a.serial_id LIKE '"+search_stock+"%' order by a.serial_id desc";
                                    d1=show_data("");
                                    System.out.println("sk try"+d1);
                                    if(d1==null)
                                    {
                                        query_show = "select * from stock_entry a inner join stock_category b on a.category_id = b.serial_id inner join supplier c on a.supplier_id = c.serial_id and a.material_name  material_name LIKE '"+search_stock+"%' order by a.serial_id desc";
                                        d1=show_data("");
                                        if( d1==null)
                                        {
                                            query_show = "select * from stock_entry a inner join stock_category b on a.category_id = b.serial_id inner join supplier c on a.supplier_id = c.serial_id and a.purchased_date LIKE '"+search_stock+"%' order by a.serial_id desc";
                                            d1=show_data("");
                                            if( d1==null)
                                            {
                                                query_show = "select * from stock_entry a inner join stock_category b on a.category_id = b.serial_id inner join supplier c on a.supplier_id = c.serial_id and b.category_name LIKE '"+search_stock+"%' order by a.serial_id desc";
                                                d1=show_data("");
                                               if( d1==null)
                                            {
                                                query_show = "select * from stock_entry a inner join stock_category b on a.category_id = b.serial_id inner join supplier c on a.supplier_id = c.serial_id and c.name LIKE '"+search_stock+"%' order by a.serial_id desc";
                                                d1=show_data("");
                                               
                                            }
                                            }
                                        }

                                    }
                                

                            }
                        }
                    }
                }
            }

            if(d1==null)
            {
                JOptionPane.showMessageDialog(this,"No result found!\nTry Again");
                System.out.println("sk");
            }
            else
            {
                DefaultTableModel dtf=new DefaultTableModel(d1,h);
                view_stock_table.setModel(dtf);
                if(row>1)
                {
                    JOptionPane.showMessageDialog(this,row+" rows found!");
                }
                else
                {
                    JOptionPane.showMessageDialog(this,row+" row found!");
                }
                System.out.println("k");
            }
        }

    }//GEN-LAST:event_search_btnActionPerformed

    private void category_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_category_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_category_txtActionPerformed

    private void supplier_name_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplier_name_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplier_name_txtActionPerformed

    private void view_stock_scrollpaneMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_view_stock_scrollpaneMouseClicked
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_view_stock_scrollpaneMouseClicked

    private void material_name_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_material_name_cmbActionPerformed
        // TODO add your handling code here:
        if(material_name_cmb.getSelectedIndex()>0)
        {
            material_name_txt.setText((String) material_name_cmb.getSelectedItem());
        }
        else
        {
            material_name_txt.setText("");
        }
    }//GEN-LAST:event_material_name_cmbActionPerformed

    private void category_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_category_cmbActionPerformed
        // TODO add your handling code here:
         if(category_cmb.getSelectedIndex()>0)
        {
            category_txt.setText((String) category_cmb.getSelectedItem());
        }
        else
        {
            category_txt.setText("");
        }
    }//GEN-LAST:event_category_cmbActionPerformed

    private void supplier_name_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplier_name_cmbActionPerformed
        // TODO add your handling code here:
         if(supplier_name_cmb.getSelectedIndex()>0)
        {
            supplier_name_txt.setText((String) supplier_name_cmb.getSelectedItem());
        }
        else
        {
            supplier_name_txt.setText("");
        }
    }//GEN-LAST:event_supplier_name_cmbActionPerformed

    private void material_name_txtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_material_name_txtFocusLost
        // TODO add your handling code here:
        boolean found_material=false;
        if(!material_name_txt.getText().trim().matches(""))
        {
            for(int check=1;check<material_name_cmb.getItemCount();check++)
            {
                if(material_name_txt.getText().equalsIgnoreCase(material_name_cmb.getItemAt(check)))
                {
                    found_material=true;
                    material_name_cmb.setSelectedIndex(check);
                }
            }
            if(!found_material)
            {
        material_name_cmb.addItem(material_name_txt.getText());
        material_name_cmb.setSelectedItem(material_name_txt.getText());
            }
        }
        else
        {
            material_name_cmb.setSelectedIndex(0);
        }
    }//GEN-LAST:event_material_name_txtFocusLost

    private void category_txtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_category_txtFocusLost
        // TODO add your handling code here:
        found_category=false;
         if(!category_txt.getText().trim().matches(""))
        {
            for(int check=1;check<category_cmb.getItemCount();check++)
            {
                if(category_txt.getText().equalsIgnoreCase(category_cmb.getItemAt(check)))
                {
                    found_category=true;
                    category_cmb.setSelectedIndex(check);
                }
            }
            if(!found_category)
            {
        category_cmb.addItem(category_txt.getText());
        category_cmb.setSelectedItem(category_txt.getText());
            }
        }
         else
        {
            category_cmb.setSelectedIndex(0);
        }
    }//GEN-LAST:event_category_txtFocusLost

    private void supplier_name_txtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_supplier_name_txtFocusLost
        // TODO add your handling code here:
       boolean found_supplier=false;
        supplier_name="";
        supplier_name=supplier_name_txt.getText();
        if(!supplier_name.isEmpty())
        {
            for(int check=0;check<supplier_name_cmb.getItemCount();check++)
            {
                if(supplier_name.equalsIgnoreCase(supplier_name_cmb.getItemAt(check)))
                {
                    found_supplier=true;
                    supplier_name_cmb.setSelectedIndex(check);
                }
            }
            if(found_supplier)
            {
                System.out.println("found");

            }
            else
            {
                System.out.println(" Not found");
                supplier_name_cmb.setSelectedIndex(0);
                //brand_items();
                JOptionPane.showMessageDialog(this,"No Matching Supplier Name found!\nPlease refer Drop Down Menu...\n                     or\nFirst add supplier details in 'ADD/VIEW Supplier' section...");

            }
        }
    }//GEN-LAST:event_supplier_name_txtFocusLost

String query_show="";
Vector h;
    ArrayList<String> serial_id_list,supplier_id_list;
int row=0;
boolean found_category=false;
String stock_id="0";
String material_name,category_name,quantity,supplier_name,price,serial_id;
int qty_cmb,price_cmb;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel add_stock_label;
    private javax.swing.JPanel add_stock_panel;
    private javax.swing.JComboBox<String> category_cmb;
    private javax.swing.JLabel category_label;
    private javax.swing.JTextField category_txt;
    private javax.swing.JButton delete_btn;
    private com.toedter.calendar.JDateChooser dob_date_chooser;
    private javax.swing.JLabel dob_purchased_label;
    private javax.swing.JLabel id_label;
    private javax.swing.JTextField id_txt;
    private javax.swing.JComboBox<String> material_name_cmb;
    private javax.swing.JLabel material_name_label;
    private javax.swing.JTextField material_name_txt;
    private javax.swing.JLabel price_label;
    private javax.swing.JTextField price_txt;
    private javax.swing.JLabel quantity_label;
    private javax.swing.JTextField quantity_txt;
    private javax.swing.JButton reset_btn;
    private javax.swing.JButton search_btn;
    private javax.swing.JTextField search_txt;
    private javax.swing.JPanel stock_details_panel;
    private javax.swing.JButton submit_btn;
    private javax.swing.JComboBox<String> supplier_name_cmb;
    private javax.swing.JLabel supplier_name_label;
    private javax.swing.JTextField supplier_name_txt;
    private javax.swing.JScrollPane view_stock_scrollpane;
    private javax.swing.JTable view_stock_table;
    // End of variables declaration//GEN-END:variables
}
