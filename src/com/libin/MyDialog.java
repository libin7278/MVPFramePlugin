package com.libin;

import javax.swing.*;
import java.awt.event.*;

public class MyDialog extends JDialog
{
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JTextField textField2;
    private JRadioButton actiRadioButton;
    private JRadioButton fragRadioButton;
    private JTextField textField3;

    private DialogCallBack mCallBack;

    public MyDialog(DialogCallBack callBack)
    {
        this.mCallBack = callBack;
        setTitle("Mvp Create Helper");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setSize(300, 200);
        setLocationRelativeTo(null);

        ButtonGroup group = new ButtonGroup();
        group.add(actiRadioButton);
        group.add(fragRadioButton);

        buttonOK.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK()
    {
        // add your code here
        if (null != mCallBack)
        {
            mCallBack.ok(textField1.getText().trim(), textField2.getText().trim(), textField3.getText().trim(), actiRadioButton.isSelected());
        }
        dispose();
    }

    private void onCancel()
    {
        // add your code here if necessary
        dispose();
    }

    public interface DialogCallBack
    {
        void ok(String author, String moduleName, String decsribe, boolean isAc);
    }

}
