package com.pm.ramilton.pontoeletronico.Banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper INSTANCIA_CONEXAO;
    private static int DATABASE_VERSION = 1;
    private static String DB_FILE_NAME = "pontoeletronico.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_FILE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstancia(Context context){
        if(INSTANCIA_CONEXAO == null){
            INSTANCIA_CONEXAO = new DatabaseHelper(context);
        }

        return INSTANCIA_CONEXAO;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Cria a tabela viagem
        String sqlViagem = "create table if not exists viagem (String objectId, "+
                "Data varchar(60), Hora date, fkOnjectIdUser varchar(20))";

        sqLiteDatabase.execSQL(sqlViagem);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
