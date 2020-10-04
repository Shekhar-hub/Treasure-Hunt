
import java.awt.List;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
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
public class Add_Product extends javax.swing.JInternalFrame {

    /**
     * Creates new form Add_Product
     */
    Connection con;
    public Add_Product(Connection con) {
        initComponents();
        this.con=con;
        id_txt.setText("EXIM"+exim);
        display_data();
        brand_select();
        
        
        
        
    }
    public void brand_select()
    {
        brand_id.clear();
        brand_cmb.removeAllItems();
        brand_cmb.addItem("-----Select Brand-----");
        String query="select * from product_brand";
        try {
            PreparedStatement ps= con.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                brand_id.add(rs.getString("brand_id"));
                brand_cmb.addItem(rs.getString("brand_name"));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(Add_Product.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        view_sale_panel = new javax.swing.JPanel();
        view_details_panel = new javax.swing.JPanel();
        view_sale_scrollpane = new javax.swing.JScrollPane();
        view_product_table = new javax.swing.JTable();
        product_label = new javax.swing.JLabel();
        materials_choose_label = new javax.swing.JLabel();
        total_cost_txt = new javax.swing.JTextField();
        reset_btn = new javax.swing.JButton();
        add_btn = new javax.swing.JButton();
        delete_btn = new javax.swing.JButton();
        quantity_txt = new javax.swing.JTextField();
        id_label = new javax.swing.JLabel();
        id_txt = new javax.swing.JTextField();
        due_date_label = new javax.swing.JLabel();
        date_txt = new javax.swing.JTextField();
        product_cmb = new javax.swing.JComboBox<>();
        product_txt = new javax.swing.JTextField();
        brand_label = new javax.swing.JLabel();
        brand_txt = new javax.swing.JTextField();
        brand_cmb = new javax.swing.JComboBox<>();
        quantity_label = new javax.swing.JLabel();
        total_price_label = new javax.swing.JLabel();
        sell_price_txt = new javax.swing.JTextField();
        sell_price_label = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        materials_list = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        mat_final_list = new javax.swing.JList<>();
        evaluate_btn = new javax.swing.JButton();
        clear_btn = new javax.swing.JButton();
        reset_btn1 = new javax.swing.JButton();
        add_stock_label = new javax.swing.JLabel();

        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        view_sale_panel.setBackground(new java.awt.Color(204, 0, 51));
        view_sale_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        view_details_panel.setBackground(new java.awt.Color(255, 255, 0));
        view_details_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        view_product_table.setModel(new javax.swing.table.DefaultTableModel(
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
        view_product_table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                view_product_tableMouseClicked(evt);
            }
        });
        view_sale_scrollpane.setViewportView(view_product_table);

        view_details_panel.add(view_sale_scrollpane, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 120, 550, 460));

        product_label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        product_label.setText("Prodouct");
        view_details_panel.add(product_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, -1, -1));

