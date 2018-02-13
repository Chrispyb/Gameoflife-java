import javax.swing.*;
import java.awt.*;
import javax.swing.JOptionPane;

import java.awt.event.*;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

/*
	Creators: Kevin Mack, Christian Brady
	Program: Conway's Game of Life
	Class: Advanced Java
	
	Program Description:
	
	Conway's Game of Life is a cellular automoton created by the mathematician John Horton Conway. 
	The Game consists of cells, each represented by a boolean value. If a cell has two or three neighbors,
	the cell lives on to the next cycle. If has has less than 2 or more than 3 neighbors, the cell dies.
	If a dead cell has 3 living neighbors, the dead cell comes back to life.
	
	Our version of the game of life uses a 100 by 100 array of "Cell" objects. It was created using swing
	for the GUI, and awt for the mouse events. Instead of using a standard timer, we used the thread.sleep
	function in a while loop. Kevin was responsible for the underlying algorithms which determined the behavior
	of the cells, while I worked primarily on the GUI. We designed the program to be capable of running at different
	time steps, which can be specified by the user.   
	
*/

//Class inherits JFrame and actionlistener.
public class Main extends JFrame implements ActionListener{

	//Declarations of references/variables.
	public Cell[][] cellArray;
	public boolean end = false;
	public boolean start = false;
	JButton button1;
	JButton button2;
	JButton button3;
   JButton button4;
	JLabel label1;
	JLabel label2;
	JTextField box1;
	
	//Default timestep is 10.
	int time = 100;
	
	int numNeighbors;
	String rule1 = "23";
	String rule2 = "3";
	
	public Main()
	{
		super("Class");
		
		setLayout(null);
		
		//Allocates 100 by 100 array of cells, creates and place buttons/labels.
		cellArray = new Cell[100][100];
		button1 = new JButton("Finished Placing");
		button2 = new JButton("Random");
		button3 = new JButton("Clear/Reset");
      button4 = new JButton("Set Rules");
		box1 = new JTextField(3);
		label1 = new JLabel("TimeStep:");
		label2 = new JLabel("");
		label1.setForeground(Color.GREEN);
		label2.setForeground(Color.RED);
		
		
		//instantiates each element in 100 by 100 array of cells
		//to disabled cells.
		for(int i = 0; i < 100; i++)
		{
			for(int j = 0; j < 100; j++)
			{
				cellArray[i][j] = new Cell(false);	
			}
		}
		
		//Adds Components, other GUI initialization.
		setContentPane(new DrawPane());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000,1000);
		setResizable(false);
		
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
      button4.addActionListener(this);
	   add(button1);
	   add(button2);
		add(button3);
      add(button4);
	   add(label1);
	   add(box1);
		setVisible(true);
		button1.setLocation(500,950);
      
