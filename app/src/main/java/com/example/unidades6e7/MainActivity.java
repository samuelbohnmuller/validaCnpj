package com.example.unidades6e7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button  btnWebService;
    EditText edtCnpj;
    TextView txtCnpj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCnpj = findViewById(R.id.edtCnpj);
        txtCnpj = findViewById(R.id.txtCnpj);
        btnWebService = findViewById(R.id.btnWebService);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnWebService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String string_url = "https://www.receitaws.com.br/v1/cnpj/" + edtCnpj.getText();
                String string_json = "";
                txtCnpj.setText(string_json);

                BufferedReader url_buffer_reader = null;
                try {
                    URL url = new URL(string_url);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                        url_buffer_reader = new BufferedReader(new InputStreamReader(inputStream));
                        string_json = BufferParaString(url_buffer_reader);
                    } finally {
                        urlConnection.disconnect();
                    }

                } catch (IOException e) {
                    Log.e("Log error", "Não foi possível conectar: " + e.getMessage());
                    if (url_buffer_reader != null) {
                        try {
                            url_buffer_reader.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                }

                try {
                    JSONObject jObj = new JSONObject(string_json);
                    txtCnpj.setText("Empresa do CNPJ: " + jObj.getString("nome") +
                            "\nCep: " + jObj.getString("cep") +
                            "\nData abertura: " + jObj.getString("abertura") +
                            "\nMunicipio: " + jObj.getString("municipio") +
                            "\nEmail: " + jObj.getString("email")
                    );
                } catch (JSONException e) {
                    txtCnpj.setText("CNPJ inválido");
                    e.printStackTrace();
                }

                Log.e("log", "detalhes: " + string_json);
            }
        });
    }

    private String BufferParaString (BufferedReader reader) {
        String linha;
        StringBuffer buffer = new StringBuffer();
        try {
            while ((linha = reader.readLine()) != null) {
                buffer.append(linha);
                buffer.append("\n");
            }
            return buffer.toString();
        } catch (IOException e) {
            Log.e("Erro:", "Erro durante a conversão do buffer para string:" + e.getMessage());
            return "";
        }
    }
}