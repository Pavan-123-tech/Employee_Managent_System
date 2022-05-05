/* Employee Managment System */
//Developed By Pawan Rameshwar Raut


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import javax.swing.table.*;
import javax.swing.event.*;

class InsertFrame extends JInternalFrame implements ActionListener
{   
	private JLabel lbl,lbl_Message;
	private JTextField txt[];
	private JButton b;
	public InsertFrame()
	{   
		super("ADD NEW_EMPLOYEE",true, true, true, true);
        this.setLayout(new GridLayout(6,2));
        this.setForeground(Color.BLACK);

        String str[]={"Employee number","Employee Name","Job","Salary"};
        txt = new JTextField[4];
       
       for(int i=0;i<txt.length;i++)
       {
        lbl=new JLabel(str[i]);
        this.add(lbl);

        txt[i]= new JTextField(); 
        this.add(txt[i]);
       }

       String arr[]={"Insert","Clear"};
       for(int i=0;i<arr.length;i++)
       {
       b=new JButton(arr[i]);
       this.add(b);
       b.addActionListener(this);
      }

      lbl_Message=new JLabel("...");
      this.add(lbl_Message);
      lbl_Message.setForeground(Color.BLUE);
      lbl_Message.setFont(new Font("Gabriola", Font.BOLD,20));

		this.setVisible(true);
		this.setSize(500,400);
        
	}
	public void actionPerformed(ActionEvent e)
	{
      String cap=e.getActionCommand();

      if(cap.equalsIgnoreCase("Insert"))
      {   
      	 try
      	 {
            
            int eno= Integer.parseInt(txt[0].getText());
            String enm=txt[1].getText().toUpperCase();
            String j= txt[2].getText().toUpperCase();
            double s=Double.parseDouble(txt[3].getText());

            String sql="insert into employee values(?,?,?,?)";
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL","System","123456789");

            PreparedStatement ps= con.prepareStatement(sql);

        ps.setInt(1 , eno);
        ps.setString(2 , enm);
        ps.setString(3 , j);
        ps.setDouble(4, s);

        int n =ps.executeUpdate();
        ps.close();
        con.close();

         if(n==1)
         {
        	lbl_Message.setForeground(Color.GREEN);
           JOptionPane.showMessageDialog(this,"Succesful...");
       
        }
        else
        	lbl_Message.setForeground(Color.RED);
            

      	 }
      	 catch(Exception ex)
      	 {
      	 	lbl_Message.setText(ex.getMessage());
      	 	ex.printStackTrace();

      	 }

      }
      else
      	if(cap.equalsIgnoreCase("Clear"))
      	{   
      		for(int i=0;i<txt.length;i++)
      		{
      		txt[i].setText("");
      		lbl_Message.setText("");

      	    }
      	}
	}
}
class SearchUpdateDeleteFrame extends JInternalFrame implements ActionListener, ListSelectionListener 
{

    private JSplitPane split_pane;
    private JPanel panel1,panel2;
    private JLabel lbl, lbl_Message;
    private JTextField txt_search, txt[];
    private JList <String>emp_list;
    private JButton [] b;
    private JScrollPane jsp;
    private static Connection con;

    public SearchUpdateDeleteFrame()
    {
        super("Search, Update, Delete",true, true,true,true);
               
               //panel-1
         panel1=new JPanel();
         this.add(panel1);
        panel1.setLayout(null);

        
        lbl= new JLabel("Enter Employee Name to Search");
        lbl.setBounds(10,10,240,30);
        panel1.add(lbl);

        txt_search= new JTextField(20);
        txt_search.setBounds(10,45,240,30);
        panel1.add(txt_search);
        txt_search.addActionListener(this);

       emp_list= new JList <String>();
       panel1.add(emp_list);
       jsp=new JScrollPane(emp_list);
       jsp.setBounds(10,80,240,150);
       panel1.add(jsp);
       getTableData();

       emp_list.addListSelectionListener(this);


       //panel- 2
       panel2= new JPanel();
       this.add(panel2);

       panel2.setLayout(new GridLayout(6,2));

       String str[]={"Employee Number","Employee Name", "Job", "Salary"};
       txt= new JTextField[4];
     for(int i=0; i<str.length;i++)
     {
       lbl=new JLabel(str[i]);
       panel2.add(lbl);

       txt[i]= new JTextField();
       panel2.add(txt[i]);
       txt[i].setFont(new Font("Calibri",Font.BOLD,20));
   }
     txt[0].setEditable(false);

       String arr[]={"Update", "Delete"};
       b=new JButton[2];
       for(int i=0;i<arr.length;i++)
       {
        b[i]=new JButton(arr[i]);
        panel2.add(b[i]);
        b[i].addActionListener(this);
       }

       lbl_Message= new JLabel("...");
       panel2.add(lbl_Message);
       lbl_Message.setForeground(Color.GREEN);
       lbl_Message.setFont(new Font("Calibri",Font.BOLD,20));

       //Split_Pane

       split_pane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel1, panel2);