        materials_choose_label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        materials_choose_label.setText("Materials Used");
        view_details_panel.add(materials_choose_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 350, -1, -1));

        total_cost_txt.setEditable(false);
        total_cost_txt.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        total_cost_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                total_cost_txtActionPerformed(evt);
            }
        });
        view_details_panel.add(total_cost_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 490, 300, 30));

        reset_btn.setBackground(new java.awt.Color(255, 153, 0));
        reset_btn.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        reset_btn.setText("Update");
        reset_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reset_btnActionPerformed(evt);
            }
        });
        view_details_panel.add(reset_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 640, 115, -1));

        add_btn.setBackground(new java.awt.Color(255, 153, 0));
        add_btn.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        add_btn.setText("Add");
        add_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                add_btnActionPerformed(evt);
            }
        });
        view_details_panel.add(add_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 640, 115, -1));

        delete_btn.setBackground(new java.awt.Color(255, 153, 0));
        delete_btn.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        delete_btn.setText("Delete");
        delete_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delete_btnActionPerformed(evt);
            }
        });
        view_details_panel.add(delete_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 640, 115, -1));

        quantity_txt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                quantity_txtFocusLost(evt);
            }
        });
        quantity_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quantity_txtActionPerformed(evt);
            }
        });
        view_details_panel.add(quantity_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 260, 300, 30));

        id_label.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        id_label.setText("ID");
        view_details_panel.add(id_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        id_txt.setEditable(false);
        id_txt.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        id_txt.setEnabled(false);
        view_details_panel.add(id_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 10, 90, 30));

        due_date_label.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        due_date_label.setText("Date");
        view_details_panel.add(due_date_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 10, -1, -1));

        date_txt.setEditable(false);
        date_txt.setFont(new java.awt.Font("Segoe UI Black", 1, 18)); // NOI18N
        date_txt.setEnabled(false);
        view_details_panel.add(date_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 10, 150, 30));

        product_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-----Select product-----" }));
        product_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                product_cmbActionPerformed(evt);
            }
        });
        product_cmb.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                product_cmbPropertyChange(evt);
            }
        });
        view_details_panel.add(product_cmb, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 190, 140, 30));

        product_txt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                product_txtFocusLost(evt);
            }
        });
        product_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                product_txtActionPerformed(evt);
            }
        });
        view_details_panel.add(product_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 190, 300, 30));

        brand_label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        brand_label.setText("Brand Name");
        view_details_panel.add(brand_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, -1, -1));

        brand_txt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                brand_txtFocusLost(evt);
            }
        });
        brand_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brand_txtActionPerformed(evt);
            }
        });
        view_details_panel.add(brand_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 120, 300, 30));

        brand_cmb.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-----Select Brand-----" }));
        brand_cmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brand_cmbActionPerformed(evt);
            }
        });
        view_details_panel.add(brand_cmb, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 120, 140, 30));

        quantity_label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        quantity_label.setText("Quantity");
        view_details_panel.add(quantity_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, -1, -1));

        total_price_label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        total_price_label.setText("Total Cost used(in Rs.)");
        view_details_panel.add(total_price_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 490, -1, -1));

        sell_price_txt.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        sell_price_txt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sell_price_txtActionPerformed(evt);
            }
        });
        view_details_panel.add(sell_price_txt, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 560, 300, 30));

        sell_price_label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        sell_price_label.setText("Selling Price(in Rs.)");
        view_details_panel.add(sell_price_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 560, -1, -1));

        materials_list.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                materials_listMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(materials_list);

        view_details_panel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 310, 300, 100));

        jScrollPane2.setViewportView(mat_final_list);

        view_details_panel.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 310, 150, 100));

        evaluate_btn.setBackground(new java.awt.Color(255, 153, 0));
        evaluate_btn.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        evaluate_btn.setText("Evaluate");
        evaluate_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                evaluate_btnActionPerformed(evt);
            }
        });
        view_details_panel.add(evaluate_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 430, 115, -1));

        clear_btn.setBackground(new java.awt.Color(255, 153, 0));
        clear_btn.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        clear_btn.setText("Clear List");
        clear_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clear_btnActionPerformed(evt);
            }
        });
        view_details_panel.add(clear_btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 430, 115, -1));

        reset_btn1.setBackground(new java.awt.Color(255, 153, 0));
        reset_btn1.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        reset_btn1.setText("Reset");
        reset_btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reset_btn1ActionPerformed(evt);
            }
        });
        view_details_panel.add(reset_btn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 640, 115, -1));

        view_sale_panel.add(view_details_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 1300, 700));

        add_stock_label.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        add_stock_label.setForeground(new java.awt.Color(255, 255, 255));
        add_stock_label.setText("ADD PRODUCT DETAILS");
        view_sale_panel.add(add_stock_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 30, -1, -1));

        getContentPane().add(view_sale_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1350, 840));

        pack();
    }// </editor-fold>//GEN-END:initComponents
