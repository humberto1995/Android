package br.com.fiap.notas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Pattern;

import br.com.fiap.notas.util.ArquivoDB;

public class CadastrarLogin_Activity extends AppCompatActivity {

    private ArquivoDB arquivoDB;
    private HashMap<String,String>mapDados;
    private EditText etNome , etSobreNome, etNascimento, etEmail, etSenha;
    private RadioGroup rgSexo;

    private final String ARQ = "dados.txt";
    private final String SP = "dados";
    boolean dadosOK = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_login);

        arquivoDB = new ArquivoDB();
        mapDados = new HashMap<>();
        etNome = (EditText) findViewById(R.id.edtNome);
        etSobreNome = (EditText) findViewById(R.id.edtSobreNome);
        etNascimento = (EditText) findViewById(R.id.edtDataNascimento);
        etEmail = (EditText) findViewById(R.id.edtEmail);
        etSenha = (EditText) findViewById(R.id.edtSenha);
        rgSexo = (RadioGroup) findViewById(R.id.rgSexo);

    }

    //Método que captura os dados do formulario e valida o correto preenchimento
    public boolean capturaDados() {

        String nome,sobreNome,nascimento,email,senha,sexo;

        nome = etNome.getText().toString();
        sobreNome = etSobreNome.getText().toString();
        nascimento = etNascimento.getText().toString();
        email = etEmail.getText().toString();
        senha = etSenha.getText().toString();

        //retorna o ID do RadioButton que esta chegando no RadioGroup
        int sexoId = rgSexo.getCheckedRadioButtonId();
        //busca o RadioButton selecionado pelo id retornado em getCheckedRadioButtonId()
        RadioButton rbSexo = (RadioButton) findViewById(sexoId);

        //Validação de preenchimento do formulario
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                !TextUtils.isEmpty(senha) &&
                !TextUtils.isEmpty(nome) &&
                !TextUtils.isEmpty(sobreNome) &&
                !TextUtils.isEmpty(nascimento) &&
                (sexoId != -1) ){

            dadosOK = true;

            sexo = rbSexo.getText().toString();
            mapDados.put("usuario",email);
            mapDados.put("senha",senha);
            mapDados.put("nome",nome);
            mapDados.put("sobreNome",sobreNome);
            mapDados.put("nascimento",nascimento);
            mapDados.put("sexo",sexo);

        }else{
            Toast.makeText(this,R.string.validacao_conta,Toast.LENGTH_SHORT).show();
        }

        return dadosOK;

    }

    public void gravarChaves(View v){

        if(capturaDados()){
            arquivoDB.gravarChaves(this,SP,mapDados);
            Toast.makeText(this,R.string.cadastro_ok,Toast.LENGTH_SHORT).show();
        }

    }
    public void excluirChaves(View v){

        if(capturaDados()){
            arquivoDB.excluirChaves(this,SP,mapDados);
            Toast.makeText(this,R.string.exclusao_ok,Toast.LENGTH_SHORT).show();
        }

    }

    public void carregarChaves(View v){

        etNome.setText(arquivoDB.retornarValor(this,SP,"nome"));
        etSobreNome.setText(arquivoDB.retornarValor(this,SP,"sobreNome"));
        etNascimento.setText(arquivoDB.retornarValor(this,SP,"Nascimento"));
        etEmail.setText(arquivoDB.retornarValor(this,SP,"email"));
        etSenha.setText(arquivoDB.retornarValor(this,SP,"senha"));

        //Ao fazermos comparações com String devemos buscá-las do arquivo strings.xml
        if(arquivoDB.retornarValor(this,SP,"sexo")
                .equals(getString(R.string.feminino))){
            rgSexo.check(R.id.rbFeminino);
        }else{
            rgSexo.check(R.id.rbMasculino);
        }

    }

    public boolean varificarChaves(){

        if(arquivoDB.verificarChave(this,SP,"usuario") &&
           arquivoDB.verificarChave(this,SP,"senha")){
           Toast.makeText(this,R.string.login_ok, Toast.LENGTH_SHORT).show();
            return true;
        }else{
            Toast.makeText(this,R.string.login_nok, Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    //grava um arquivo de nome arquivo.txt com o conteudo em string contido no HashMap
    public void gravarArquivo (){

        if(capturaDados()){

            try {

                arquivoDB.gravarArquivo(this,ARQ,mapDados.toString());
                Toast.makeText(this,R.string.arquivo_ok, Toast.LENGTH_SHORT).show();

            } catch (IOException e) {

                Toast.makeText(this,R.string.arquivo_nok, Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }

        }

    }

    public void lerArquivo(View v){

        String txt = "Vazio!";

        try {

            txt = arquivoDB.lerArquivo(this,ARQ);

        }catch (Exception e){

            Toast.makeText(this, R.string.ler_arquivo_nok, Toast.LENGTH_SHORT).show();

        }

        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();

    }

    public void excluirArquivo(View v){

        try {

            arquivoDB.excluirArquivo(this,ARQ);
            Toast.makeText(this, R.string.exclusao_arq_ok, Toast.LENGTH_SHORT).show();

        } catch (IOException e) {

            e.printStackTrace();
            Toast.makeText(this, R.string.exclusao_arq_nok, Toast.LENGTH_SHORT).show();

        }

    }
}
