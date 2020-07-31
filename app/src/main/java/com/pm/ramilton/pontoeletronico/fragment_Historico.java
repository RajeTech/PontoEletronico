package com.pm.ramilton.pontoeletronico;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.pm.ramilton.pontoeletronico.Adaptador.AcoesAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

public class fragment_Historico extends ListFragment {

    View view;

    protected List<ParseObject> mviagem;

    public fragment_Historico(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.historico_fragment, container, false);

        ParseUser usuariocorrente = ParseUser.getCurrentUser();
        String usuario = usuariocorrente.getUsername();
        if(usuariocorrente != null){
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Paradas");
            query.whereEqualTo("username",usuario);
            query.orderByDescending("CreatedAl");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> viagemobjects, ParseException e) {
                    if(e == null){
                        mviagem = viagemobjects;
                        AcoesAdapter acoesAdapter = new AcoesAdapter(getListView().getContext(), mviagem);
                        setListAdapter(acoesAdapter);
                    }else{

                    }
                }
            });
        }

        return view;
    }

}
