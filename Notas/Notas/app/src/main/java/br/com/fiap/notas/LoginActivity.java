package br.com.fiap.notas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import br.com.fiap.notas.util.ArquivoDB;

public class LoginActivity extends AppCompatActivity {

    EditText edEmail, edSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edEmail = (EditText) findViewById(R.id.edtEmail);
        edSenha = (EditText) findViewById(R.id.edtSenha);
    }

    public void logar(View v){
        if(validarLogin(edEmail.getText().toString(), edSenha.getText().toString())) {
            Intent toNotasCard = new Intent(this, NotasCardActivity.class);
            startActivity(toNotasCard);
        }else{
            Toast.makeText(this, R.string.erro_login, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarLogin(String email, String senha) {

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && !TextUtils.isEmpty(senha)) {
            ArquivoDB arquivoDB = new ArquivoDB();

            String u = arquivoDB.retornarValor(this, "dados", "usuario");
            String s = arquivoDB.retornarValor(this, "dados", "senha");

            if (u.equals(email) && s.equals(senha)) {
                return true;
            }
        }
        return false;
    }


    public void gravar(View v){
        HashMap<String, String> mapDados = new HashMap<>();
        mapDados.put("usuario", edEmail.getText().toString());
        mapDados.put("senha", edSenha.getText().toString());

        ArquivoDB arquivoDB = new ArquivoDB();
        arquivoDB.gravarChaves(this, "dados", mapDados);

        Toast.makeText(this, "Dados gravados com sucesso.", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        edSenha.setText("");
    }
}
