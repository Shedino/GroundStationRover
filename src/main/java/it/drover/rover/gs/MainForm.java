package it.drover.rover.gs;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.MAVLink.MAVLinkPacket;
import com.MAVLink.aslaradio.msg_global_position_int;
import jssc.*;

/**
 * Created by dmengoli on 23/10/2015.
 */
public class MainForm {
    private JButton sparaButton;
    private JTextField fieldLat;
    private JTextField fieldLon;
    private JTextField fieldVel;
    private JTextField fieldHeading;
    private JPanel panelMain;
    private JComboBox cbComPorts;
    private JButton collegatiButton;
    private JTextPane debugPane;
    private JButton scollegatiButton;

    protected SerialPort serialPort;

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public MainForm() {


        sparaButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Bototne.");
                //TODO: Genero MAvlink e sparo sulla seriale
                Waypoint wpobj = new Waypoint();
                getData(wpobj);
                msg_global_position_int mwp = new msg_global_position_int();
                mwp.time_boot_ms=0;
                mwp.lat=wpobj.getLatMavlink();
                mwp.lon=wpobj.getLonMavlink();
                mwp.alt = 0;
                mwp.relative_alt = 0;
                mwp.vx=wpobj.getVelMavlink();
                mwp.vy = 0;
                mwp.vz = 0;
                mwp.hdg = wpobj.getHdgMavlink();
                MAVLinkPacket mavpkt = mwp.pack();
                try{
                    serialPort.writeBytes(mavpkt.encodePacket());
                    System.out.println("MAV Packet sent.");
                    System.out.println(mavpkt.toString());
                }catch (SerialPortException ex){
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });


        collegatiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (serialPort!=null && serialPort.isOpened()){
                    try {
                        serialPort.closePort();
                    }catch (SerialPortException ex){
                        //Niente
                    }
                }
                serialPort = new SerialPort(cbComPorts.getSelectedItem().toString());
                try {
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_57600,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);

                    serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);

                }catch(SerialPortException ex){
                    System.out.println(ex.getMessage());
                    ex.printStackTrace();
                }

            }
        });
        scollegatiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (serialPort!=null && serialPort.isOpened()){
                    try {
                        serialPort.closePort();
                    }catch (SerialPortException ex){
                        //Niente
                    }
                }
                serialPort=null;
            }
        });
    }

    public void setData(Waypoint data) {
        fieldLat.setText(data.getLatStr());
        fieldLon.setText(data.getLonStr());
        fieldVel.setText(data.getVelStr());
        fieldHeading.setText(data.getHdgStr());
    }

    public void getData(Waypoint data) {
        data.setLatStr(fieldLat.getText());
        data.setLonStr(fieldLon.getText());
        data.setVelStr(fieldVel.getText());
        data.setHdgStr(fieldHeading.getText());
    }

    public boolean isModified(Waypoint data) {
        if (fieldLat.getText() != null ? !fieldLat.getText().equals(data.getLatStr()) : data.getLatStr() != null)
            return true;
        if (fieldLon.getText() != null ? !fieldLon.getText().equals(data.getLonStr()) : data.getLonStr() != null)
            return true;
        if (fieldVel.getText() != null ? !fieldVel.getText().equals(data.getVelStr()) : data.getVelStr() != null)
            return true;
        if (fieldHeading.getText() != null ? !fieldHeading.getText().equals(data.getHdgStr()) : data.getHdgStr() != null)
            return true;
        return false;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        String[] portNames = SerialPortList.getPortNames();
        cbComPorts = new JComboBox();
        DefaultComboBoxModel cbmodel = new DefaultComboBoxModel();
        for (int i = 0; i < portNames.length; i++){
            System.out.println(portNames[i]);
            cbmodel.addElement(portNames[i]);
        }
        cbComPorts.setModel(cbmodel);

    }


    private class PortReader implements SerialPortEventListener {

        public void serialEvent(SerialPortEvent event) {
            if(event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    String receivedData = serialPort.readString(event.getEventValue());
                    //System.out.println("Received response from port: " + receivedData);
                    debugPane.setText(debugPane.getText()+receivedData);
                }
                catch (SerialPortException ex) {
                    System.out.println("Error in receiving response from port: " + ex);
                }
            }
        }
    }
}