public void display_data()
{
    DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("dd/MMM/YYYY");
        LocalDate localDate = LocalDate.now();
        String todayDate = dtf1.format(localDate);
        date_txt.setText(todayDate);

    Vector h = new Vector();
           Vector d = new Vector();
           h.add("S.No.");
            h.add("Brand_name");
            h.add("Name");
            
            h.add("Qty");
           //h.add("M.Unit");
            h.add("Price");
            //h.add("Price Unit");
            h.add("materials used");
            h.add("Calculated price");
            h.add("Date");
            
            //row=0;
            String query="select * from export_item";
              String query1="select * from product_brand where brand_id=?";
        try {
            PreparedStatement ps=con.prepareStatement(query);
              ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                Vector d1=new Vector();
                
                d1.add(rs.getString("serial_id"));
    
            PreparedStatement ps1=con.prepareStatement(query1);
            ps1.setString(1,rs.getString("brand_id"));
              ResultSet rs1=ps1.executeQuery();
              while(rs1.next())
              {
                 d1.add(rs1.getString("brand_name"));
              }
                d1.add(rs.getString("product_name"));
                d1.add(rs.getString("product_quantity"));
                d1.add(rs.getString("product_price"));
                //d1.add(rs.getString("material_qty_unit"));
                d1.add(rs.getString("materials_used"));
                //d1.add(rs.getString("material_price_unit"));
                d1.add(rs.getInt("calculated_price"));
                
                d.add(d1);
                DefaultTableModel dtf=new DefaultTableModel(d,h);
            view_product_table.setModel(dtf);
            }
        }catch (SQLException ex) {
            Logger.getLogger(Add_Product.class.getName()).log(Level.SEVERE, null, ex);
        }
}
    private void view_product_tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_view_product_tableMouseClicked
        // TODO add your handling code here:
        //quantity_spinner.setValue(0);
       int row = view_product_table.getSelectedRow();
        TableModel tm = view_product_table.getModel();
        String serial_id = (String) tm.getValueAt(row, 0);
        id_txt.setText(serial_id);
        brand_txt.setText((String) tm.getValueAt(row, 1));
        brand_cmb.setSelectedItem((String) tm.getValueAt(row, 1));
        
        product_txt.setText((String) tm.getValueAt(row, 2));
        product_cmb.setSelectedItem((String) tm.getValueAt(row, 2));
        quantity_txt.setText((String) tm.getValueAt(row, 3));
         sell_price_txt.setText((String) tm.getValueAt(row, 4));
       String enter=(String)tm.getValueAt(row, 5);
        listModel1.clear();
       try
       {
       String put[]=enter.split(",");
      
       for(int i=0;i<put.length;i++)
       {
           listModel1.addElement(put[i]);
       }
       
       }
       catch(Exception ex)
       {
           
       }
       mat_final_list.setModel(listModel1);
        total_cost_txt.setText(String.valueOf(tm.getValueAt(row, 6)));
        mat_used_qty.sum=(int)tm.getValueAt(row, 6);
        //select_data();
        //total_amount_txt.setText((String) tm.getValueAt(row,5));
    }//GEN-LAST:event_view_product_tableMouseClicked

    private void total_cost_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_total_cost_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_total_cost_txtActionPerformed

    private void reset_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reset_btnActionPerformed
        // TODO add your handling code here:
if(!found_category)
        {
            category_add();
        }
        update_export_data();
        reset_fields();
         JOptionPane.showMessageDialog(this, "Data updated successfully!");
    }//GEN-LAST:event_reset_btnActionPerformed
public void add_export_item()
{
    String query="insert into export_item(brand_id,product_name,product_quantity,product_price,materials_used,calculated_price,date_entry) values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps=con.prepareStatement(query);
            ps.setString(1,brand_id_add);
            ps.setString(2,product_txt.getText());
            ps.setString(3,quantity_txt.getText());
            ps.setString(4,sell_price_txt.getText());
            String material_all_list="";
            for(int i=0;i<listModel1.getSize();i++)
            {
                 material_all_list+=(String) listModel1.get(i) + ",";
            }
            ps.setString(5,material_all_list);
             ps.setInt(6,Integer.parseInt(total_cost_txt.getText()));
             ps.setString(7,date_txt.getText());
            ps.executeUpdate();
            ps.close();
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Add_Product.class.getName()).log(Level.SEVERE, null, ex);
        }
    
}
public void update_stock_details()
{/*
    String data="";
    for(int i=0;i<listModel1.getSize();i++)
    {
    data+=listModel1.get(i)+",";
    }
    System.out.println(data);
    String split1[]=data.split(",");
    
     for(int o=0;o<split1.length;o++)
    {
    System.out.println(split1[o]);
    }
    ArrayList<String> split3=new ArrayList<>();
     ArrayList<String> split4=new ArrayList<>();
    System.out.println(split1.length);
    int m=1;
    int n=0;
    for(int j=0;j<split1.length;j++)
    {
        String split2[]=split1[j].split("---");
          System.out.println(split2.length);
          for(int o=0;o<split1.length;o++)
    {
        System.out.print(split2[o]);
    }
        split3.add(split2[m]);
        split4.add(split2[n]);
        
    }
    for(int o=0;o<split3.size();o++)
    {
    System.out.println(split3.get(o));
    }
    String query = "update stock_entry set material_qty=? where serial_id=?";
    
    String query1 = "select * from stock_entry where material_name=?";
           
    try {
        for(int k=0;k<split3.size();k++)
        {
                PreparedStatement ps = con.prepareStatement(query);
                PreparedStatement ps1 = con.prepareStatement(query1);
                ps1.setString(1,split4.get(k));
                ResultSet rs =ps1.executeQuery();
                String mat_qty="";
                if(rs.next())
                {
                    mat_qty=rs.getString("material_qty");
                }
                System.out.println("pass"+k);
                ps.setString(1,String.valueOf(Integer.parseInt(mat_qty)-Integer.parseInt(split3.get(k))));
                
              ps.executeUpdate();
                
                ps.close();
                ps1.close();
        }
            } catch (SQLException ex) {
                Logger.getLogger(Add_Customer.class.getName()).log(Level.SEVERE, null, ex);
            }
    */
}
    private void add_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_add_btnActionPerformed
        // TODO add your handling code here:
        if(!found_category)
        {
            category_add();
        }
        add_export_item();
        update_stock_details();
        reset_fields();
        JOptionPane.showMessageDialog(this,"Data inserted successfullt!");
        exim++;
        id_txt.setText("EXIM"+exim);
    }//GEN-LAST:event_add_btnActionPerformed
