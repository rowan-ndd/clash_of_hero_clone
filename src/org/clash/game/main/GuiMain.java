package org.clash.game.main;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.*;  

public class GuiMain
{
    private static void createAndShowGUI() 
    {
        JFrame frame = new JFrame("ClashGame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.setContentPane(new DrawPane());
        //Display the window.
        frame.pack();
        frame.setSize(400, 400);
        frame.setVisible(true);
    }	
	
    public static void main(String[] args) 
    {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                createAndShowGUI();
            }
        });
    }	
    
    static class DrawPane extends JPanel
    {
        public void paintComponent(Graphics g)
        {
          //draw on g here e.g.
          //g.fillRect(20, 20, 100, 200);
          g.drawRoundRect(0,0,100,100,10,10);
        }
     }    
}
