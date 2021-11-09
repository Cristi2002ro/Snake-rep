package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SnakeFrame extends JFrame{

    SnakeFrame(){
        this.add(new SnakePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setIconImage(new ImageIcon("C:\\Users\\Alexandru Duna\\IdeaProjects\\Snake\\res\\1.png").getImage());
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }


}