public void delete_export_data()
{
     String query="delete from export_item where serial_id=?";
        try {
            PreparedStatement ps=con.prepareStatement(query);
           
               ps.setString(1,id_txt.getText());
            ps.executeUpdate();
            ps.close();
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Add_Product.class.getName()).log(Level.SEVERE, null, ex);
        }
}
public void reset_fields()
{
    id_txt.setText("EXIM"+exim);
    display_data();
    brand_txt.setText("");
    product_txt.setText("");
    quantity_txt.setText("");
    total_cost_txt.setText("");
    sell_price_txt.setText("");
    listModel1.clear();
    mat_final_list.setModel(listModel1);
}
    private void delete_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delete_btnActionPerformed
        // TODO add your handling code here:

        delete_export_data();
        reset_fields();
        JOptionPane.showMessageDialog(this, "Data deleted successfully!");

    }//GEN-LAST:event_delete_btnActionPerformed
public void update_export_data()
{
     String query="update export_item set brand_id=?,product_name=?,product_quantity=?,product_price=?,materials_used=?,calculated_price=? date_entry=? where serial_id=?";
        try {
            PreparedStatement ps=con.prepareStatement(query);
            ps.setString(1,brand_id_add);
            ps.setString(2,product_txt.getText());
            ps.setString(3,quantity_txt.getText());
            ps.setString(4,sell_price_txt.getText());
            String material_all_list="";
            for(int i=0;i<listModel1.getSize();i++)
            {
                 material_all_list+=(String) listModel1.get(i) + ",";
            }
            ps.setString(5,material_all_list);
             ps.setInt(6,Integer.parseInt(total_cost_txt.getText()));
             ps.setString(7,date_txt.getText());  
             ps.setString(8,id_txt.getText());
               
            ps.executeUpdate();
            ps.close();
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Add_Product.class.getName()).log(Level.SEVERE, null, ex);
        }
}
    public void material_list_show()
{
     if(!quantity_txt.getText().matches(""))
        {
     String query="select * from stock_entry";
            DefaultListModel listModel;
            listModel = new DefaultListModel();
            material_id.clear();
            material_price.clear();
            
            try {
                PreparedStatement ps=con.prepareStatement(query);
                ResultSet rs=ps.executeQuery();
                while(rs.next())
                {
                    material_id.add(rs.getString("material_qty"));
                   listModel.addElement(rs.getString("material_name")); 
                   material_price.add(rs.getString("material_price"));
                }
                materials_list.setModel(listModel);
            } catch (SQLException ex) {
                Logger.getLogger(Add_Product.class.getName()).log(Level.SEVERE, null, ex);
            }
        }  
        
}
    private void quantity_txtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_quantity_txtFocusLost
        // TODO add your handling code here:
        
       
            material_list_show();
        
        
    }//GEN-LAST:event_quantity_txtFocusLost

    private void quantity_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quantity_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_quantity_txtActionPerformed
