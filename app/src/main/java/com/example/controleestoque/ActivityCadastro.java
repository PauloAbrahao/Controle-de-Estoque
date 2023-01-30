package com.example.controleestoque;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityCadastro extends AppCompatActivity {

//  Declarações das funções para capturar os valores inseridos nos campos
    private EditText getEditNome(){ return (EditText) findViewById(R.id.editTextName);}

    private EditText getEditValor(){ return (EditText) findViewById(R.id.editTextValue);}

    private EditText getEditQuantidadeAtual(){ return (EditText) findViewById(R.id.editTextQuantidadeAtual);}
    private EditText getEditQuantidadeMinima(){ return (EditText) findViewById(R.id.editTextQuantidadeMinima);}
    private Button getBtnUpdat(){ return (Button) findViewById(R.id.btnCadastrarAlterar);}
    private Button getBtnDelet() {return (Button) findViewById(R.id.btnExcluir);}

    //  Declarações das variaveis para receber os valores antigos do banco de dados (quando for ATUALIZAR
    // um produto, os campos estarão brevemente preenchidos)
    private String  oldNameUpdatePK, oldValorUpdatePK, oldQuantidadeAtualUpdatePK, oldQuantidadeMinUpdatePK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        //verifica se a activity foi aberta através de algum item da lista
        boolean isModeUpdate = isModeUpdateAndConfigure();

        // quando abrir a tela de inserir, o teclado será exibido para preencher o campo NOME
        if(getEditNome().requestFocus()){
            //exibe teclado
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        // Verifica se será atualizado ou criado um produto
        getBtnUpdat().setOnClickListener(v -> {
            // Instanciado o banco de dados
            HelperDB helperDB = new HelperDB(ActivityCadastro.this);

            //  Declarações das variaveis para capturar os valores novos inseridos nos campos dentro da tela
            // de EDITAR
            String newNome = getEditNome().getText().toString().trim();
            Double newValor = Double.valueOf(getEditValor().getText().toString().trim());
            Integer newQuantidadeAtual = Integer.valueOf(getEditQuantidadeAtual().getText().toString().trim());
            Integer newQuantidadeMinima = Integer.valueOf(getEditQuantidadeMinima().getText().toString().trim());

            try {
                if(isModeUpdate) {
                    // Se a verificacao do ModeUpdate for true entao serão atualizados os valores do produto
                    // no banco
                    if (newQuantidadeAtual >= newQuantidadeMinima) {
                        helperDB.Update(new String[]{oldNameUpdatePK, oldValorUpdatePK, oldQuantidadeAtualUpdatePK, oldQuantidadeMinUpdatePK}, newNome, newValor, newQuantidadeAtual, newQuantidadeMinima);
                        Toast.makeText(ActivityCadastro.this, "Atualizado com Sucesso!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);

                        finish();
                    } else {
                        Toast.makeText(ActivityCadastro.this, "Repor estoque do produto!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if (newQuantidadeAtual >= newQuantidadeMinima) {
                        // se for false, serão cadastrados os produtos no banco
                        long rowid = helperDB.Insert(newNome, newValor, newQuantidadeAtual, newQuantidadeMinima);

                        Toast.makeText(ActivityCadastro.this, "Cadastrado com Sucesso! nº:" + rowid, Toast.LENGTH_SHORT).show();
                        getEditNome().setText("");
                        getEditValor().setText("");
                        getEditQuantidadeAtual().setText("");
                        getEditQuantidadeMinima().setText("");

                        getEditNome().requestFocus();
                        setResult(RESULT_OK);
                    } else {
                        Toast.makeText(ActivityCadastro.this, "Repor estoque do produto!", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (SQLException e) {
                new AlertDialog.Builder(this).setTitle("SQL Erro").setMessage(e.getMessage()).setNeutralButton("OK", null).show();
            }
        });

        // Verifica se o botao de apagar foi acionado
        getBtnDelet().setOnClickListener(v -> {
            confirmDeleteDialog();
        });

        getBtnUpdat().setOnLongClickListener(v -> {
            new HelperDB(this).InsertTest(Integer.valueOf(getEditValor().getText().toString()));
            Toast.makeText(ActivityCadastro.this, "Teste Inseridos!", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            return true;
        });
    }

    // Funcao para apagar os produtos
    private void confirmDeleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Deletar "+ oldNameUpdatePK +" ?")
                .setMessage("Tem certeza de que deseja apagar "+ oldNameUpdatePK +"?");

        builder.setPositiveButton("Sim", (dialog, which) -> {
                try {
                    String[] PKClustered = new String[]{oldNameUpdatePK, oldValorUpdatePK, oldQuantidadeAtualUpdatePK, oldQuantidadeMinUpdatePK};
                    HelperDB hdb = new HelperDB(ActivityCadastro.this);
                    hdb.Delete(PKClustered);
                    setResult(RESULT_OK);
                    Toast.makeText(ActivityCadastro.this, "Deletado com Sucesso!", Toast.LENGTH_SHORT).show();
                    finish();
                }catch (SQLException e) {
                    new AlertDialog.Builder(ActivityCadastro.this).setTitle("SQL Erro").setMessage(e.getMessage()).setNeutralButton("OK", null).show();
                }
        });builder.setNegativeButton("Não", (dialog, which) -> {

        });
        builder.create().show();
    }

    // Quando é apertado o produto para edição, é acionada essa funcao para capturar os valores
    // antigos do banco e setá-los como hint, ou um valor brevemente preenchido no campo
    // Assim, quando abrir um produto para editá-lo, será possivel ver os valores que estavam
    // inseridos anteriormente
    private Boolean isModeUpdateAndConfigure(){
        if(getIntent().hasExtra("nome") && getIntent().hasExtra("valor") && getIntent().hasExtra("quantidadeAtual") && getIntent().hasExtra("quantidadeMin")) {

            //configura activity para modo update
            oldNameUpdatePK = getIntent().getStringExtra("nome");
            oldValorUpdatePK = getIntent().getStringExtra("valor");
            oldQuantidadeAtualUpdatePK = getIntent().getStringExtra("quantidadeAtual");
            oldQuantidadeMinUpdatePK = getIntent().getStringExtra("quantidadeMin");

            setTitle("Alterar");

            getEditNome().setText(oldNameUpdatePK);
            getEditValor().setText(oldValorUpdatePK);
            getEditQuantidadeAtual().setText(oldQuantidadeAtualUpdatePK);
            getEditQuantidadeMinima().setText(oldQuantidadeMinUpdatePK);

            getBtnUpdat().setText("Atualizar");
            getBtnDelet().setVisibility(View.VISIBLE);

            return true;
        }
        return false;
    }
}
