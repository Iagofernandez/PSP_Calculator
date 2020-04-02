package psp_calculadora_servidor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;

import java.net.Socket;
import javax.swing.JOptionPane;

public class PSP_Calculadora_Servidor {

    public static void main(String[] args) {

        int port = Integer.parseInt(JOptionPane.showInputDialog("Puerto?"));

        try {
            
            System.out.println("Creando socket ");
            ServerSocket serverSocket = new ServerSocket(port);

          
            while (true) {
              
                System.out.println("Aceptando");
                Socket newSocket = serverSocket.accept();

              
                new Cliente(newSocket).start();
            }
        } catch (IOException ex) {
            System.out.println("Error al recibir conexiones");
        }
    }
}


class Cliente extends Thread {

    private Socket socket;
    private InputStream is;
    private OutputStream os;

   
    public Cliente(Socket socket) throws IOException {
        this.socket = socket;
        is = socket.getInputStream();
        os = socket.getOutputStream();
        System.out.println("Conexión recibida");
    }

   
    public static double suma(double num1, double num2) {
        System.out.println(" " + num1 + " + " + num2);
        return num1 + num2;
    }

   
    public static double resta(double num1, double num2) {
        System.out.println(" " + num1 + " - " + num2);
        return num1 - num2;
    }

   
    public static double multiplicacion(double num1, double num2) {
        System.out.println(" " + num1 + " * " + num2);
        return num1 * num2;
    }

   
    public static double division(double num1, double num2) {
        System.out.println(" " + num1 + " / " + num2);
        return num1 / num2;
    }

 
    

    @Override
    public void run() {
        try {
            
            byte[] mensajeRecibido = new byte[25];
            is.read(mensajeRecibido);
            System.out.println("Mensaje recibido: " + new String(mensajeRecibido));

            
            String[] cadena = new String(mensajeRecibido).split(" ");
            double resultado = 0;
            
                switch (cadena[1]) {
                  
                    case "+":
                        resultado = suma(Double.valueOf(cadena[0]), Double.valueOf(cadena[2]));
                        break;
                   
                    case "-":
                        resultado = resta(Double.valueOf(cadena[0]), Double.valueOf(cadena[2]));
                        break;
                  
                    case "*":
                        resultado = multiplicacion(Double.valueOf(cadena[0]), Double.valueOf(cadena[2]));
                        break;
                   
                    case "/":
                        resultado = division(Double.valueOf(cadena[0]), Double.valueOf(cadena[2]));
                        break;
                   
                   
                }
            
           
            System.out.println("Enviando mensaje: " + resultado);
            String mensajeEnviado = String.valueOf(resultado);
            os.write(mensajeEnviado.getBytes());
            System.out.println("Mensaje enviado");
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
             
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error al cerrar la conexión");
            }
        }
    }
}