public void category_add()
{
  String query = "insert into product_brand(brand_name) values(?)";
  String query1 = "select brand_id from product_brand where brand_name=?";
           
    try {
                PreparedStatement ps = con.prepareStatement(query);
                PreparedStatement ps1 = con.prepareStatement(query1);
                ps.setString(1,brand_name);
                ps1.setString(1,brand_name);
                ps.executeUpdate();
                
                ps.close();
                ResultSet rs=ps1.executeQuery();
                if(rs.next())
                {
                    brand_id_add=rs.getString("brand_id");
                }
                ps1.close();
               
}       catch (SQLException ex) {
            Logger.getLogger(Add_stock.class.getName()).log(Level.SEVERE, null, ex);
        }
}
    private void product_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_product_cmbActionPerformed
        // TODO add your handling code here:

        //quantity_cmb.removeAllItems();
        //quantity_cmb.addItem("-----Select Quantity-----");
        // System.out.println("clicked");
      /*  quantity_txt.setText("");
        product_name = (String) product_cmb.getSelectedItem();

        if (product_cmb.getSelectedIndex() > 0) {
            product_txt.setText(product_name);
            quantity_spinner.setEnabled(true);
            String query = "select * from export_item where product_name=?";
            try {
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, product_name);
                ResultSet rs = ps.executeQuery();
                total_quantity = 0;
                //int total_quantity=0;
                //int price = 0;
                price = 0;
                while (rs.next()) {
                    total_quantity = rs.getInt("product_quantity");
                    price = rs.getInt("product_price");

                }

                //System.out.println(quantity_minimum+"ok");
                if (product_cmb.getSelectedItem().equals(product_select)) {
                    select_data();
                } else {
                    qty_purchased = 0;
                }
                mode_cmb.setEnabled(true);
                payment_txt.setEditable(true);
                payment_txt.setEnabled(true);
                updated_mode_cmb.setEnabled(true);
                quantity_spinner.setModel(new javax.swing.SpinnerNumberModel(0, -(qty_purchased - 1), total_quantity, 1));
                //SpinnerModel sm = new SpinnerNumberModel(0,-10,total_quantity,1); //default value,lower bound,upper bound,increment by
                // quantity_spinner = new JSpinner(sm);
                //String output=NumberFormat.getInstance().format(price);
                //System.out.println(output);
                if (conversion) {
                    price_txt.setText(Number_converter.convertToIndianCurrency(String.valueOf(price)));
                } else {
                    price_txt.setText(NumberFormat.getInstance().format(price));
                }
                price_txt.setText(Number_converter.convertToIndianCurrency(String.valueOf(price)));

            } catch (SQLException ex) {
                Logger.getLogger(Add_Sales.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            quantity_txt.setText("");
            price_txt.setText("");
            payment_txt.setText("");
        }
        if (quantity_txt.getText().matches("")) {
            total_amount_txt.setText("");

            payment_txt.setEnabled(false);
        }*/
    }//GEN-LAST:event_product_cmbActionPerformed

    private void product_cmbPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_product_cmbPropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_product_cmbPropertyChange

    private void product_txtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_product_txtFocusLost
        // TODO add your handling code here:
        /*boolean found_product = false;
        product_name = "";
        product_name = product_txt.getText();
        if (!product_name.isEmpty()) {
            for (int check = 0; check < product_cmb.getItemCount(); check++) {
                if (product_name.equalsIgnoreCase(product_cmb.getItemAt(check))) {
                    found_product = true;
                    product_cmb.setSelectedIndex(check);
                }
            }
            if (found_product) {
                System.out.println("found");

            } else {
                System.out.println(" Not found");
                brand_items();
                JOptionPane.showMessageDialog(this, "No Matching Item found!\nPlease refer Drop Down Menu...");

            }
        }*/
    }//GEN-LAST:event_product_txtFocusLost

    private void product_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_product_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_product_txtActionPerformed

    private void brand_txtFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_brand_txtFocusLost
        // TODO add your handling code here:
      found_category=false;
         if(!brand_txt.getText().trim().matches(""))
        {
            for(int check=1;check<brand_cmb.getItemCount();check++)
            {
                if(brand_txt.getText().equalsIgnoreCase(brand_cmb.getItemAt(check)))
                {
                    found_category=true;
                    
                    brand_cmb.setSelectedIndex(check);
                    brand_id_add=brand_id.get(check-1);
                }
            }
            if(!found_category)
            {
        
        brand_name=brand_txt.getText();
        
        
            }
        }
         else
        {
            brand_cmb.setSelectedIndex(0);
        }
    }//GEN-LAST:event_brand_txtFocusLost

    private void brand_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brand_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_brand_txtActionPerformed

    private void brand_cmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brand_cmbActionPerformed
        // TODO add your handling code here:
        String brand_name="";
        product_cmb.removeAllItems();
        product_cmb.addItem("-----Select Product-----");
        
        if (brand_cmb.getSelectedIndex() > 0) {
            //found_category=true;
            brand_name = (String) brand_cmb.getSelectedItem();
            brand_txt.setText(brand_name);
            String brand_id_get="";     
        brand_id_get = brand_id.get(brand_cmb.getSelectedIndex() - 1);
            
            String query = "select * from export_item where brand_id=?";
                brand_id_add=brand_id_get;
            try {
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, brand_id_get);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    product_cmb.addItem(rs.getString("product_name"));

                }

            } catch (SQLException ex) {
                Logger.getLogger(Add_Sales.class.getName()).log(Level.SEVERE, null, ex);
            }
     
        }
    }//GEN-LAST:event_brand_cmbActionPerformed

    private void sell_price_txtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sell_price_txtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sell_price_txtActionPerformed

    private void materials_listMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_materials_listMouseClicked
        // TODO add your handling code here:
 //material_list_show();
        if(materials_list.getSelectedIndex()>=0)
     {
     materials_selected=materials_list.getSelectedValue();
     material_name.add(materials_list.getSelectedValue());
     int price=materials_list.getSelectedIndex();
     mat_used_qty obj=new mat_used_qty(Integer.parseInt(material_id.get(materials_list.getSelectedIndex())),materials_selected,price,material_price);
     obj.setVisible(true);
     
     mat_final_list.setModel(listModel1);
     
     
     }
    }//GEN-LAST:event_materials_listMouseClicked

    private void evaluate_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_evaluate_btnActionPerformed
        // TODO add your handling code here:
        System.out.println(mat_used_qty.sum);
        total_cost_txt.setText(String.valueOf(mat_used_qty.sum));
        
        
    }//GEN-LAST:event_evaluate_btnActionPerformed

    private void clear_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clear_btnActionPerformed
        // TODO add your handling code here:
        
        listModel1.clear();
        material_list_show();
        mat_used_qty.sum=0;
        total_cost_txt.setText("");
        
    }//GEN-LAST:event_clear_btnActionPerformed

    private void reset_btn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reset_btn1ActionPerformed