       split_pane.setDividerLocation(200);
       split_pane.setDividerSize(1);
       this.add(split_pane);

        this.setSize(610,350);
        this.setVisible(true);
    }
    public void connect() throws Exception
    {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL","system","123456789");
        


    }
    public void disconnect() throws Exception
    {
        if(!con.isClosed())
            con.close();
    }
    public void actionPerformed(ActionEvent e)
    {
      try
      {
        Object obj= e.getSource();
      
        if(obj==b[0])
        {
            //Update Button

          int eno=Integer.parseInt(txt[0].getText());
          String enm= txt[1].getText().toUpperCase();
          String j=txt[2].getText().toUpperCase();
          double sal= Double.parseDouble(txt[3].getText());

          String sql="update employee set ename=?,job=?, salary=? where empno=?";
          connect();
          PreparedStatement ps=con.prepareStatement(sql);
          ps.setString(1,enm);
          ps.setString(2,j);
          ps.setDouble(3,sal);
          ps.setInt(4,eno);

          int n= ps.executeUpdate();

          ps.close();
          disconnect();

          if(n == 1){
            getTableData();

            lbl_Message.setText("Record Updated...");
          }
           else
           {
             lbl_Message.setText("Record Not Updated...");
           }

        }
        else
         if(obj==b[1])
         {
           // Delete

            int eno=Integer.parseInt(txt[0].getText());
            String sql="delete employee where empno=?";
            connect();
            PreparedStatement ps=con.prepareStatement(sql);
            ps.setInt(1, eno);
            int n=ps.executeUpdate();
            ps.close();
            disconnect();

            if (n== 1)
            {
                getTableData();
                for(int i=0; i<txt.length;i++)
                {
                    txt[i].setText("");
                    lbl_Message.setText("");
                    JOptionPane.showMessageDialog(this, "Record deleted...");
                }

            // else                        
             //JOptionPane.showMessageDialog(this, "Record not deleted...");
            
            }

         }
        else
         if(obj== txt_search)
         {
           String search_text= txt_search.getText().toUpperCase();

           String sql="select ename from employee where ename like '" +search_text+"%'";
           connect();
           PreparedStatement ps=con.prepareStatement(sql);
           ResultSet rs=ps.executeQuery();

           Vector <String> v= new Vector <String> ();

           while(rs.next())
           {
             String enm= rs.getString("ename");
             v.add(enm);
           }
           rs.close();
           ps.close();
           disconnect();
           emp_list.setListData(v);
         }

      }
      catch(Exception ex)
      {
        JOptionPane.showMessageDialog(this, ex.getMessage());
        ex.printStackTrace();
      }
    }

    @Override
    public void valueChanged(ListSelectionEvent e )
    {
      try
      {
         String enm= (String) emp_list.getSelectedValue();
         String sql=" select empno,job,salary from employee where ename=?";

         connect();
         PreparedStatement ps= con.prepareStatement(sql);
         ps.setString(1,enm);
         ResultSet rs=ps.executeQuery();

         if(rs.next())
         {   
             int eno=rs.getInt("empno");
             
             String j=rs.getString("job");
             double sal=rs.getDouble("salary");

             txt[0].setText(eno+"");
             txt[1].setText(enm);
             txt[2].setText(j);
             txt[3].setText(sal+"");          

         }

             rs.close();
             ps.close();
             disconnect();

      }
      catch(Exception ex)
      {
        lbl_Message.setText(ex.getMessage());
        ex.printStackTrace();
      }
    }

    public void getTableData()
    {
        try
        {
             String sql="select ename from employee";
             connect();
             PreparedStatement ps=con.prepareStatement(sql);
             ResultSet rs=ps.executeQuery();

             Vector <String> v= new Vector <String>();

             while(rs.next())
             {
                String enm=rs.getString("ename");
                v.add(enm);
             }
             rs.close();
             ps.close();
             disconnect();

             emp_list.setListData(v);



        }
        catch(Exception ex)
        {
            lbl_Message.setText(ex.getMessage());
            ex.printStackTrace();

        }
    }
}

class AllEmployeesFrame extends JInternalFrame
{   
	private JTable tbl;
	
