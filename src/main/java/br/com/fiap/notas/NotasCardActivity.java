package br.com.fiap.notas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NotasCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas_card);
    }

    public void voltar(View v){
        finish();
    }
}