reset_fields();
        // TODO add your handling code here:
    }//GEN-LAST:event_reset_btn1ActionPerformed
    ArrayList<String> brand_id=new ArrayList<>();
     ArrayList<String> material_id=new ArrayList<>();
       ArrayList<String> material_name=new ArrayList<>();
     static  ArrayList<String> material_price=new ArrayList<>();
     static String materials_selected="";
static DefaultListModel listModel1 = new DefaultListModel();
boolean found_category=false;
static int exim=101;
String brand_name="",brand_id_add="";
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton add_btn;
    private javax.swing.JLabel add_stock_label;
    public javax.swing.JComboBox<String> brand_cmb;
    private javax.swing.JLabel brand_label;
    public javax.swing.JTextField brand_txt;
    private javax.swing.JButton clear_btn;
    private javax.swing.JTextField date_txt;
    private javax.swing.JButton delete_btn;
    private javax.swing.JLabel due_date_label;
    private javax.swing.JButton evaluate_btn;
    private javax.swing.JLabel id_label;
    private javax.swing.JTextField id_txt;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList<String> mat_final_list;
    private javax.swing.JLabel materials_choose_label;
    private javax.swing.JList<String> materials_list;
    private javax.swing.JComboBox<String> product_cmb;
    private javax.swing.JLabel product_label;
    private javax.swing.JTextField product_txt;
    private javax.swing.JLabel quantity_label;
    private javax.swing.JTextField quantity_txt;
    private javax.swing.JButton reset_btn;
    private javax.swing.JButton reset_btn1;
    private javax.swing.JLabel sell_price_label;
    private javax.swing.JTextField sell_price_txt;
    private javax.swing.JTextField total_cost_txt;
    private javax.swing.JLabel total_price_label;
    private javax.swing.JPanel view_details_panel;
    private javax.swing.JTable view_product_table;
    private javax.swing.JPanel view_sale_panel;
    private javax.swing.JScrollPane view_sale_scrollpane;
    // End of variables declaration//GEN-END:variables
}
