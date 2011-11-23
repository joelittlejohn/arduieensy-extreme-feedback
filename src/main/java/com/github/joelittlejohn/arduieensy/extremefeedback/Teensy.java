package com.github.joelittlejohn.arduieensy.extremefeedback;

import static java.lang.String.*;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

public class Teensy {

    static {
        System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");
    }

    private static final String COM_PORT_OWNER = "ArduieensyExtremeFeedback";
    private static final int COM_PORT_TIMEOUT = 2000;

    private static final Pin[] pins = new Pin[] {
            new Pin(0), new Pin(1), new Pin(2), new Pin(3), new Pin(4), new Pin(5), new Pin(6), new Pin(7),
            new Pin(8), new Pin(9), new Pin(10), new Pin(11), new Pin(12), new Pin(13), new Pin(14), new Pin(15),
            new Pin(16), new Pin(17), new Pin(18), new Pin(19), new Pin(20), new Pin(21), new Pin(22), new Pin(23)
    };

    private final OutputStream outputStream;

    public Teensy() throws PortAccessException {
        this.outputStream = getOutputStream();
    }

    public Pin getPin(int pinNumber) {
        return pins[pinNumber];
    }

    public void write(Pin pin, int value) throws PortAccessException {
        try {
            this.outputStream.write(new byte[] { (byte) pin.getNumber(), (byte) value });
        } catch (IOException e) {
            throw new PortAccessException(format("Unable to write value %d to Teensy pin %d", value, pin.getNumber()), e);
        }
    }

    @SuppressWarnings("unchecked")
    private OutputStream getOutputStream() throws PortAccessException {
        for (Enumeration<CommPortIdentifier> e = CommPortIdentifier.getPortIdentifiers(); e.hasMoreElements();) {

            CommPortIdentifier portId = e.nextElement();

            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                try {
                    SerialPort serialPort = (SerialPort) portId.open(COM_PORT_OWNER, COM_PORT_TIMEOUT);
                    serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                    return serialPort.getOutputStream();

                } catch (PortInUseException | UnsupportedCommOperationException | IOException ex) {
                    throw new PortAccessException("Error accessing serial port", ex);
                }
            }
        }

        throw new PortAccessException("Enumerated all ports but couldn't find an available serial port");
    }

}
