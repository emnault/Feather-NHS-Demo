package com.example.telegramactivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.ProcessBuilder;
import java.io.IOException;
//import com.android.volley.*;
//import com.android.volley.RequestQueue;
//import com.android.volley.toolbox.*;
import java.io.DataOutputStream;
import android.os.StrictMode;
import java.net.UnknownHostException;





import com.example.telegramactivity.databinding.FragmentFirstBinding;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    //private Socket socket;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
//        SocketConnection sock = new SocketConnection();
//        sock.connectSocket();
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//
//        StrictMode.setThreadPolicy(policy);
//        try{
//            Socket s=new Socket("137.195.117.241",65432);
//            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
//            dout.writeUTF(" ");
//            dout.flush();
//            dout.close();
//            s.close();
//        }catch(Exception e){System.out.println(e);}



//        try {
//            InetAddress laptopIP = InetAddress.getLocalHost();
//            Socket clientSocket = new Socket(laptopIP, 65432);
////            Process proc = new ProcessBuilder("curl", "--data", "' '", "localhost:65432").start();
//            String command = "curl --data '' localhost:65432";
//            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
//        } catch (Exception e) {
//            System.out.println(e);
//        }

//        try {
//            ServerSocket ss = new ServerSocket(65432);
//            Socket clientSocket = ss.accept();//establishes connection
//            Process proc = new ProcessBuilder(args).start();
//        }catch(Exception e){
//            System.out.println(e);
//        }

//        try{
//            ServerSocket server = new ServerSocket(65432);
//            Socket client = server.accept();
//        } catch (IOException e) {
//            System.out.println("Could not listen on port 65432");
//            System.exit(-1);
//        }

//        try{
//            client = server.accept();
//        } catch (IOException e) {
//            System.out.println("Accept failed: 4321");
//            System.exit(-1);
//        }


        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                try {
//                    //InetAddress serverAddr = InetAddress.getByName("137.195.117.241");
//                    Socket socket = new Socket("137.195.117.241", 65432);
//
//                } catch (UnknownHostException e1) {
//                    e1.printStackTrace();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }

                //speak();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        speak();
                    }

                }).start();

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void speak(){
        //String command = "curl -w --data '' localhost:65432"; //IP of laptop, port matching port in service.py

//        try {
//            // Process p = processBuilder.start();
//            Process process = Runtime.getRuntime().exec("curl -w --data '' 192.168.1.243:65432");
//            //System.out.println("Curl command: ");
//            Log.i("Curl", "Curl Command Executed");
//        } catch(Exception e){
//            System.out.println(e);
//        }
        try{
            //InetAddress serverAddr = InetAddress.getByName("192.168.1.243");
            //Socket s=new Socket(serverAddr, 65432);
            Socket s=new Socket("192.168.1.243", 65432);
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
            dout.writeUTF(" ");
            dout.flush();
            dout.close();
            s.close();
        }catch(Exception e){System.out.println(e);}
    }

//    public void request() {
//
//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="https://www.google.com";
//
//// Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        textView.setText("Response is: "+ response.substring(0,500));
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                textView.setText("That didn't work!");
//            }
//        });
//
//// Add the request to the RequestQueue.
//        queue.add(stringRequest);
//    }





}