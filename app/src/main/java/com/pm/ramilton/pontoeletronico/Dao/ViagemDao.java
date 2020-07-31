package com.pm.ramilton.pontoeletronico.Dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import com.pm.ramilton.pontoeletronico.Banco.DatabaseHelper;
import com.pm.ramilton.pontoeletronico.Model.Viagem;

public class ViagemDao {

    private final DatabaseHelper conexao;
    private SQLiteDatabase db;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public Viagem viagem;

    public ViagemDao(Context context) {

        conexao = DatabaseHelper.getInstancia(context);
        pref = context.getSharedPreferences("sessaoViagemPontoEletronico", 0); // 0 - for private mode
        editor = pref.edit();
        viagem = new Viagem();
        povoarDadosViagem();
    }

    public String retornarCodigo() {
        if (verificarSessao()) {
            return pref.getString("objectId", String.valueOf(0));
        }
        return "0";
    }

    public void inserirInformacaoViagem(String objectId, String Data, String Hora, String fkOnjectIdUser, String mensagemjustificativa) {

        editor.putString("objectId", objectId);
        editor.putString("Data", Data);
        editor.putString("Hora", Hora);
        editor.putString("fkOnjectIdUser", fkOnjectIdUser);
        editor.putString("JustificativaInicioJornada", mensagemjustificativa);
        editor.commit();
    }

    public boolean verificarSessao() {
        if (pref.getString("ObjectId", String.valueOf(0)) != "0") {
            return true;
        } else {
            return false;
        }
    }

    public void povoarDadosViagem() {
        if (verificarSessao()) {
            viagem.setObjectId(pref.getString("ObjectId", null));
            viagem.setData(pref.getString("Data", null));
            viagem.setHora(pref.getString("Hora", null));
            viagem.setFkOnjectIdUser(pref.getString("fkOnjectIdUser", null));
            viagem.setJustificativaInicioJornada(pref.getString("JustificativaInicioJornada", null));
        }
    }
}
