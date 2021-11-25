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

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        //Opens socket which tells Nao to speak
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Socket s=new Socket("192.168.1.197", 65432);
                    s.close();
                }catch(Exception e){System.out.println(e);}
            }

        }).start();
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Opens socket which tells Nao to speak
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Socket s=new Socket("192.168.1.197", 65432);
                            s.close();
                        }catch(Exception e){System.out.println(e);}
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
}