		//This is the main loop. It runs after the Main window is setup.
		//It only exits when the boolean end is true.
		while(!end)
		{
			//Control statement which is checked every timestep.
			//It calls the algorithm checkArray, which handles the
			//updating of the cells.
			//Note: start is set to true by button1.
			if(start == true)
			{
				try{
					Thread.sleep(time);
					checkArray();
		
				}catch(Exception e){}	
			} 
			//Repaints at end of timestep.
			repaint();
		}
	}
															  
	public void setRules()
	{
		rule1 = JOptionPane.showInputDialog(null, "Enter rule 1(Number of neighbors that will result cause cell to survive):");
		rule2 = JOptionPane.showInputDialog(null, "Enter rule 2(Number of neighbors to revive cell):");
	}		
	//This is the main class which the GUI is being drawn to. It inherits JPanel,
	//MouseListener, and MouseMotionListener.
	class DrawPane extends JPanel implements MouseListener, MouseMotionListener{
	  
		  public DrawPane()
		  {
		     addMouseListener(this);
           addMouseMotionListener(this); 
		  }
		  
		  //Handles all drawing to the JPanel.
        public void paintComponent(Graphics g){
        	
        	g.setColor(Color.BLACK);
        	g.fillRect(0,0,1000,1000);
        	g.setColor(Color.DARK_GRAY);
        	
        	for(int i = 0; i < 100; i++)
        	{
        		g.drawLine(0, i*10, 1000, i*10);
        		g.drawLine(i*10,0,i*10,1000);
        	}
        	
        	g.setColor(Color.WHITE);
        	for(int i = 0; i < 100; i++)
    		{
    			for(int j = 0; j < 100; j++)
    			{
    				if(cellArray[i][j].enabled == true)
    				{
    					g.fillRect(i*10,j*10,10,10);
    				}
    					
    			}
    		}
      }
		//This event is essentially what allows cells to be drawn when
		//the mouse is held down by the user.
      public void mouseDragged(MouseEvent e)
      {
         System.out.println("mouse Entered " + e.getX() + e.getY());
			if(cellArray[e.getX()/10][e.getY()/10].enabled == false)
			{
				cellArray[e.getX()/10][e.getY()/10].enabled =true;
				System.out.println("activated");
				repaint();
			}
      }
      //Just blank overriden methods from
		//MouseEvent class.
		public void mouseEntered(MouseEvent e)
		{
		}
	
		public void mouseExited(MouseEvent e)
		{
		}
	
		public void mouseReleased(MouseEvent e)
		{
		}
	   
      public void mouseMoved(MouseEvent e)
		{
		}
      
		public void mousePressed(MouseEvent e)
		{
        
		}
	   //MouseClick procedure which handles placing
		//a single cell at a time.
		public void mouseClicked(MouseEvent e)
		{
			System.out.println("mouse Entered " + e.getX() + e.getY());
			if(cellArray[e.getX()/10][e.getY()/10].enabled == false)
			{
				cellArray[e.getX()/10][e.getY()/10].enabled =true;
				System.out.println("activated");
				repaint();
			}
			else
			{
				cellArray[e.getX()/10][e.getY()/10].enabled =false;
				System.out.println("deactivated");
				repaint();
			}
			
		}
	}
	
	public boolean rule1()
	{
		boolean rule = false;
		
		for(int i = 0; i < 8; i++)
		{
			if(rule1.contains(""+(i+1)))
			{
				rule = rule || numNeighbors == (i+1);
			}
		}
		return rule;
	}
	
	public boolean rule2()
	{
		boolean rule = false;
		
		for(int i = 0; i < 8; i++)
		{
			if(rule2.contains(""+(i+1)))
			{
				rule = rule || numNeighbors == (i+1);
			}
		}
		return rule;
	}
				
			
	//All button actions are handled here. Rather than creating multiple different
	//Each button uses this one. The function checks the value of the sender argument
	//to determine what action to take.
	
	//Button1(Text-'Finished Placing')- Starts Game after being drawn.
	//Button2(Text-'Random')-Randomly Generates cells using random function.
	//Button3(Text-'Clear/Reset')-Resets and stops game. Allows to be redrawn.
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == button1)
		{
			if(box1.getText().length() < 5 && box1.getText().length() > 0)
			{
				time = Integer.parseInt(box1.getText());
				start = true;
				System.out.println(start);
			}
			else
			{
				Frame frame = new Frame();
				JOptionPane.showMessageDialog( frame,"Please Enter a value for Timestep between 1 and 9999.");
			}
    	}
		if(e.getSource() == button2 && start == true)
		{
			Random r = new Random();
			for(int i = 0; i < 100; i++)
			{
				for(int j = 0; j < 100; j++)
				{
					if(r.nextInt(10) == 1)
					{
						cellArray[i][j].enabled = true;
					}
				}
			}
			repaint();
			start = true;
		}
		if(e.getSource() == button3 && start == true)
		{
			for(int i = 0; i < 100; i++)
			{
				for(int j = 0; j < 100; j++)
				{
					cellArray[i][j].enabled = false;	
				}
			}
			start = false;
			repaint();
		}
      
      if(e.getSource() == button4 && start == false)
      {
         setRules();
      }
		
	}

   public void finalize()
   {
   }
	
	//This is the algorithm which checks the array and determines how to update it. First, a copy of array
	//cellArray is created. Then, the number of neighbors for each cell in cellArray is determined, and appropriate
	//performed on the cells. 
	
	//Rules:
	//2-3 neighbors: Cell lives
	//3 neighbors(cell dead): cell revives
	//any other amount of neighbors:cell dies
	public void checkArray()
	{
		Cell tempArray[][] = new Cell[100][100];
		
		for(int i = 0; i < 100; i++)
		{
			for(int j = 0; j < 100; j++)
			{
				tempArray[i][j] = new Cell(false);
				tempArray[i][j].enabled = cellArray[i][j].enabled;
			}
		}
		
		for(int i = 0; i < 100; i++)
		{
			for(int j = 0; j < 100; j++)
			{
			   numNeighbors = 0;
				
				if(i<99 && tempArray[i+1][j].enabled == true)
					numNeighbors+=1;
				if(i>0 && tempArray[i-1][j].enabled == true)
					numNeighbors+=1;
				if(j<99 && tempArray[i][j+1].enabled == true)
					numNeighbors+=1;
				if(j>0 && tempArray[i][j-1].enabled == true)
					numNeighbors+=1;
				
				if(i<99 && j < 99 && tempArray[i+1][j+1].enabled == true)
					numNeighbors+=1;
				if(i<99 && j > 0 && tempArray[i+1][j-1].enabled == true)
					numNeighbors+=1;
				if(i>0 && j < 99 && tempArray[i-1][j+1].enabled == true)
					numNeighbors+=1;
				if(i>0 && j > 0 && tempArray[i-1][j-1].enabled == true)
					numNeighbors+=1;
				
				if(rule1())
				{
					if(rule2())
					   cellArray[i][j].enabled = true;
				}
				else
					cellArray[i][j].enabled = false;
			}
		}
	}
	
	public static void main(String[] args)
	{
		Main mainR = new Main();
	}
	

}

