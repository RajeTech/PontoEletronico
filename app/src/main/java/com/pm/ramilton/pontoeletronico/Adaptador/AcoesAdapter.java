package com.pm.ramilton.pontoeletronico.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;
import com.pm.ramilton.pontoeletronico.Model.Acoes;
import com.pm.ramilton.pontoeletronico.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class AcoesAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mviagem;

    public AcoesAdapter(Context context, List<ParseObject> viagem){
        super(context, R.layout.itemlista, viagem);
        mContext = context;
        mviagem = viagem;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.itemlista, null);

            holder = new ViewHolder();
            holder.txtrotulohorario = (TextView) convertView.findViewById(R.id.txtrotulohorario);
            holder.txtHorario = (TextView) convertView.findViewById(R.id.txtHorario);
            holder.txtrotuloultimaacao = (TextView) convertView.findViewById(R.id.txtrotuloultimaacao);
            holder.txtultimaacao = (TextView) convertView.findViewById(R.id.txtultimaacao);

            convertView.setTag(holder);

        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ParseObject tragetoviagem = mviagem.get(position);

        String horario = tragetoviagem.getString("Hora");
        holder.txtHorario.setText(horario);

        String ultimaacao = tragetoviagem.getString("Descricao");
        holder.txtultimaacao.setText(ultimaacao);

        return convertView;
    }

    public static class ViewHolder{
        TextView txtrotulohorario;
        TextView txtHorario;
        TextView txtrotuloultimaacao;
        TextView txtultimaacao;
    }
}