	public AllEmployeesFrame()
	{
		super("All Employees", true, true, true, true);

		Vector <String>col_names=new Vector <String>();
		String cols[]={"Employee Number","Employee Name","Job","Salary"};
        for(int i=0;i<cols.length;i++)
        {
		col_names.add(cols[i]);
	    }

	    Vector <Vector> rows= new Vector <Vector>(); 

	    try
	    {
	    	Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:ORCL","System","123456789");
            String sql="select * from employee";
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();

            while(rs.next())
            {
            	int eno=rs.getInt("empno");
            	String enm=rs.getString("ename");
            	String job=rs.getString("job");
            	double sal=rs.getDouble("salary");

            	Vector <String> single_row =new Vector <String>();

            	single_row.add(eno+"");
            	single_row.add(enm);
            	single_row.add(job);
            	single_row.add(sal+"");

            	rows.add(single_row);
            }
            rs.close();
            ps.close();
            con.close();

            tbl =new JTable(rows, col_names);
            JScrollPane jsp= JTable.createScrollPaneForTable(tbl);
            JTableHeader th=tbl.getTableHeader();
            th.setBackground(Color.CYAN);
            th.setForeground(Color.RED);
            this.add(jsp, BorderLayout.CENTER);


	    }
	    catch(Exception ex)
	    {
	    	JOptionPane.showMessageDialog(this, ex.getMessage());

	    }

		this.setSize(650,400);
		this.setVisible(true);
	}
}

class MyFrame extends JFrame implements ActionListener
{
    private JDesktopPane jdp;
    private JToolBar tbar;
    private JButton b[];
    private InsertFrame insert_frame;
    private AllEmployeesFrame all_employee_frame;
    //private JLabel pavan_image;
    private SearchUpdateDeleteFrame search_update_delete_frame;
	public MyFrame()
	{  
		super("Employee ManagementSystem");
		tbar=new JToolBar(JToolBar.HORIZONTAL);
		this.add(tbar, BorderLayout.NORTH);
		tbar.setFloatable(false);
		tbar.setLayout(new GridLayout(1,3));

         try
         {
         String arr[]={"ADD New Employee","Search,Update,Delete","All Employees"};
         b=new JButton[3]; 
      
         for(int i=0;i<arr.length;i++)
      
    {
         b[i]= new JButton(arr[i]);
         b[i].addActionListener(this);
         tbar.add(b[i]);
         b[i].setBackground(Color.PINK);


     }
 }
 catch(Exception e)
 {
 	e.printStackTrace();
 }

        jdp=new JDesktopPane();
        this.add(jdp);

        /*pavan_image=new JLabel(new ImageIcon("images/pavan.jpeg"));
        this.add(pavan_image);
        pavan_image.setBounds(50,50,100,100);*/

        //JScrollPane jsp=new JScrollPane(pavan_image);
       // this.add(jsp);

		this.setVisible(true);
		this.setSize(900,900);
	}
	public void actionPerformed(ActionEvent e)
	{
         Object  obj=e.getSource();
		//String cap=e.getActionCommand();

          if(obj==b[0])//ADD New Employee
        // if(cap.equals("ADD New Employee"))
          {   
          	JInternalFrame arr[]=jdp.getAllFrames();
          		int c=0;
          		for(int i=0; i<arr.length;i++)
          			{
          				if(insert_frame==arr[i])
          				{
          					c++;
          					break;
          				}
	          	   }
          if(c==0)
          	insert_frame=null;
          		
          if(insert_frame==null)
          	{
              insert_frame=new InsertFrame();
              jdp.add(insert_frame);
              insert_frame.setBounds(10,10,300,250);
            }
            else
          	JOptionPane.showMessageDialog(this,"Frame already Opened...");
          }

          else
          if(obj==b[1]) //Search,Update,Delete
          //	if(cap.equals("Search,Update,Delete"))
          {
            JInternalFrame arr[]=jdp.getAllFrames();
                int c=0;
                for(int i=0; i<arr.length;i++)
                    {
                        if(search_update_delete_frame==arr[i])
                        {
                            c++;
                            break;
                        }
                    }
                    if(c==0)
                    
                    search_update_delete_frame=null;
                else
                    JOptionPane.showMessageDialog(this,"Frame already Opened...");
            if(search_update_delete_frame==null)
            {
                search_update_delete_frame= new SearchUpdateDeleteFrame();
                search_update_delete_frame.setBounds(350,10,600,280);
                jdp.add(search_update_delete_frame);
            }

          } 
          else
          	if(obj==b[2])//All Employees
          	//if(cap.equals("All Employees"))
          	{
          		JInternalFrame arr[]=jdp.getAllFrames();
          		int c=0;
          		for(int i=0; i<arr.length;i++)
          			{
          				if(all_employee_frame==arr[i])
          				{
          					c++;
          					break;
          				}
          			}
          			if(c==0)
          			
          			all_employee_frame=null;
          		
          			if(all_employee_frame==null)
          			{
          				all_employee_frame= new AllEmployeesFrame();
          				all_employee_frame.setBounds(10,300,500,300);
          				jdp.add(all_employee_frame);
          			}
          			else
          		    JOptionPane.showMessageDialog(this,"Frame already Opened...");
                         
                 }  
          	}
	}


class Project
{
	public static void main(String args[])
	{

	   MyFrame f=new MyFrame();
	}
}

/* 
Create this table  in the Oracle or sql Database:

create table employee
(

 empno number(4)primary key,
 ename varchar2(20),
 job varchar2(20),
 salary number(10,2)
 );
