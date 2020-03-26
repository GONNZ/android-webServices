package com.example.rest.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.rest.R;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HomeFragment extends Fragment {
    EditText etCountry;
    Button btBuscar;
    TextView tvSalida;

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);

        etCountry = root.findViewById(R.id.etCountry);
        btBuscar = root.findViewById(R.id.btBuscar);
        tvSalida = root.findViewById(R.id.tvSalida);

        btBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ConsumeWS().execute();
            }
        });

        return root;
    }

    private class ConsumeWS extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params)
        {
            String countryCode = params[0];
            final String url = "http://api.geonames.org/countryInfoJSON?formatted=true&lang=it&country=CR&username=lulu&style=full";
            HttpURLConnection httpURLConnection = null;
            String data = "";

            try {
                httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                data = parseResult(inputStream);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(httpURLConnection != null){
                    try {
                        httpURLConnection.disconnect();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
            return data;
        }


        protected void onPostExecute(String datos){
            TextView tvSalida = root.findViewById(R.id.tvSalida);
            tvSalida.setText(datos);

        }

        private String parseResult(InputStream inputStream) {
            BufferedReader bufferedReader = null;
            StringBuffer stringBuffer = new StringBuffer("");

            try {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                if (bufferedReader != null){
                    try {
                        bufferedReader.close();;
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }


            return stringBuffer.toString();

        }
    }


}