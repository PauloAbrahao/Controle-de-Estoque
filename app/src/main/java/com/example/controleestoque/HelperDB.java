package com.example.controleestoque;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperDB extends SQLiteOpenHelper {

    private static String DATABASE = "db_produto";
    private static int VERSION = 1;

    //criacao da tabela do banco
    String[] tables = {
            "CREATE TABLE IF NOT EXISTS produto" + "(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nome_Produto TEXT NOT NULL," +
                    "valor_Produto REAL NOT NULL," +
                    "quantidade_Atual INTEGER NOT NULL," +
                    "quantidade_Minima INTEGER NOT NULL" +
            ")"
    };

    public HelperDB(Context context){
        super(context, DATABASE,null, VERSION);
    }

    //metodo executa se o banco nao existir
    @Override
    public void onCreate(SQLiteDatabase db) {
        //é possivel somente criar uma tabela por execução
        for (String table : tables) {
            db.execSQL(table);
        }
    }

    //metodo executa se o banco ja existir e a versão mudar
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //code backup

        db.execSQL("DROP TABLE IF EXISTS produto");
        onCreate(db);
        db.close();
    }

    //metodo executa ao ser apertado o botao de inserir produto
    public long Insert(String nome, Double valor, Integer quantidadeAtual, Integer quantidadeMinima) {
        SQLiteDatabase  db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nome_Produto", nome);
        cv.put("valor_Produto", valor);
        cv.put("quantidade_Atual", quantidadeAtual);
        cv.put("quantidade_Minima", quantidadeMinima);
        long id = db.insertOrThrow("produto", null, cv);
        db.close();
        return id;
    }

    //metodo executa se o produto ja existir e for apertado para edita-lo
    public long Update(String[] PK_NomeTel, String novoNome, Double novoValor, Integer novoQuantidadeAtual, Integer novoQuantidadeMinima) {
        SQLiteDatabase  db = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("nome_Produto", novoNome);
        cv.put("valor_Produto", novoValor);
        cv.put("quantidade_Atual", novoQuantidadeAtual);
        cv.put("quantidade_Minima", novoQuantidadeMinima);

        long rows_affected = db.update("produto", cv, "nome_Produto = ? and valor_Produto = ? and quantidade_Atual = ? and quantidade_Minima = ?", PK_NomeTel);
        db.close();
        return rows_affected;
    }

    //metodo executa ao apertar para deletar um produto
    public void Delete(String[] PK_NomeTel){
        SQLiteDatabase  db = getWritableDatabase();
        db.delete("produto", "nome_Produto = ? and valor_Produto = ? and quantidade_Atual = ? and quantidade_Minima = ?", PK_NomeTel);
    }

    public void InsertTest(int Quantidade){
        SQLiteDatabase  db = getWritableDatabase();
        for (int i = 1; i <= Quantidade; i++) {

            String queryInsert = "INSERT INTO produto VALUES ( 'Contato"+i+"',"+i+");";
            db.execSQL(queryInsert);
        }
    }

    //metodo executa ao apertar para deletar TODOS os produtos
    public void DeleteAll(){
        SQLiteDatabase  db = getWritableDatabase();
        String queryInsert = "DELETE FROM produto";
        db.execSQL(queryInsert);
    }

    // metodo executa para selecionar todos os produtos do banco
    public static Cursor select_all(Context context){
        SQLiteDatabase  db = new HelperDB(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("select rowid, * from produto order by rowid desc", null);//order by rowid desc
        return cursor;
    }

    // metodo executa para pesquisar um produto especifico do banco
    public static Cursor search_item(Context context) {
        SQLiteDatabase  db = new HelperDB(context).getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from produto where nome_Produto = ?", null);

        return cursor;
    }
}
