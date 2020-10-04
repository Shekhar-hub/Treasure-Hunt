
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author DELL
 */
class DrawLine1 extends JPanel {
    int l;
    public DrawLine1(int l)
    {
        this.l=l;
    }
	
	
	
	protected void paintComponent(Graphics g) {
                 Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(10));
		g2.setColor( Color.DARK_GRAY);
		// X Start, Y Start, X End, Y End
		// X = <---------->
		g2.drawLine (1,0,1,l+180);
 
	}
}
public class Assistant_login extends javax.swing.JFrame {

    /**
     * Creates new form Assistant_login
     */
    Connection con;
    public Assistant_login(Connection con) {
        //this.setLayout(new GridLayout());
        initComponents();
        this.con=con;
        
        //welcome_panel.setLayout(new RelativeLayout(RelativeLayout.X_AXIS, 10));
        //setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        
        
        
        //getRootPane().setBorder(BorderFactory.createMatteBorder(4, 4, 4, 4, Color.RED));
        //view_panel.setVisible(true);
       // customer_panel.setVisible(false);
       
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jframe_panel = new javax.swing.JPanel();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        welcome_options_panel = new javax.swing.JPanel();
        sales_panel = new javax.swing.JPanel();
        sales_label = new javax.swing.JLabel();
        welcome_label = new javax.swing.JLabel();
        customers_panel = new javax.swing.JPanel();
        customers_label = new javax.swing.JLabel();
        view_sales_panel = new javax.swing.JPanel();
        view_sales_label = new javax.swing.JLabel();
        red_title_panel = new javax.swing.JPanel();
        exit_label = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1626, 868));
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jframe_panel.setPreferredSize(new java.awt.Dimension(1626, 700));
        jframe_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jframe_panel.add(jDesktopPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 30, 1350, 840));

        welcome_options_panel.setBackground(new java.awt.Color(102, 0, 102));
        welcome_options_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sales_panel.setBackground(new java.awt.Color(102, 0, 102));
        sales_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sales_label.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        sales_label.setForeground(new java.awt.Color(255, 255, 255));
        sales_label.setText("Sales");
        sales_label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sales_labelMouseClicked(evt);
            }
        });
        sales_panel.add(sales_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        welcome_options_panel.add(sales_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 410, 290, 70));

        welcome_label.setFont(new java.awt.Font("Segoe UI Black", 1, 48)); // NOI18N
        welcome_label.setForeground(new java.awt.Color(255, 255, 255));
        welcome_label.setText("Welcome");
        welcome_options_panel.add(welcome_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, -1, -1));

        customers_panel.setBackground(new java.awt.Color(102, 0, 102));
        customers_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        customers_label.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        customers_label.setForeground(new java.awt.Color(255, 255, 255));
        customers_label.setText("Customers");
        customers_label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                customers_labelMouseClicked(evt);
            }
        });
        customers_panel.add(customers_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        welcome_options_panel.add(customers_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 270, 290, 70));

        view_sales_panel.setBackground(new java.awt.Color(102, 0, 102));
        view_sales_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        view_sales_label.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        view_sales_label.setForeground(new java.awt.Color(255, 255, 255));
        view_sales_label.setText("View Sales");
        view_sales_label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                view_sales_labelMouseClicked(evt);
            }
        });
        view_sales_panel.add(view_sales_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        welcome_options_panel.add(view_sales_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 550, 290, 70));

        jframe_panel.add(welcome_options_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 290, 870));

        red_title_panel.setBackground(new java.awt.Color(255, 0, 51));
        red_title_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        exit_label.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        exit_label.setText("Log-Out");
        exit_label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                exit_labelMouseClicked(evt);
            }
        });
        red_title_panel.add(exit_label, new org.netbeans.lib.awtextra.AbsoluteConstraints(1550, 0, 80, 30));

        jframe_panel.add(red_title_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1630, 30));

        getContentPane().add(jframe_panel);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void sales_labelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sales_labelMouseClicked
        // TODO add your handling code here:
       customers_panel.setBackground(new Color(102,0,102));
        sales_panel.setBackground(new Color(255,0,0));
        view_sales_panel.setBackground(new Color(102,0,102));
        jDesktopPane1.removeAll();
        Add_Sales add_Sales=new Add_Sales(con);
        add_Sales.setVisible(true);
        ((javax.swing.plaf.basic.BasicInternalFrameUI) add_Sales.getUI()).setNorthPane(null);

        add_Sales.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
          jDesktopPane1.add(add_Sales);
          //jDesktopPane1.setSelectedFrame(customer_options);
          //jDesktopPane1.show();
          
        
        
    }//GEN-LAST:event_sales_labelMouseClicked

    private void exit_labelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_exit_labelMouseClicked
        try {
            // TODO add your handling code here:
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Assistant_login.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
        
    }//GEN-LAST:event_exit_labelMouseClicked

    private void customers_labelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_customers_labelMouseClicked
        // TODO add your handling code here:
        customers_panel.setBackground(new Color(255,0,0));
        sales_panel.setBackground(new Color(102,0,102));
        view_sales_panel.setBackground(new Color(102,0,102));
        jDesktopPane1.removeAll();
        Add_Customer add_Customer=new Add_Customer(con);
        add_Customer.setVisible(true);
        ((javax.swing.plaf.basic.BasicInternalFrameUI) add_Customer.getUI()).setNorthPane(null);

        add_Customer.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
          jDesktopPane1.add(add_Customer);
          //jDesktopPane1.setSelectedFrame(customer_options);
          //jDesktopPane1.show();
        
    }//GEN-LAST:event_customers_labelMouseClicked

    private void view_sales_labelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_view_sales_labelMouseClicked
        // TODO add your handling code here:
         customers_panel.setBackground(new Color(102,0,102));
        sales_panel.setBackground(new Color(102,0,102));
        view_sales_panel.setBackground(new Color(255,0,0));
        jDesktopPane1.removeAll();
        View_sales view_sales=new View_sales(con);
        view_sales.setVisible(true);
        ((javax.swing.plaf.basic.BasicInternalFrameUI) view_sales.getUI()).setNorthPane(null);

        view_sales.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
          jDesktopPane1.add(view_sales);
    }//GEN-LAST:event_view_sales_labelMouseClicked
public void category()
{
//     Category category=new Category(x);
//        category.setVisible(true);
//        //obj.setSize(430, 290);
//        //jDesktopPane1.add(category);
}
public void add_customer()
{
//     Add_Customer add_customer=new Add_Customer();
//        add_customer.setVisible(true);
//        // jDesktopPane1.add(add_customer);
}
    /**
     * @param args the command line arguments
     */
    
int l=0;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel customers_label;
    private javax.swing.JPanel customers_panel;
    private javax.swing.JLabel exit_label;
    public static javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JPanel jframe_panel;
    private javax.swing.JPanel red_title_panel;
    private javax.swing.JLabel sales_label;
    private javax.swing.JPanel sales_panel;
    private javax.swing.JLabel view_sales_label;
    private javax.swing.JPanel view_sales_panel;
    private javax.swing.JLabel welcome_label;
    private javax.swing.JPanel welcome_options_panel;
    // End of variables declaration//GEN-END:variables
}

